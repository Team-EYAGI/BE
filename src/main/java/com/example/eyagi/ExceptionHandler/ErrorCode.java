package com.example.eyagi.ExceptionHandler;

public enum ErrorCode {

    EXPIRED_TOKEN, //토큰 만료
    NON_LOGIN,  //토큰 없음
    INVALID_TOKEN; // 시그니처 일치 X


    public String getMessage() {
        return "TimeOut";
    }

    public String getCode() {
//        switch (this) {
//            case EXPIRED_TOKEN:
//                return "404";
//        }
        return "405";
    }

    public String getStatus() {
        return "Method Not Allowed";
    }

}
