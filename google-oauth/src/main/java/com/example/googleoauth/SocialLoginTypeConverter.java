package com.example.googleoauth;

import org.springframework.core.convert.converter.Converter;

public class SocialLoginTypeConverter implements Converter<String, SocialLoginType> {

    /**
     * Request Path의 소문자를 ENUM을 위해 대문자로 변환
     * */
    @Override
    public SocialLoginType convert(String source) {
        return SocialLoginType.valueOf(source.toUpperCase());
    }

}
