package com.coffee.dto;


import com.coffee.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

// 사용자가 주문할 때 필요한 변수들을 정의해 놓은 클래스(주문 1건의 최소 단위)
@Getter
@Setter
@ToString
public class OrderDto {
    private Long memberId ; // 주문자 정보
    private OrderStatus status ;
    private List<OrderProductDto> orderItems ; // 주문 상품 목록
}
