package com.example.eyagi.service;

import com.example.eyagi.model.ClientAddress;
import com.example.eyagi.model.VisitCount;
import com.example.eyagi.repository.ClientAdressRepository;
import com.example.eyagi.repository.VisitCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final VisitCountRepository visitCountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientAdressRepository clientAdressRepository;

    /*쿠키 생성 !
    oneTimeCookie 쿠키 생성
    monthCookie 쿠키 생성
*/
    public void selectOneTimeCookie(HttpServletResponse response, VisitCount toDayCount) throws UnsupportedEncodingException {
        String a = URLEncoder.encode("Hello", "UTF-8");
        ResponseCookie oneTimeCookie = ResponseCookie.from("oneTimeCookie", a)
                .domain(".eyagi99.shop")
                .sameSite("None")
                .secure(true)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", oneTimeCookie.toString());
        toDayCount.addCount();
    }

//    public void selectMonthCookie(HttpServletResponse response, VisitCount toDayCount) throws UnsupportedEncodingException {
//
//        String a = URLEncoder.encode("Welcome", "UTF-8");
//        ResponseCookie monthCookie = ResponseCookie.from("monthCookie", a)
//                .domain(".eyagi99.shop")
//                .sameSite("None")
//                .secure(true)
//                .maxAge(60 * 60 * 24 * 30)
//                .path("/")
//                .build();
//        response.addHeader("Set-Cookie", monthCookie.toString());
//        toDayCount.addVister();
//    }

    public VisitCount newDayNewCount(LocalDate nowDay) {
        List<VisitCount> allCount = visitCountRepository.findAll();
        if (allCount.size() == 0) {
            return new VisitCount();
        }
        VisitCount lastDay = allCount.get(allCount.size() - 1);
        return lastDay.newVisitCounter(nowDay.toString());

    }

    public String etRemoteAddr(HttpServletRequest request) {

        String ip = null;

        ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
//            System.out.println(1 + ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
//            System.out.println(2+ ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
//            System.out.println(3+ ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//            System.out.println(4+ ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
//            System.out.println(5+ ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
//            System.out.println(6+ ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
//            System.out.println(7+ ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
//            System.out.println(8+ ip);
        }

        return ip;

    }

    public String addressEncoder(String ip) {
        //        System.out.println("여기는 인코더 " + address);
        return passwordEncoder.encode(ip);
    }

    public void addressCheck (String ip, VisitCount toDayCount) {
        List<ClientAddress> clientAddressList = clientAdressRepository.findAll();
        int a =0;
        if ( clientAddressList.size() == 0){
            ClientAddress clientAddress = new ClientAddress(addressEncoder(ip));
            clientAdressRepository.save(clientAddress);
            toDayCount.addVister();
//            System.out.println("비어있다!");
        } else {
            for (ClientAddress c : clientAddressList){

                if(passwordEncoder.matches(ip, c.getIp())){
                    System.out.println("똑같다!");
                    a++;
                }
            }
//        System.out.println(a);
            if (a==0){
                ClientAddress clientAddress = new ClientAddress(addressEncoder(ip));
                clientAdressRepository.save(clientAddress);
                toDayCount.addVister();
            }
        }

    }


}
/*
하고싶은거
1. 일별 접속자 수 (1인 중복 가능할수 있음 . 새로고침한다던가.. 아침에 들어왔다 밤에 들어왔다던가.. 다 포함.
    일일 시간을 정해야함. 지금 로컬 타임이랑 년훨일. 변수에 있는 로컬 타임이랑 비교해서 넘겨주면 될듯 ?
    사용자가 들어온 시점의 로컬 타임과 , 현재 만들어져서 사용자 수가 더해지고 있는
    방법 - 1.사용자 ip 값을 무조건 다 받는다 ! 중복허용
2. 접속한 개인 수 ( 중복 없이. 순수 사람 수를 셈. 방법- 1. 사용자 ip 주소로 계산. 중복 안됨.
3. 가입한 회원수를 세어서 2번과 비교하여 회원 전환률 계산.
 */
