# 내용 정리

## SpringBoot Dependencies

    springboot dependencies
    ├ spring-boot-starter-web
    │  ├ spring-boot-starter-tomcat    // Embedded Tomcat이 내장되어 있어서 자동으로 8080으로 웹 서버를 띄울 수 있음
    │  └ spring-webmvc
    ├ spring-boot-starter-thymeleaf   //템플릿 엔진
    ├ spring-boot-starter-data-jpa
    │  ├ spring-boot-starter-aop
    │  │  └ spring-boot-starter
    │  │    ├ spring-boot-core
    │  │    └ spring-boot
    │  │      └ spring-boot-starter-logging   //logback, slf4j
    │  ├ spring-boot-starter-jdbc
    │  │  └ HikariCP    //Connection Pool
    │  ├ hibernate-core
    │  ├ spring-data-jpa
    │  └ spring-jdbc
    └ spring-boot-starter-test
       ├ junit
       ├ mockito
       ├ assertj
       └ spring-test

## Server Side View 설정

👉 주로 Thymeleaf 사용

    정적 페이지
    /src/main/resources/static 내부에 저장
    동적 페이지
    /src/main/resources/templates 내부에 저장

## H2 Database 설정

> version: 1.4.200

##### H2 Database 접속

    URL - jdbc:h2:~/jpashop (최초 1회)
        - jdbc:h2:tcp://localhost/~/jpashop

<br>

##### application.yml

각종 설정 파일에 대해서는 공식 문서를 팔 것 !
https://spring.io/projects/spring-boot

    spring:
        datasource:
            url: jdbc:h2:tcp://localhost/~/jpashop:MVCC=TRUE  
            #MVCC=TRUE -> 여러 개가 한 번에 접근했을 때 빠르게 처리
            username: sa
            password:
            driver-class-name: org.h2.Driver
            #database connection과 관련된 source 설정 완료(HikariCP가 Connection Pool 설정)
        jpa:
            hibernate:
            ddl-auto: create
            properties:
            hibernate:
                show_sql: true
                format_sql: true
    logging:
        level: 
            org.hibernate.SQL: debug
            # SQL과 관련된 쿼리를 logger를 통해 출력(System.out으로 찍지 않는다!)

<br>

### Test

> 🔥 EntityManager를 통한 모든 변경은 트랜잭션 내에서 이루어져야 한다. 🔥 

    @Test
    @Transactional      //트랜잭션 설정
    @Rollback(value = false)    //설정하지 않을 경우 테스트 수행 후 초기 상태로 Rollback 됨
    public void testMember() throws Exception

## 도메인 분석 설계

> - 요구사항 분석
> - 도메인 모델과 테이블 설계
> - 엔티티 클래스 개발

### 요구사항 분석
- 회원 기능
  - 회원 등록
  - 회원 조회
- 상품 기능
  - 상품 등록
  - 상품 수정
  - 상품 조회
- 주문 기능
  - 상품 주문
  - 주문 내역 조회
  - 주문 취소
- 기타 요구사항
  - 상품은 재고 관리가 필요하다.
  - 상품의 종류는 도서, 음반, 영화가 있다.
  - 상품을 카테고리로 구분할 수 있다.
  - 상품 주문시 배송 정보를 입력할 수 있다.

### 도메인 모델 설계

- 객체를 식별해낸다
- 각각의 객체들의 관계(1대1, 1대다, 다대다)를 도출한다
- 상속 관계를 구분한다

### 엔티티 분석

예제에서는 실습을 위해 아래의 사항을 포함했다.

- 다대다 매핑은 실무에서 절대 쓰지 않는다
- 양방향 연관관계 또한 가급적 사용하지 않는다(단방향을 쓴다)

### 테이블 설계

- 임베디드 타입 정보는 테이블로 그대로 들어간다.
- 상속받은 여러 클래스의 타입을 통합해서 하나의 테이블로 만든다. DTYPE 컬럼으로 타입을 구분한다.
- 명명 규칙
  - 보통은 대문자 + _(언더스코어) 혹은 소문자 + _(언더스코어) 방식 중 하나를 정해 일관적으로 사용

### 연관관계 매핑 분석

- 1대다, 다대일의 양방향 관계
  - 연관관계의 주인은? **외래 키가 있는 쪽**
  - 연관관계의 주인이 반대쪽을 프로퍼티로 가지고 외래 키와 매핑한다
- 일대일 양방향 관계
  - 외래 키는 아무 테이블이나 가질 수 있음
  - 외래 키를 가진 쪽을 연관관계 주인으로 설정
- 다대다 양방향 관계
  - <code>@ManyToMany</code>로 매핑. 실무에서는 절대절대 쓰지 않는다

<br>

> 🔥 연관관계의 주인? 🔥
> 단순히 외래 키를 누가 관리하느냐의 문제! 비즈니스 상 우위에 대한 문제가 아님
> 외래 키를 가지지 않은 쪽이 연관관계의 주인으로 설정될 경우 외래 키를 가진 쪽의 키 값이 업데이트 되므로 관리와 유지보수에 어려움
> 추가적으로 별도 업데이트 쿼리가 발생하는 성능 문제도 있음

<br>

## 단축키, 설정 관련 꿀팁

> Ctrl + Shift + T - Create Test Case
> Ctrl + Alt + V - Extract Variable