package com.coffee.entity;

import com.coffee.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "orders") // 주의) order은 데이터 베이스 전용 키워드입니다.
public class Order { // 주문과 관련된 Entity입니다.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    // 고객 1명이 여러 개의 주문을 할 수 있습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member ;

    // 통상적으로 우리가 주문을 할때, 여러 개의 `주문 상품`을 동시에 주문합니다.
    // 하나의 주문에는 '주문 상품'을 여러개 담을 수 있습니다.
    // 주의) mappedBy 항목의 "order"는 OrderProduct에 들어 있는 Order 타입의 변수명입니다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts ;

    private LocalDate orderdate ; // 주문 날짜

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus ; // 주문 상태
}
