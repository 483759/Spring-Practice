package com.jwt.demo.entity;

import java.util.Set;

import javax.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity		//Database Table과 1:1로 mapping되는 객체
@Table(name = "user")	//mapping될 테이블명
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor		//자동 생성자, geter, setter 설정을 위한 lombok Annotation
public class User {
	@JsonIgnore		//Json Request시 무시될 수 있는 속성
	@Id		//Primary Key
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(name = "username", length = 50, unique = true)		//컬럼명, 속성 지정
	private String username;

	@JsonIgnore
	@Column(name = "password", length = 100)
	private String password;

	@Column(name = "nickname", length = 50)
	private String nickname;

	@JsonIgnore
	@Column(name = "activated")
	private boolean activated;

	//N:M mapping 관계 및 테이블 지정
	@ManyToMany
	@JoinTable(
			name = "user_authority",
			joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "authority_name") })
	private Set<Authority> authorities;

}
