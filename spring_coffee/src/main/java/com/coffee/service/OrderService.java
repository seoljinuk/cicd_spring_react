package com.coffee.service;

import com.coffee.constant.OrderStatus;
import com.coffee.constant.Role;
import com.coffee.dto.OrderDetailDto;
import com.coffee.dto.OrderDto;
import com.coffee.dto.OrderProductDto;
import com.coffee.entity.Member;
import com.coffee.entity.Order;
import com.coffee.entity.OrderProduct;
import com.coffee.entity.Product;
import com.coffee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberService memberService;
    private final ProductService productService;
    private final CartProductService cartProductService;
    private final OrderRepository orderRepository;

    /**
     * 주문 생성 로직
     * - 회원 정보 확인
     * - 상품 재고 확인 및 차감
     * - 주문 및 주문 상품 생성
     * - 장바구니 품목 삭제
     */
	@Transactional // import org.springframework.transaction.annotation.Transactional;
    public Order createOrder(OrderDto dto) {
        // 1. 회원 확인
        Optional<Member> optionalMember = memberService.findMemberById(dto.getMemberId());
        if (!optionalMember.isPresent()) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        // 2. 주문 객체 생성
        Order order = new Order();
        order.setMember(member);
        order.setOrderdate(LocalDate.now());
        order.setOrderStatus(dto.getStatus());

        // 3. 주문 상품 생성 및 재고 처리
        List<OrderProduct> orderProductList = new ArrayList<>();
        for (OrderProductDto item : dto.getOrderItems()) {
            Long productId = item.getProductId() ;
            System.out.println("상품 아이디 : " + productId);

            Optional<Product> optionalProduct = productService.findProductById(productId);
			
            if (!optionalProduct.isPresent()) {
                throw new RuntimeException("해당 상품이 존재하지 않습니다.");
            }
            Product product = optionalProduct.get();

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("재고 수량이 부족합니다.");
            }

            // 주문 상품 객체 생성
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(item.getQuantity());
            orderProductList.add(orderProduct);

            // 재고 차감
            product.setStock(product.getStock() - item.getQuantity());

            // 장바구니 품목 삭제
            Long cartProductId = item.getCartProductId();
            if (cartProductId != null) {
                cartProductService.deleteCartProductById(cartProductId);
            } else {
                System.out.println("상품 상세 보기에서 클릭하셨군요.");
            }
        }

        // 4. 주문에 주문 상품 목록 설정
        order.setOrderProducts(orderProductList);

        // 5. DB 저장
        return orderRepository.save(order);
    }

    // 주문 내역 조회 : 관리자(모든 내역), 일반인(본인 것만)
    public List<OrderDetailDto> getOrderListByRole(Long memberId, Role role) {
        List<Order> orders;

        if (role == Role.ADMIN) { // 관리자일 경우 전체 주문 내역 조회
            orders = orderRepository.findByOrderStatusOrderByIdDesc(OrderStatus.PENDING);

        } else { // 일반 사용자일 경우 본인 주문 내역만 조회
            orders = orderRepository.findByMemberIdAndOrderStatusOrderByIdDesc(memberId, OrderStatus.PENDING);
        }

        return convertToOrderDetailDtoList(orders);
    }


    /**
     * 엔티티 목록을 DTO 목록으로 변환하는 공통 메서드
     */
    private List<OrderDetailDto> convertToOrderDetailDtoList(List<Order> orders) {
        List<OrderDetailDto> responseDtos = new ArrayList<>();

        for (Order order : orders) {
            // 주문의 기초 정보 셋팅
            OrderDetailDto dto = new OrderDetailDto();
            dto.setOrderId(order.getId());
            dto.setName(order.getMember().getName()); //
            dto.setOrderDate(order.getOrderdate());
            dto.setStatus(order.getOrderStatus().name());

            // `주문 상품` 여러 개에 대한 셋팅
            List<OrderDetailDto.OrderItem> orderItems = new ArrayList<>();
            for (OrderProduct op : order.getOrderProducts()) {
                OrderDetailDto.OrderItem item =
                        new OrderDetailDto.OrderItem(op.getProduct().getName(), op.getQuantity());
                orderItems.add(item);
            }

            dto.setOrderItems(orderItems);
            responseDtos.add(dto);
        }

        return responseDtos;
    }

    // 관리자가 수행하는 주문된 상품에 대한 `완료` 처리 기능
    @Transactional
    public String updateOrderStatus(Long orderId, OrderStatus newStatus) {

        // 1. 주문 존재 여부 확인
        String message = "해당 주문이 존재하지 않습니다. 주문 Id : " + orderId ;
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(message));

        // 2. 상태 변경 가능 여부 검증 (예: 취소된 주문은 다시 변경 불가)
        if(order.getOrderStatus() == OrderStatus.CANCELED){
            throw new IllegalStateException("취소된 주문은 상태를 변경할 수 없습니다.");
        }

        // 3. 상태 변경
        order.setOrderStatus(newStatus);

        // 4. DB에 반영 (Dirty Checking)
        // JPA에서는 save() 없이도 변경 사항이 자동 반영됨.
        // 단, Modifying 쿼리를 쓰고 싶다면 Repository 메서드 호출 가능.
        // orderRepository.updateOrderStatus(orderId, newStatus);

        // 5. 사용자에게 전달할 메시지 생성
        return "송장 번호 " + orderId + "의 주문 상태가 " + newStatus + "(으)로 변경되었습니다.";
    }

    // 주문된 상품에 대한 `취소` 기능
    @Transactional
    public String cancelOrder(Long orderId) {
        // 1. 주문 존재 여부 확인
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 주문이 존재하지 않습니다. ID: " + orderId);
        }

        Order order = orderOptional.get();

        // 2. `주문 상품`을 반복하면서 재고 수량을 더해 줍니다.(수량 복원)
        for (OrderProduct op : order.getOrderProducts()) {
            Product product = op.getProduct();
            int quantity = op.getQuantity();

            // 기존 재고 + 취소된 수량
            product.setStock(product.getStock() + quantity);

            // 재고 수량 반영
            productService.save(product);
        }

        // 3. 주문 삭제
        orderRepository.deleteById(orderId);

        // 4. 사용자에게 반환할 메시지 생성
        return "주문이 취소되었습니다.";
    }
}
