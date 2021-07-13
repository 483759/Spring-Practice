package com.jwt.demo.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.demo.dto.LoginDto;
import com.jwt.demo.dto.TokenDto;
import com.jwt.demo.token.JwtFilter;
import com.jwt.demo.token.TokenProvider;

@RestController
@RequestMapping("/api")
public class AuthController {
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	//TokenProvider 및 AuthenticationManagerBuilder 주입
	public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto){
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
		//LoginDto 형식의 객체를 받아 UsernamePasswordAuthenticationToken 생성
		
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		//authenticationToken을 이용하여 Authentication 객체를 생성하고, authenticate 메소드를 실행 -> loadUserByUsername 메소드 실행
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//Authentication 객체를 SecurityContext에 저장 
		
		String jwt = tokenProvider.createToken(authentication);
		//Authentication 정보를 이용하여 JWT Token 생성
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer" + jwt);
		
		return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
		//JWT Token을 Response Header에도 넣어주고 TokenDto를 이용하여 Response Body에도 넣은 뒤 리턴
	}
}
