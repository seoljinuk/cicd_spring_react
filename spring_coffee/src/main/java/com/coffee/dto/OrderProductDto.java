package com.coffee.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 주문이 발생할 때 1건의 상품 정보를 저장하고 있는 클래스
// DTO 클래스란 일반적으로 서버와 클라이언트 간 데이터를 전달할 때 사용하는 객체를 의미합니다.
// OrderProductDto는 "주문 1건에 대한 상품 정보(상품 번호, 수량, 카트번호)"를 담는 단순한 데이터 전송 객체(Data Transfer Object)입니다
@Getter
@Setter
@ToString
public class OrderProductDto {
    // 변수 cartProductId는 '카트 목록 보기(CartList.)' 메뉴에서만 사용이 됩니다.
    private Long cartProductId ; // 카트 상품 번호
    private Long productId ; // 상품 번호
    private int quantity ; // 구매 수량
}
