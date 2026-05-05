package com.coffee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

// 리액트가 주문 내역을 조회할 때 사용하는 주문 1개를 의미하는 자바 클래스
@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 자동으로 포함합니다.
@NoArgsConstructor // 매개 변수가 없는 기본 생성자를 자동 생성합니다.
@AllArgsConstructor // 모든 필드를 매개 변수로 받는 생성자를 자동 생성합니다.
public class OrderDetailDto {
    private Long orderId ; // 송장 번호(주문의 고유 번호_PK 기반)
    private String name; // 사용자 이름
    private LocalDate orderDate ; // 주문 날짜
    private String status ; // 주문의 상태

    // 주문에 속해 있는 상품 목록을 의미하며, 하단의 OrderItem에 대한 컬렉션입니다.
    private List<OrderItem> orderItems ;

    // OrderProduct 엔터티와 이름이 동일하여 OrderItem으로 명명했습니다
    // OrderItem 클래스는 `주문 상품` 1개를 의미합니다.
    @Data
    @AllArgsConstructor // 만약 상품에 대한 추가 정보가 더 필요하면 하단에 변수를 추가하세요.
    public static class OrderItem{ // 내부 정적 클래스 정의
        private String productName ; // 상품 이름
        private int quantity ; // 주문 수량
    }
}
