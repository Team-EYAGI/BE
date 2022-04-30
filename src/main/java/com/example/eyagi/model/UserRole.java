package com.example.eyagi.model;

public enum UserRole {
//    USER, // 사용자권한 - 청취자
//    SELLER, // 사용자권한 - 북리더
//    ADMIN // 관리자권한
    USER(Authority.USER),  // 사용자 권한
    SELLER(Authority.SELLER),  // 사용자권한 - 북리더
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String SELLER = "ROLE_SELLER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
