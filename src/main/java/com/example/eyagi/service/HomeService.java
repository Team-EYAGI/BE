package com.example.eyagi.service;

import com.example.eyagi.model.ClientAddress;
import com.example.eyagi.model.VisitCount;
import com.example.eyagi.repository.ClientAdressRepository;
import com.example.eyagi.repository.VisitCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final VisitCountRepository visitCountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientAdressRepository clientAdressRepository;

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
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;

    }

    public String addressEncoder(String ip) {
        return passwordEncoder.encode(ip);
    }

    public void addressCheck (String ip, VisitCount toDayCount) {
        List<ClientAddress> clientAddressList = clientAdressRepository.findAll();
        int a =0;
        if ( clientAddressList.size() == 0){
            ClientAddress clientAddress = new ClientAddress(addressEncoder(ip));
            clientAdressRepository.save(clientAddress);
            toDayCount.addVister();
        } else {
            for (ClientAddress c : clientAddressList){

                if(passwordEncoder.matches(ip, c.getIp())){
                    System.out.println("똑같다!");
                    a++;
                }
            }
            if (a==0){
                ClientAddress clientAddress = new ClientAddress(addressEncoder(ip));
                clientAdressRepository.save(clientAddress);
                toDayCount.addVister();
            }
        }

    }


}
