package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable     //Embedded 클래스 쪽에 이 어노테이션을 붙이나, 사용한 클래스 쪽에 @Embedded를 붙이나 선택할 수 있다
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
