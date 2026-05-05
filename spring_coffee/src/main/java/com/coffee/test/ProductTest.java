package com.coffee.test;

import com.coffee.common.GenerateData;
import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductTest {
    @Autowired
    private ProductRepository productRepository ;

    @Test
    @DisplayName("이미지를 이용한 데이터 추가")
    public void createProductMany(){
        // 특정한 폴더 내에 들어 있는 상품 이미지들을 이용하여 상품 테이블에 추가합니다.
        GenerateData gendata = new GenerateData();

        List<String> imageNameList = gendata.getImageFileNames();
        System.out.println("총 이미지 개수 : " + imageNameList.size());

        // 반복문을 사용하여 데이터 베이스에 각각 추가합니다.
        for (int i = 0; i < imageNameList.size(); i++) {
//            System.out.println(imageNameList.get(i));
            Product bean = gendata.createProduct(i, imageNameList.get(i));
            //System.out.println(bean);
            this.productRepository.save(bean);
        }
    }
}
