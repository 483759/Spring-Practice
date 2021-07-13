package com.jwt.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	//JpaRepository를 extends하여 SQL관련 메소드 사용 가능
	
	@EntityGraph(attributePaths = "authorities")	//Lazy조회가 아닌 Eager 조회로 authorities 정보를 가져옴
	Optional<User> findOneWithAuthoritiesByUsername(String username);
	//username을 기준으로 User정보와 권한 정보를 함께 가져옴
}
