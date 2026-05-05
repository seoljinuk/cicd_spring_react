package com.coffee.controller;

import com.coffee.constant.OrderStatus;
import com.coffee.constant.Role;
import com.coffee.dto.OrderDetailDto;
import com.coffee.dto.OrderDto;
import com.coffee.entity.Order;
import com.coffee.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /* 리액트에서 '주문하기' 버튼 클릭 시 호출되는 엔드포인트 */
    @PostMapping("")
    public ResponseEntity<String> order(@RequestBody OrderDto dto) {
        System.out.println("주문 요청 DTO: " + dto);

        // 핵심 로직은 서비스로 위임
        Order savedOrder = orderService.createOrder(dto);

        String message = "송장 번호 " + savedOrder.getId() + "에 대한 주문이 완료 되었습니다.";
        return ResponseEntity.ok(message);
    }
    // 특정한 회원의 주문 정보를 최신 날짜 순으로 조회합니다.
    // http://localhost:9000/order/list?memberId=회원아이디&role=USER
    @GetMapping("/list") // 리액트의 OrderList.js 파일 내의 useEffect 참조
    public ResponseEntity<List<OrderDetailDto>> getOrderList(@RequestParam Long memberId, @RequestParam Role role) {
        System.out.println("로그인 한 사람의 id : " + memberId);
        System.out.println("로그인 한 사람 역할 : " + role);

        List<OrderDetailDto> responseDtos = orderService.getOrderListByRole(memberId, role);

        System.out.println("주문 건수 : " + responseDtos.size());
        return ResponseEntity.ok(responseDtos);
    }

    // 관리자가 수행하는 주문된 상품에 대한 `완료` 처리 기능
    @PutMapping("/update/{orderId}")
    public ResponseEntity<String> statusChange(@PathVariable Long orderId, @RequestParam OrderStatus status){
        System.out.println("수정할 항목의 아이디 : " + orderId);
        System.out.println("변경하고자 하는 주문 상태 : " + status);

        // 서비스 로직에서 모든 처리를 맡김
        String message = orderService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok(message);
    }

    // `관리자` 또는 `당사자`가 주문에 대한 삭제 요청을 하였습니다.
    // 주문된 상품에 대한 `취소` 기능
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {

        try {
            // 서비스에 모든 비즈니스 로직을 위임
            String message = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
