package com.example.googleoauth;

public interface SocialOauth {
    /**
     * 각 Social Login 페이지로 Redirect 처리할 URL Build
     * 사용자로부터 로그인 요청을 받아 Social Login Server의 인증용 code를 요청
     * */
    String getOauthRedirectURL();
}
