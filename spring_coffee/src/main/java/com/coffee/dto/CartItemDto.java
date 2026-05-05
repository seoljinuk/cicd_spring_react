package com.coffee.dto;

import com.coffee.entity.CartProduct;
import lombok.Getter;
import lombok.Setter;

// 리액트의 CartList.tsx 파일에서 fetchCartProducts() 함수 참조
// `장바구니` 목록 페이지에서 데이터 1개를 의미하는 자바 DTO 클래스
@Getter
@Setter
public class CartItemDto {
    private Long cartProductId; // 카트 상품의 id로써, `수량의 변경`이나 `삭제`시 반드시 사용됩니다.
    private Long productId; // 상품의 id
    private String name ; // 상품 이름
    private String image ; // 이미지
    private int price ; // 단가
    private int stock ; // 재고
    private int quantity ; // 구매 또는 장바구니에 담을 수량
    private boolean checked = false ; // 카트 상품 목록에서 체크 여부

    public CartItemDto(CartProduct cartProduct) {
        this.cartProductId = cartProduct.getId();
        this.productId = cartProduct.getProduct().getId() ;
        this.name = cartProduct.getProduct().getName() ;
        this.image = cartProduct.getProduct().getImage() ;
        this.price = cartProduct.getProduct().getPrice() ;
        this.stock = cartProduct.getProduct().getStock() ;
        this.quantity = cartProduct.getQuantity() ;
    }
}
