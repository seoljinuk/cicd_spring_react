package com.coffee.service;

import com.coffee.entity.CartProduct;
import com.coffee.repository.CartProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartProductService {
    private final CartProductRepository cartProductRepository ;

    public void saveCartProduct(CartProduct cp) {
        this.cartProductRepository.save(cp);
    }

    public String editCartProductQuantity(Long cartProductId, Integer quantity){
        // 수량 검증
        if(quantity == null || quantity < 1){
            return "오류 : 장바구니 품목은 최소 1개 이상이어야 합니다.";
        }

        // 해당 카트 상품 찾기
        Optional<CartProduct> cartProductOptional = cartProductRepository.findById(cartProductId);

        if(cartProductOptional.isEmpty()){
            return "오류 : 카트 품목을 찾을 수 없습니다.";
        }

        // 재고 수량 점검 및 수량 변경
        CartProduct cartProduct = cartProductOptional.get();

        int stock = cartProduct.getProduct().getStock() ;
        if(quantity > stock){
            return "오류 : 재고 수량이 부족합니다.";
        }

        cartProduct.setQuantity(quantity);


        // 누적 변경시 다음과 같이 코딩합니다.
        //cartProduct.setQuantity(cartProduct.getQuantity() + quantity);

        // 데이터 베이스에 저장
        cartProductRepository.save(cartProduct);

        // 성공 메시지 반환
        String message = "카트 상품 아이디 " + cartProductId + "번이 '" + quantity + "'개로 수정이 되었습니다.";
        return message ;
    }

    public void deleteCartProductById(Long cartProductId) {
        cartProductRepository.deleteById(cartProductId);
    }

}
