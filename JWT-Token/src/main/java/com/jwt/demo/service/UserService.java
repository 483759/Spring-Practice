package com.jwt.demo.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwt.demo.dto.UserDto;
import com.jwt.demo.entity.Authority;
import com.jwt.demo.entity.User;
import com.jwt.demo.repository.UserRepository;
import com.jwt.demo.util.SecurityUtil;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User signup(UserDto userDto) {
    	//회원가입 로직 수행
    	
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        //username이 DB에 이미 존재하는지 검증

        //빌더 패턴의 장점
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        //일반 회원가입은 Authority가 하나

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
        //Authority와 User 정보를 생성해서 UserRepository의 save 메소드를 통해 DB에 저장
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
    	//어떠한 username이던 user객체와 권한 정보를 가져옴
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
    	//현재 SecurityContext에 저장된 username의 정보만 가져옴 
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}
