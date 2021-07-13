package com.jwt.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jwt.demo.token.JwtAccessDeniedHandler;
import com.jwt.demo.token.JwtAuthenticationEntryPoint;
import com.jwt.demo.token.JwtSecurityConfig;
import com.jwt.demo.token.TokenProvider;

@EnableWebSecurity		//기본적인 웹 보안 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true)		// @PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해 적용
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	//implements WebSecurityConfigurer
	//extends WebSecurityConfigurerAdapter
	
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	
	public SecurityConfig(
			TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler
			) {
		this.tokenProvider = tokenProvider;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
//		http
//		.authorizeRequests()			//HttpServletRequest를 사용하는 요청에 대한 접근제한 설정
//		.antMatchers("/api/hello").permitAll()		// /api/hello에 대한 요청은 인증 없이 접근 허용
//		.anyRequest().authenticated();		//나머지 요청은 전부 인증을 받아야함
		http
		.csrf().disable()		//토큰 사용
		
		.exceptionHandling()
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.accessDeniedHandler(jwtAccessDeniedHandler)
		//Exception Handling 클래스들 추가
		
		.and()
		.headers()
		.frameOptions()
		.sameOrigin()
		//h2-console을 위한 설정 추가
		
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		//세션을 사용하지 않기 때문에 STATELESS
		
		.and()
		.authorizeRequests()
		.antMatchers("/api/hello").permitAll()
		.antMatchers("/api/authenticate").permitAll()
		.antMatchers("/api/signup").permitAll()
		.anyRequest().authenticated()
		//로그인, 회원가입 API는 토큰이 없는 상태에서 요청이 들어오기 때문에 허가
		
		.and()
		.apply(new JwtSecurityConfig(tokenProvider));
		//JwtFilter를 addFilterBefore로 등록한 JwtSecurityConfig 클래스 적용
	}
	
	@Override
	public void configure(WebSecurity web) {
		web
		.ignoring()
		.antMatchers("/h2-console/**","/favicon.ico");
		// /h2-console 하위 모든 요청과 파비콘은 무시 
	}
}
