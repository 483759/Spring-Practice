package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Member;

/**
 * @author ijin
 * JpaRepository를 상속받아 SimpleJpaRepository 사용
 * SimpleJpaRepository - 기본적인 CRUD 구현되어 있음
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	
}
