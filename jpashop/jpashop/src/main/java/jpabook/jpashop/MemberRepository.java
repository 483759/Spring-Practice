package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    //spring boot가 위의 annotation을 통해 Entity manager를 주입
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();  //Command랑 Query를 분리한다
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
