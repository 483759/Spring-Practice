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