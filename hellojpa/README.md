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

#### 실행 방법
      >> cd C:\h2-2019-10-14\h2\bin
      >> h2.bat
      
      localhost:8082 접속 확인

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

### 데이터베이스 스키마 자동 생성하기
- DDL을 애플리케이션 실행 시점에 자동 생성
- 테이블 중심 -> 객체 중심
- 데이터베이스 방언을 활용해서 데이터베이스에 맞는 적절한 DDL 생성
- 이렇게 생성된 DDL은 개발 장비에서만 사용
- 생성된 DDL은 운영서버에서는 사용하지 않거나 적절히 다듬은 후 사용
- hibernate.hbm2ddl.auto
  - create: 기존 테이블 삭제 후 다시 생성(DROP+CREATE)
  - create-drop: create와 같으나 종료 시점에 테이블 DROP
  - update: 변경분만 반영(운영DB에는 사용하면 안됨)
  - validate: 엔티티와 테이블이 정상 매핑되었는지만 확인
  - none: 사용하지 않음

#### create option을 사용했을 때
: 이미 존재하는 table을 drop한 뒤 다시 생성

    Hibernate: 
        
        drop table Member if exists
        
    Hibernate: 
        
        create table Member (
          id bigint not null,
            name varchar(255),
            primary key (id)
        )

#### 데이터베이스 스키마 자동 생성하기 주의
- *운영 장비에서는 절대 create, create-drop, update 사용하면 안된다*
- 개발 초기 단계는 create 또는 update
- 테스트 서버는 update 또는 validate
- 스테이징과 운영 서버는 validate 또는 none

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

    @Column(name = "USERNAME")
    private String name;    //기본적으로 DB의 컬럼명과 매핑됨(이름 다르게 매핑 가능
    
    private int age;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;
}
```
```
create table Member(
    id bigint not null,
    name varchar(255),
    primary key (id)
)
```


**@Entity: JPA가 관리할 객체(엔티티)**

### 매핑 어노테이션

|Annotation|Description|
|----------|-----------|
|@Column|클래스 변수와 테이블 어트리뷰트 매핑|
|@Temporal|시간 관련 어노테이션|
|@Enumerated|Enum 타입 매핑(현업에서는 무조건 String)|
|@Lob||
|@Transient|매핑하지 않는 필드|

#### @Column
- 가장 많이 사용됨
- name: 필드와 매핑할 테이블의 컬럼 이름
- insertable, updatable: 읽기 전용
- nullable: null 허용여부 결정, DDL 생성시 사용
- unique: 유니크 제약 조건, DDL 생성시 사용
- columnDefinition, length, precision, scale(DDL)

#### @Temporal
- 날짜 타입 매핑

      @Temporal(TemporalType.DATE)
      private Date date;  //날짜

      @Temporal(TemporalType.TIME)
      private Date time;  //시간

      @Temporal(TemporalType.TIMESTAMP)
      private Date timestamp;     //날짜와 시간

#### @Enumerated
- 열거형 매핑
- EnumType.ORDINAL: 순서를 저장(기본값)
- EnumType.STRING: 열거형 이름을 그대로 저장, 가급적 이것 사용

      @Enumerated(EnumType.STRING)
      private RoleType roleType;

#### @Lob
- CLOB, BLOB 매핑
- CLOB: String, char[], java.sql.CLOB
- BLOB: byte[], java.sql.BLOB

      @Lob
      private String lobString;

      @Lob
      private byte[] lobByte;

#### @Transient
- 이 필드는 매핑하지 않는다.
- 애플리케이션에서 DB에 저장하지 않는 필드

<br>

### 식별자 매핑 어노테이션

|Annotation|Description|
|----------|-----------|
|@Id|DP PK와 매핑할 필드|
|@GeneratedValue||

      @Id @GeneratedValue(strategy = GenerationType.AUTO)
      private Long id;


#### 식별자 매핑 방법
- @Id(직접 매핑)
- @GeneratedValue
  - **IDENTITY**: 데이터베이스에 위임, MySQL
  - **SEQUENCE**: 데이터베이스 시퀀스 오브젝트 사용, ORACLE
    - @SequenceGenerator 필요
  - **TABLE**: 키 생성용 테이블 사용, 모든 DB에서 사용
    - @TableGenerator 필요
  - **AUTO**: 방언에 따라 자동 지정, 기본값
<br>

#### 권장하는 식별자 전략
- 기본 키 제약 조건: null 아님, 유일, 변하면 안된다
- 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대체키)를 사용하자
- 예를 들어 주민등록번호도 기본 키로 적합하지 않다
- **권장: Long + 대체키 + 키 생성전략 사용**