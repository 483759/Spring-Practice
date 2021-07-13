package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.dto.Member;
import com.example.demo.repository.MemberRepository;

/*
 * @SpringBootTest - 전체 빈을 등록하고 모든 application.properties를 반영해서 테스트(통합 테스트)
 * @Rollback(false) - Transaction 반영이 실패할 경우 true 옵션을 주어 롤백 가능
 * */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Rollback(false)
class MemberServiceTests {
	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	public void saveMemberTest() {
		Member member = new Member();
		member.setName("ijin");
		member.setAge(25);
		memberRepository.save(member);
		//이름과 나이를 갖는 Member를 H2 Database에 저장
		
		Member retrivedMember = memberRepository.findById(member.getId()).get();
		//member의 Id로 DB로부터 조회
		
		assertEquals(retrivedMember.getName(), "ijin");
		assertEquals(retrivedMember.getAge(), 25);
		//기존의 데이터와 비교
	}

}
