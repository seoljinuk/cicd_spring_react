package com.coffee.service;

import com.coffee.constant.Category;
import com.coffee.dto.SearchDto;
import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository ;

//    /*상품 목록 가져 오기*/
//    public List<Product> getProductList() {
//        return this.productRepository.findProductByOrderByIdDesc();
//    }

    public Page<Product> listProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }


    @Value("${productImageLocation}")
    private String productImageLocation ;	

    /*상품 삭제 기능*/
    @Transactional
    public boolean deleteProduct(Long id) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return false;
        }

        String fileName = product.getImage();

        try {
            // DB 먼저 삭제
            productRepository.deleteById(id);

        } catch (Exception e) {
            // DB 실패 → 이미지 건드리지 않음
            throw e; // 트랜잭션 롤백
        }

        // DB 성공 후 이미지 삭제
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(productImageLocation + fileName);

            System.out.println("삭제될 파일 이름");
            System.out.println(file.getAbsolutePath());

            if (file.exists()) {
                boolean deleted = file.delete();

                if (!deleted) {
                    System.out.println("이미지 삭제 실패: " + fileName);
                }
            }
        }

        return true;
    }
    
    /* 상품 등록 기능 */
    // import org.springframework.beans.factory.annotation.Value;
    // 상품 등록하기

    // Base64 인코딩 문자열을 변환하여 이미지로 만들고, 저장해주는 메소드입니다.
    private String saveProductImage(String base64Image) {
        // 데이터 베이스와 이미지 경로에 저장될 이미지의 이름
        // 현재 시각을 '년월일시분' 포맷으로 변환 (예: 202510171430)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedNow = LocalDateTime.now().format(formatter);

        // 데이터 베이스와 이미지 경로에 저장될 이미지의 이름
        String imageFileName = "product_" + formattedNow + ".jpg";

        // String 클래스 공부 : endsWith(), split() 메소드

        File imageFile = new File(productImageLocation  + imageFileName);
        System.out.println("이미지 이름");
        System.out.println(imageFile.getName());

        // base64Image : JavaScript FileReader API에 만들어진 이미지입니다.
        // 메소드 체이닝 : 점을 연속적으로 찍어서 메소드를 계속 호출하는 것
        byte[] decodedImage = Base64.getDecoder().decode(base64Image.split(",")[1]);

        // FileOutputStream는 바이트 파일을 처리해주는 자바의 Stream 클래스
        // 파일 정보를 byte 단위로 변환하여 이미지를 복사합니다.
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(decodedImage);
            return imageFileName;
        } catch (Exception e) {
            throw new IllegalStateException("이미지 파일 저장 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public Product insertProduct(Product product){
        // product는 리액트에서 넘어온 상품 등록을 위한 정보입니다.
        if(product.getImage() == null || !product.getImage().startsWith("data:image")){
            throw new RuntimeException("이미지 정보가 올바르지 않습니다.");
        }

        String imageFileName = null;

        try {
            // 이미지 저장
            imageFileName = this.saveProductImage(product.getImage());

            // 데이터 베이스에는 product_년월일시분초.jpg 형식으로 저장되어야 합니다.
            product.setImage(imageFileName);
            product.setInputdate(LocalDate.now());

            System.out.println("서비스 클래스에서 상품 등록 정보 확인");
            System.out.println(product);

            // DB 저장
            return this.productRepository.save(product);

        } catch (Exception e) {
            // DB 저장 실패 시 이미지 삭제
            if (imageFileName != null) {
                deleteOldImage(imageFileName);
            }

            throw e; // 반드시 다시 던져야 트랜잭션 롤백됨
        }
    }


    /* 상품 수정 기능 */
    // 상품 수정하기 get 방식 시작
    public Product getProductById(Long id) {
        // findById() 메소드는 CrudRepository에 포함되어 있습니다.
        // 그리고, Optional<>을 반환합니다.
        // Optional : 해당 상품이 있을 수도 있지만, 경우에 따라서 없을 수도 있습니다.
        Optional<Product> product = this.productRepository.findById(id);

        // 의미 있는 데이터이면 그냥 넘기고, 그렇지 않으면 null을 반환해 줍니다.
        return product.orElse(null);
    }

    // 상품 수정하기 put 방식 시작
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    // 이전 이미지 파일을 삭제하는 메소드
    private void deleteOldImage(String oldImageFileName) {
        if (oldImageFileName == null || oldImageFileName.isBlank()) {
            return;
        }

        File oldImageFile = new File(productImageLocation + oldImageFileName);

        if (oldImageFile.exists()) {
            boolean deleted = oldImageFile.delete();
            if (!deleted) {
                System.err.println("기존 이미지 삭제 실패 : " + oldImageFileName);
            }
        }
    }

    // Product 수정
    @Transactional // import org.springframework.transaction.annotation.Transactional;
    public Product updateProduct(Product savedProduct, Product updatedProduct) {
        // TransactionSynchronizationManager 라는 항목이 있슴

        String oldImage = savedProduct.getImage();
        String newImageFileName = null;

        if (updatedProduct.getImage() != null && updatedProduct.getImage().startsWith("data:image")) {
            newImageFileName = saveProductImage(updatedProduct.getImage());
            savedProduct.setImage(newImageFileName);
        }

        savedProduct.setName(updatedProduct.getName());
        savedProduct.setPrice(updatedProduct.getPrice());
        savedProduct.setCategory(updatedProduct.getCategory());
        savedProduct.setStock(updatedProduct.getStock());
        savedProduct.setDescription(updatedProduct.getDescription());

        Product result = productRepository.save(savedProduct);

        // DB 저장 성공 후 삭제
        if (newImageFileName != null && oldImage != null) {
            deleteOldImage(oldImage);
        }

        return result;
    }

    public Optional<Product> findProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public Page<Product> listProducts(SearchDto searchDto, int pageNumber, int pageSize){
        // Specification는 엔터티 객체에 대한 쿼리 조건을 정의할 수 있는 조건자(Specification)로 사용됩니다.
        Specification<Product> spec = Specification.unrestricted() ;

        // 기간 검색 콤보 박스의 조건 추가하기
        String searchDateType = searchDto.getSearchDateType() ;

        if(searchDateType != null){
            spec = spec.and(ProductSpecification.hasDateRange(searchDateType));
        }

        // 카테고리의 조건 추가하기
        Category category = searchDto.getCategory() ;

        if(category != null){
            Category categoryEnum = Category.valueOf(category.toString().toUpperCase());
            spec = spec.and(ProductSpecification.hasCategory(categoryEnum));
        }

        // 검색 모드에 따른 조건 추가하기(name 또는 description)
        String searchMode = searchDto.getSearchMode() ;
        String searchKeyword = searchDto.getSearchKeyword() ;

        if(searchMode != null && searchKeyword != null){
            if("name".equals(searchMode)){ // 상품명으로 검색
                spec = spec.and(ProductSpecification.hasNameLike(searchKeyword));

            }else if("description".equals(searchMode)){ // 상품 설명으로 검색
                spec = spec.and(ProductSpecification.hasDescriptionLike(searchKeyword));
            }
        }
        // 상품의 id를 역순으로 정렬하기
        Sort sort = Sort.by(Sort.Order.desc("id")) ;

        // pageNumber 페이지(0 base)를 보여 주시되, sort 방식으로 정렬하여 pageSize 개씩 보여 주세요.
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return this.productRepository.findAll(spec, pageable) ;
    }

    public List<Product> getProductsByFilter(String filter) {
        if(filter != null && !filter.isEmpty()){
            return productRepository.findByImageContaining(filter);
        }
        return productRepository.findAll();
    }


}
