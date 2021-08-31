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

### Test

> 🔥 EntityManager를 통한 모든 변경은 트랜잭션 내에서 이루어져야 한다. 🔥 

    @Test
    @Transactional      //트랜잭션 설정
    @Rollback(value = false)    //설정하지 않을 경우 테스트 수행 후 초기 상태로 Rollback 됨
    public void testMember() throws Exception {


## 단축키, 설정 관련 꿀팁

> Ctrl + Shift + T - Create Test Case
> Ctrl + Alt + V - Extract Variable