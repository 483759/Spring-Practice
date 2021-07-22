# JPA-test
JPA, H2 개발 환경 구축 실습

## 기본 설정
### H2 Database
- http://www.h2database.com/
- 최고의 실습용 DB
- 가볍다(1.5M)
- 웹용 쿼리툴 제공
- MySQL, Oracle 데이터베이스 시뮬레이션 가능
- 시퀀스, AUTO INCREMENT 기능 지원

### Maven
- https://maven.apache.org/
- 자바 라이브러리, 빌드 관리
- 라이브러리 자동 다운로드 및 의존성 관리

### persistence.xml
- JPA 설정 파일
- /META-INF/persistence.xml 위치
- javax.persistence로 시작: JPA 표준 속성
- hibernate로 시작: 하이버네이트 전용 속성

### 데이터베이스 방언
![dialect](img/dialect.png)
- JPA는 특정 데이터베이스에 종속적이지 않은 기술
- 각각의 데이터베이스가 제공하는 SQL 문법과 함수는 조금씩 다르다
  - 가변 문자: MySQL은 VARCHAR, Oracle은 VARCHAR2
  - 문자열 슬라이싱 함수: SQL 표준은 SUBSTRING(), Oracle은 SUBSTR()
  - 페이징: MySQL은 LIMIT, Oracle은 ROWNUM
- 방언: SQL 표준을 지키지 않거나 특정 데이터베이스만의 고유한 기능
- hibernate.dialect 속성에 지정
  - H2: org.hibernate.dialect.H2Dialect
  - MySQL: org.hibernate.dialect.MySQL5InnoDBDialect
- 하이버네이트는 45가지 방언 지원

## 애플리케이션 개발

- 엔티티 매니저 팩토리 설정
- 엔티티 매니저 설정
- 트랜잭션
- 비즈니스 로직(CRUD)

1. 엔티티 매니저 설정

![persistence](img/persistence.png)
```
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

EntityManager em = emf.createEntityManager();
EntityTransaction tx = em.getTransaction();
tx.begin();

try {
	Member member = new Member();
	member.setId(100L);
	member.setName("윤이진");

	em.persist(member);

	tx.commit();
} catch (Exception e) {
	tx.rollback();
} finally {
	em.close();
}

emf.close();
```

- 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
- 엔티티 매니저는 쓰레드간에 공유하면 안된다(사용하고 버려야 한다) -> 데이터베이스 커넥션 당 하나를 사용해야 함 !
- JPA의 모든 데이터 변경은 트랜잭션 안에서 실행된다

<br>

## JPA 기본 문법

```
@Entity
public class Member{
    @Id
    private Long Id;
    private String name;    //기본적으로 DB의 컬럼명과 매핑됨(이름 다르게 매핑 가능)
}
```
```
create table Member(
    id bigint not null,
    name varchar(255),
    primary key (id)
)
```

@Entity : JPA가 관리할 객체(엔티티)
@Id : DP PK와 매핑할 필드
