package com.example.eyagi.security;



import com.example.eyagi.security.filter.FormLoginFilter;
import com.example.eyagi.security.provider.FormLoginAuthProvider;
import com.example.eyagi.security.filter.JwtAuthFilter;
import com.example.eyagi.security.jwt.HeaderTokenExtractor;
import com.example.eyagi.security.provider.JWTAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTAuthProvider jwtAuthProvider;
    private final HeaderTokenExtractor headerTokenExtractor;

    public WebSecurityConfig(
            JWTAuthProvider jwtAuthProvider,
            HeaderTokenExtractor headerTokenExtractor
    ) {
        this.jwtAuthProvider = jwtAuthProvider;
        this.headerTokenExtractor = headerTokenExtractor;
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(formLoginAuthProvider())
                .authenticationProvider(jwtAuthProvider);
    }


    @Override
    public void configure(WebSecurity web) {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers();


        // 서버에서 인증은 JWT로 인증하기 때문에 Session의 생성을 막습니다.
        http
                .cors().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //.and().exceptionHandling().authenticationEntryPoint(/*filter 에서의 exception 처리 */);

        /*
         * 1.
         * UsernamePasswordAuthenticationFilter 이전에 FormLoginFilter, JwtFilter 를 등록합니다.
         * FormLoginFilter : 로그인 인증을 실시합니다.
         * JwtFilter       : 서버에 접근시 JWT 확인 후 인증을 실시합니다.
         */
        http
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                //웹소켓 접근
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/chatting/**").permitAll()
                .antMatchers("/sub/**").permitAll()
                .antMatchers("/pub/**").permitAll()
                .anyRequest()
                .permitAll()
                .and()
                // [로그아웃 기능]
                .logout()
                // 로그아웃 요청 처리 URL
                .logoutUrl("/user/logout")
                //todo : addLogoutHandler-> 로그아웃 이후 일어날 로직을 입력.
                // CustomLogoutHandler -> LogoutHandler를 구현. 리프레시 토큰을 삭제해주는 로그아웃 핸들러 추가.
//                .addLogoutHandler(new CustomLogoutHandler())
                .permitAll()
                .and()
                .exceptionHandling()
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // TODO : RT ?....
                .and()
                .exceptionHandling()
                // "접근 불가" 페이지 URL 설정
                .accessDeniedPage("/forbidden.html");
    }

    @Bean
    public FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager());
        formLoginFilter.setFilterProcessesUrl("/user/login");
        formLoginFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler());
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Bean
    public FormLoginSuccessHandler formLoginSuccessHandler() {
        return new FormLoginSuccessHandler();
    }

    @Bean
    public FormLoginAuthProvider formLoginAuthProvider() {
        return new FormLoginAuthProvider(encodePassword());
    }

    private JwtAuthFilter jwtFilter() throws Exception {
        List<String> skipPathList = new ArrayList<>();

        // Static 정보 접근 허용
        skipPathList.add("GET,/images/**");
        skipPathList.add("GET,/css/**");

        // h2-console 허용
        skipPathList.add("GET,/h2-console/**");
        skipPathList.add("POST,/h2-console/**");
        // 회원 관리 API 허용
        skipPathList.add("GET,/user/**");
        skipPathList.add("POST,/user/join"); //singUp 에서 join 으로 수정!
        skipPathList.add("POST,/user/userName/check"); // 회원가입 중복 체크
        skipPathList.add("POST,/user/email/check"); // 회원가입 중복 체크


        skipPathList.add("GET,/");
        skipPathList.add("GET,/category/**");
        skipPathList.add("GET,/category");
        skipPathList.add("GET,/viewer/**");  //셀러 프로필 보기 허용

        skipPathList.add("GET,/basic.js");
        skipPathList.add("GET,/cookie");

        skipPathList.add("GET,/sellerList"); //셀러 목록 조회 허용
        skipPathList.add("GET,/favicon.ico");

        skipPathList.add("GET,/book/detail/**"); //책 상세페이지 조회 허용
        skipPathList.add("GET,/book/request"); //오디오 요청 페이지 조회 허용
        skipPathList.add("GET,/fund"); //펀드 목록 조회 허용
        skipPathList.add("POST,/fund"); //펀드 목록 조회 허용
        skipPathList.add("POST,/fund/detail/**");
        skipPathList.add("GET,/main/fund"); //메인 펀딩 목록
        skipPathList.add("GET,/search");  //검색허용
        skipPathList.add("GET,/user/kakao/callback"); //카카오 소셜 로그인 허용

        // stomp열기
//        skipPathList.add("GET,/chatting/**");
//        // 나중에 지울 파일
//        skipPathList.add("GET,/webjars/**");
//        skipPathList.add("POST,/webjars/**");


        FilterSkipMatcher matcher = new FilterSkipMatcher(
                skipPathList,
                "/**"
        );

        JwtAuthFilter filter = new JwtAuthFilter(
                matcher,
                headerTokenExtractor
        );
        filter.setAuthenticationManager(super.authenticationManagerBean());

        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // CORS 처리부분
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();



        configuration.addAllowedOrigin("https://eyagi99.shop");
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("https://eyagibook.shop");
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");
        configuration.addAllowedOriginPattern("*");
        configuration.addExposedHeader("oneTimeCookie");
        configuration.addExposedHeader("Set-Cookie");


//        configuration.addAllowedOrigin("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}