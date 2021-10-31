package com.example.googleoauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;

    /**
     * 소셜 로그인 플랫폼 별로 Redirect 호출 분리
     * */
    public void request(SocialLoginType socialLoginType) {
        String redirectURL;

        switch (socialLoginType) {
            case GOOGLE:
                redirectURL = googleOauth.getOauthRedirectURL();
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
        }

        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
