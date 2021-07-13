package com.example.demo.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="MEMBER")		//Member Entity와 Mapping할 테이블명 지정(클래스 명과 일치한다면 명시하지 않아도 무방)
@Entity		//Member Class가 엔티티임을 명시
public class Member {
	@Id		//Persiscontext에서 식별할 수 있는 값(유니크한 값)
	@GeneratedValue(strategy = GenerationType.AUTO)		//Id가 생성되는 전략(기본은 AUTO) 설정. 각각 다른 Database의 Id 생성 전략 대응 가능
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="AGE")
	private Integer age;
}
