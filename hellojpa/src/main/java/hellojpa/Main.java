package hellojpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import hellojpa.entity.Member;
import hellojpa.entity.Team;

public class Main {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Team team = new Team();
			team.setName("TeamA");
			em.persist(team);
			
			Member member = new Member();
			//member.setId(100L);
			member.setName("윤이진");
			member.setTeam(team);	//단방향 연관관계 설정, 참조 저장
			//member.setMemberType(MemberType.ADMIN);
			
			em.persist(member);
			
			em.flush();
			em.clear();
			
		    //조회
		    Member findMember = em.find(Member.class, member.getId());
		    //연관관계 생성
		    Team findTeam = findMember.getTeam();
		    
		    //연관관계가 없음
		    //Team findTeam = em.find(Team.class, team.getId());
		    
		    List<Member> members = findTeam.getMembers();
		    for(Member mem: members) {
		    	System.out.println(mem);
		    }

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();

	}

}
