package com.jwt.demo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
	//Token 정보를 Response할 때 사용 
	private String token;
}
