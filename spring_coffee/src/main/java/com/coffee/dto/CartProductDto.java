package com.coffee.dto;

import lombok.Getter;
import lombok.Setter;

// DTO : Data Transfer Object의 줄인말로 메소드의 매개 변수에 담아서 전송을 하는 데 사용되는 객체라는 의미를 강조하기 위한 용어입니다.
// 장바구니에 담기 위하여 리액트가 넘겨 주는 파라미터의 값을 저장하기 위한 클래스
@Getter
@Setter
public class CartProductDto {
    private Long memberId;  // 회원 아이디
    private Long productId; // 상품 아이디
    private int quantity; // 수량
}
