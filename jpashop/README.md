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
> (실제 테이블에서는 외래 키와 관련된 튜플을 바꿔야 함! 어디 값이 바뀌었을 때 외래 키를 바꿔야 되지? 객체는 변경 포인트가 두 곳이지만 테이블은 한쪽만 관계를 가지기 때문에 **연관관계의 주인**이라는 개념을 설정)  
> ex) Member, Order 클래스가 있을 때 Order에는 필드 변수로 Member가 있다 -> Order가 Member를 가지고 있어 더 가까우므로 Order가 연관관계의 주인이다

<br>

## 엔티티 클래스 개발

- Getter는 열고, Setter는 꼭 필요한 경우에만 사용하는 것을 추천
  - Entity 데이터는 조회할 일이 굉장히 많으므로 Getter는 모두 열어두는 것이 편리
  - Setter를 호출하면 데이터가 변함
  - Entity가 어떻게 변경되는지 추적하기 힘들어짐
  - Setter 대신 **변경 지점을 명확하게 하기 위한 비즈니스 메서드**를 별도로 제공
    - 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들자.
    - JPA 스펙 상 엔티티/임베디드 타입은 자바 기본 생성자를 <code>public</code>또는 <code>protected</code>로 설정해야 한다. JPA 구현 라이브러리가 객체를 생성할 때 리플렉션 같은 기술을 사용할 수 있도록 지원해야 하기 때문
- Entity 식별자는 <code>id</code>를 사용하고 PK 컬럼명은 <code>테이블명+id</code>을 사용한다(관례상). 다른 명명 규칙도 괜찮지만 일관성을 지킬 것
- LocalDateTime을 쓰면 자동으로 날짜 매핑을 해준다
- @Enumerated를 쓸 때 EnumType.ORDINAL을 쓰면 수정했을 때 다 밀린다 !! STRING을 쓰자
- 1대1 관계에서는 어디에 외래 키? 접근을 많이 하는 곳에 
- 실무에서 N대M 관계에 @ManyToMany를 쓰지 않는 이유
  - 중개 테이블에 필드를 더 추가하지 못함
  - 활용성 X

<br>

### Entity 설계 시 주의점

<br>

#### Entity에는 가급적 Setter를 사용하지 말자

Setter가 모두 열려있다. 변경 포인트가 너무 많아서, 유지보수가 어렵다.

<br>

#### ⭐모든 연관관계는 지연로딩으로 설정!⭐

> 즉시 로딩(EAGER): 엔티티를 불러올 때 연관된 엔티티들을 전부 불러옴
> 지연 로딩(LAZY): 엔티티를 불러올 때 에는 해당 엔티티만 불러오고, 엔티티의 해당 프로퍼티에 접근할 때 불러옴

- 즉시 로딩(EAGER)는 예측이 어렵고, 어떤 SQL이 실행되는지 추적하기 어렵다. 특히 JPQL을 실행할 때, N+1문제가 자주 발생한다.
- 실무에서 모든 연관관계는 **LAZY**로 설정 !!
- 연관된 엔티티를 함께 DB에서 조회해야 하면, fetch join 또는 엔티티 그래프 기능 사용
- **@XToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩**이므로 직접 지연로딩으로 설정해야 함

<br>

#### 컬렉션은 필드에서 초기화

- <code>null</code>문제에서 안전하다
- 하이버네이트가 엔티티를 영속화 할 때, 컬렉션을 감싸서 하이버네이트 제공 내장 컬렉션으로 변경한다. 따라서 <code>getOrders()</code>처럼 임의의 메서드에서 컬렉션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다. 따라서 필드 레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.
- 따라서 컬렉션 필드는 가급적 변경하지 않는 것이 안전함 !!

```java
Member member = new Member();
System.out.println(member.getOrders().getClass());
em.persist(member);
System.out.println(member.getOrders().getClass());

//출력 결과
class java.util.ArrayList
class org.hibernate.internal.PersistentBag
```

<br>

#### Cascade 옵션

```java
persist(orderItemA);
persist(orderItemB);
persist(orderItemC);
persist(orderItem);

->
persist(orderItem);
```
해당 엔티티에 영속성을 부여할 때 Cascade 옵션이 붙은 필드의 컬렉션 내의 요소들도 전부 영속성 부여. @OneToX에 사용

<br>

#### N + 1 문제

    //JPQL
    select o From order o;

    //SQL
    select * from order;

Order만 조회하는데 해당 element에 연관된 N개의 Member를 실행 시간에 함께 불러오게 됨
👉 1(원하는 엔티티 조회) + N(연관된 엔티티 모두 조회)번의 SQL문이 수행됨

<br>

### 예제 테이블 생성 SQL문

<details>
<summary>코드 보기</summary>

```Bash
2021-09-14 08:56:58.394 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    create table category (
       category_id bigint not null,
        name varchar(255),
        parent_id bigint,
        primary key (category_id)
    )
Hibernate: 
    
    create table category (
       category_id bigint not null,
        name varchar(255),
        parent_id bigint,
        primary key (category_id)
    )
2021-09-14 08:56:58.395  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418395 | took 0ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    create table category (
       category_id bigint not null,
        name varchar(255),
        parent_id bigint,
        primary key (category_id)
    )

    create table category (
       category_id bigint not null,
        name varchar(255),
        parent_id bigint,
        primary key (category_id)
    );
2021-09-14 08:56:58.395 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    create table category_item (
       category_id bigint not null,
        item_id bigint not null
    )
Hibernate: 
    
    create table category_item (
       category_id bigint not null,
        item_id bigint not null
    )
2021-09-14 08:56:58.396  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418396 | took 0ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    create table category_item (
       category_id bigint not null,
        item_id bigint not null
    )

    create table category_item (
       category_id bigint not null,
        item_id bigint not null
    );
2021-09-14 08:56:58.397 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    create table delivery (
       delivery_id bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255),
        status varchar(255),
        primary key (delivery_id)
    )
Hibernate: 
    
    create table delivery (
       delivery_id bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255),
        status varchar(255),
        primary key (delivery_id)
    )
2021-09-14 08:56:58.398  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418398 | took 0ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    create table delivery (
       delivery_id bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255),
        status varchar(255),
        primary key (delivery_id)
    )

    create table delivery (
       delivery_id bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255),
        status varchar(255),
        primary key (delivery_id)
    );
2021-09-14 08:56:58.398 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    create table item (
       dtype varchar(31) not null,
        item_id bigint not null,
        name varchar(255),
        price integer not null,
        stock_quantity integer not null,
        artist varchar(255),
        etc varchar(255),
        author varchar(255),
        isbn varchar(255),
        actor varchar(255),
        director varchar(255),
        primary key (item_id)
    )
Hibernate: 
    
    create table item (
       dtype varchar(31) not null,
        item_id bigint not null,
        name varchar(255),
        price integer not null,
        stock_quantity integer not null,
        artist varchar(255),
        etc varchar(255),
        author varchar(255),
        isbn varchar(255),
        actor varchar(255),
        director varchar(255),
        primary key (item_id)
    )
2021-09-14 08:56:58.399  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418399 | took 0ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    create table item (
       dtype varchar(31) not null,
        item_id bigint not null,
        name varchar(255),
        price integer not null,
        stock_quantity integer not null,
        artist varchar(255),
        etc varchar(255),
        author varchar(255),
        isbn varchar(255),
        actor varchar(255),
        director varchar(255),
        primary key (item_id)
    )

    create table item (
       dtype varchar(31) not null,
        item_id bigint not null,
        name varchar(255),
        price integer not null,
        stock_quantity integer not null,
        artist varchar(255),
        etc varchar(255),
        author varchar(255),
        isbn varchar(255),
        actor varchar(255),
        director varchar(255),
        primary key (item_id)
    );
2021-09-14 08:56:58.399 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    create table member (
       member_id bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255),
        name varchar(255),
        primary key (member_id)
    )
Hibernate: 
    
    create table member (
       member_id bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255),
        name varchar(255),
        primary key (member_id)
    )
2021-09-14 08:56:58.400  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418400 | took 0ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    create table member (
       member_id bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255),
        name varchar(255),
        primary key (member_id)
    )

    create table member (
       member_id bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255),
        name varchar(255),
        primary key (member_id)
    );
2021-09-14 08:56:58.401 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    create table order_item (
       order_item_id bigint not null,
        count integer not null,
        order_price integer not null,
        item_id bigint,
        order_id bigint,
        primary key (order_item_id)
    )
Hibernate: 
    
    create table order_item (
       order_item_id bigint not null,
        count integer not null,
        order_price integer not null,
        item_id bigint,
        order_id bigint,
        primary key (order_item_id)
    )
2021-09-14 08:56:58.402  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418402 | took 1ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    create table order_item (
       order_item_id bigint not null,
        count integer not null,
        order_price integer not null,
        item_id bigint,
        order_id bigint,
        primary key (order_item_id)
    )

    create table order_item (
       order_item_id bigint not null,
        count integer not null,
        order_price integer not null,
        item_id bigint,
        order_id bigint,
        primary key (order_item_id)
    );
2021-09-14 08:56:58.402 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    create table orders (
       order_id bigint not null,
        order_date timestamp,
        status varchar(255),
        delivery_id bigint,
        member_id bigint,
        primary key (order_id)
    )
Hibernate: 
    
    create table orders (
       order_id bigint not null,
        order_date timestamp,
        status varchar(255),
        delivery_id bigint,
        member_id bigint,
        primary key (order_id)
    )
2021-09-14 08:56:58.403  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418403 | took 1ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    create table orders (
       order_id bigint not null,
        order_date timestamp,
        status varchar(255),
        delivery_id bigint,
        member_id bigint,
        primary key (order_id)
    )

    create table orders (
       order_id bigint not null,
        order_date timestamp,
        status varchar(255),
        delivery_id bigint,
        member_id bigint,
        primary key (order_id)
    );
2021-09-14 08:56:58.404 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    alter table category 
       add constraint FK2y94svpmqttx80mshyny85wqr 
       foreign key (parent_id) 
       references category
Hibernate: 
    
    alter table category 
       add constraint FK2y94svpmqttx80mshyny85wqr 
       foreign key (parent_id) 
       references category
2021-09-14 08:56:58.406  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418406 | took 2ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    alter table category 
       add constraint FK2y94svpmqttx80mshyny85wqr 
       foreign key (parent_id) 
       references category

    alter table category 
       add constraint FK2y94svpmqttx80mshyny85wqr 
       foreign key (parent_id) 
       references category;
2021-09-14 08:56:58.406 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    alter table category_item 
       add constraint FKu8b4lwqutcdq3363gf6mlujq 
       foreign key (item_id) 
       references item
Hibernate: 
    
    alter table category_item 
       add constraint FKu8b4lwqutcdq3363gf6mlujq 
       foreign key (item_id) 
       references item
2021-09-14 08:56:58.408  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418408 | took 1ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    alter table category_item 
       add constraint FKu8b4lwqutcdq3363gf6mlujq 
       foreign key (item_id) 
       references item

    alter table category_item 
       add constraint FKu8b4lwqutcdq3363gf6mlujq 
       foreign key (item_id) 
       references item;
2021-09-14 08:56:58.409 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    alter table category_item 
       add constraint FKcq2n0opf5shyh84ex1fhukcbh 
       foreign key (category_id) 
       references category
Hibernate: 
    
    alter table category_item 
       add constraint FKcq2n0opf5shyh84ex1fhukcbh 
       foreign key (category_id) 
       references category
2021-09-14 08:56:58.411  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418411 | took 1ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    alter table category_item 
       add constraint FKcq2n0opf5shyh84ex1fhukcbh 
       foreign key (category_id) 
       references category

    alter table category_item 
       add constraint FKcq2n0opf5shyh84ex1fhukcbh 
       foreign key (category_id) 
       references category;
2021-09-14 08:56:58.411 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    alter table order_item 
       add constraint FKija6hjjiit8dprnmvtvgdp6ru 
       foreign key (item_id) 
       references item
Hibernate: 
    
    alter table order_item 
       add constraint FKija6hjjiit8dprnmvtvgdp6ru 
       foreign key (item_id) 
       references item
2021-09-14 08:56:58.413  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418413 | took 2ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    alter table order_item 
       add constraint FKija6hjjiit8dprnmvtvgdp6ru 
       foreign key (item_id) 
       references item

    alter table order_item 
       add constraint FKija6hjjiit8dprnmvtvgdp6ru 
       foreign key (item_id) 
       references item;
2021-09-14 08:56:58.414 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    alter table order_item 
       add constraint FKt4dc2r9nbvbujrljv3e23iibt 
       foreign key (order_id) 
       references orders
Hibernate: 
    
    alter table order_item 
       add constraint FKt4dc2r9nbvbujrljv3e23iibt 
       foreign key (order_id) 
       references orders
2021-09-14 08:56:58.416  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418416 | took 1ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    alter table order_item 
       add constraint FKt4dc2r9nbvbujrljv3e23iibt 
       foreign key (order_id) 
       references orders

    alter table order_item 
       add constraint FKt4dc2r9nbvbujrljv3e23iibt 
       foreign key (order_id) 
       references orders;
2021-09-14 08:56:58.416 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    alter table orders 
       add constraint FKtkrur7wg4d8ax0pwgo0vmy20c 
       foreign key (delivery_id) 
       references delivery
Hibernate: 
    
    alter table orders 
       add constraint FKtkrur7wg4d8ax0pwgo0vmy20c 
       foreign key (delivery_id) 
       references delivery
2021-09-14 08:56:58.418  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418418 | took 1ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    alter table orders 
       add constraint FKtkrur7wg4d8ax0pwgo0vmy20c 
       foreign key (delivery_id) 
       references delivery

    alter table orders 
       add constraint FKtkrur7wg4d8ax0pwgo0vmy20c 
       foreign key (delivery_id) 
       references delivery;
2021-09-14 08:56:58.419 DEBUG 20056 --- [  restartedMain] org.hibernate.SQL                        : 
    
    alter table orders 
       add constraint FKpktxwhj3x9m4gth5ff6bkqgeb 
       foreign key (member_id) 
       references member
Hibernate: 
    
    alter table orders 
       add constraint FKpktxwhj3x9m4gth5ff6bkqgeb 
       foreign key (member_id) 
       references member
2021-09-14 08:56:58.421  INFO 20056 --- [  restartedMain] p6spy                                    : #1631577418421 | took 2ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/jpashop

    alter table orders 
       add constraint FKpktxwhj3x9m4gth5ff6bkqgeb 
       foreign key (member_id) 
       references member

    alter table orders 
       add constraint FKpktxwhj3x9m4gth5ff6bkqgeb 
       foreign key (member_id) 
       references member;
```

</details>

<br>

## 애플리케이션 개발

계층형 구조 사용

- controller, web: 웹 계층
- service: 비즈니스 로직, 트랜잭션 처리
- repository: JPA를 직접 사용하는 계층, 엔티티 매니저 사용
- domain: 엔티티가 모여 있는 계층, 모든 계층에서 사용

> 단순 조회만 하는 경우 controller에서 repository를 바로 호출해도 무방.
> 오히려 불필요하게 service에 처리를 위임할 경우 실용성이 떨어짐.

#### Injection은 Constructor Injection

- Field Injection은 직접 객체를 주입하기 힘들다
- Setter Injection은 변경 위험이 있고, 실제로 애플리케이션 실행 중 바꿀 일이 없다
- 객체 생성 시 의존 관계를 명확하게 알 수 있다

<br>

## 단축키, 설정 관련 꿀팁

> Ctrl + Shift + T - Create Test Case
> Ctrl + Alt + V - Extract Variable
> Ctrl + Alt + N - Inline Variable