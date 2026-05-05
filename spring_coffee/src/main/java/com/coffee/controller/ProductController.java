package com.coffee.controller;

import com.coffee.constant.Category;
import com.coffee.dto.SearchDto;
import com.coffee.entity.Product;
import com.coffee.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService ;

//    @GetMapping("/list") // 상품 목록을 List 컬렉션으로 반환해 줍니다.
//    public List<Product> list(){
//        List<Product> products = this.productService.getProductList() ;
//
//        return products ;
//    }

//    @GetMapping("/list") // 페이징 관련 파라미터를 사용하여 상품 목록을 조회합니다.
//    public ResponseEntity<Page<Product>> listProducts(
//            @RequestParam(defaultValue = "0") int pageNumber,
//            @RequestParam(defaultValue = "6") int pageSize
//    ){
//        System.out.println("pageNumber : " + pageNumber + ", pageSize : " + pageSize );
//
//        // 현재 페이지는 pageNumber이고, 페이지당 보여줄 갯수 pageSize를 사용하여 Pageable 페이지를 구합니다.
//        // 상품 번호가 큰 것부터 정렬합니다.
//        Sort mysort = Sort.by(Sort.Direction.DESC, "id");
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, mysort);
//
//        Page<Product> productPage = productService.listProducts(pageable) ;
//
//        return ResponseEntity.ok(productPage) ;
//    }


    // 클라이언트가 특정 상품 id에 대하여 "삭제" 요청을 하였습니다.
    // @PathVariable는 URL의 경로 변수를 메소드의 매개 변수로 값을 전달해 줍니다.
    @DeleteMapping("/delete/{id}") // {id}는 경로 변수라고 하며, 가변 매개 변수로 이해하면 됩니다.
    public ResponseEntity<String> delete(@PathVariable Long id){ // {id}으로 넘겨온 상품의 아이디가, 변수 id에 할당됩니다.
        try{
            boolean isDeleted = this.productService.deleteProduct(id);

            if(isDeleted){
                return ResponseEntity.ok(id + "번 상품이 삭제 되었습니다.");
            }else{
                return ResponseEntity.badRequest().body(id + "번 상품이 존재하지 않습니다.");
            }
        }catch (DataIntegrityViolationException err){
            String message = "해당 상품은 장바구니에 포함되어 있거나, 이미 매출이 발생한 상품입니다.\n확인해 주세요.";
            return ResponseEntity.badRequest().body(message);

        }catch (Exception err){
            return ResponseEntity.internalServerError().body("오류 발생 : " + err.getMessage());
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody Product product, BindingResult bindingResult) {
        // 1. 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError xx : bindingResult.getFieldErrors()) {
                errors.put(xx.getField(), xx.getDefaultMessage());
            }

            // 400 Bad Request + 에러 메시지
            return new ResponseEntity<>(
                    Map.of(
                            "message", "상품 등록 유효성 검사에 문제가 있습니다.",
                            "errors", errors
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        // 2. 상품 등록 시도
        try {
            Product savedProduct = this.productService.insertProduct(product);

            if(savedProduct == null){
                return ResponseEntity
                        .status(500)
                        .body(
                                Map.of(
                                        "message", "상품 등록에 실패하였습니다.",
                                        "error", "bad image file format"
                                )
                        );
            }

            return ResponseEntity.ok(
                    Map.of(
                            "message", "상품이 성공적으로 등록되었습니다.",
                            "image", savedProduct.getImage()
                    )
            );

        } catch (IllegalStateException err) { // 경로 또는 이미지 저장 문제
            return ResponseEntity
                    .status(500)
                    .body(
                            Map.of(
                                    "message", err.getMessage(),
                                    "error", "File Save Error"
                            )
                    );

        } catch (Exception err) { // 데이터 베이스 오류
            return ResponseEntity
                    .status(500)
                    .body(
                            Map.of(
                                    "message", err.getMessage(),
                                    "error", "Internal Server Error"
                            )
                    );
        }
    }

    // 상품 수정 페이지 get 방식
    // 프론트 앤드의 상품 수정 페이지에서 요청이 들어 왔습니다.
    @GetMapping("/update/{id}") // 상품의 id 정보를 이용하여 해당 상품 Bean 객체를 반환해 줍니다.
    public ResponseEntity<Product> getUpdate(@PathVariable Long id){
        System.out.println("수정할 상품 번호 : " + id);

        Product product = this.productService.getProductById(id) ;

        if(product == null){ // 상품이 없으면 404 응답과 함께 null을 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }else{ // 해당 상품의 정보와 함께, 성공(200) 메시지를 반환합니다.
            return ResponseEntity.ok(product);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> putUpdate(@PathVariable Long id,
                                       @Valid @RequestBody Product updatedProduct,
                                       BindingResult bindingResult) {
        // 1. 유효성 검사
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(
                    Map.of(
                            "message", "상품 수정 유효성 검사에 문제가 있습니다.",
                            "errors", errors
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        // 2. 상품 조회
        Optional<Product> findProduct = productService.findById(id);

        if (findProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Product savedProduct = findProduct.get();
            productService.updateProduct(savedProduct, updatedProduct);

            return ResponseEntity.ok(Map.of("message", "상품 수정 성공"));

        } catch (Exception err) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", err.getMessage(),
                            "error", "상품 수정 실패"
                    ));
        }
    }

    @GetMapping("/detail/{id}") // 프론트 엔드가 상품에 대한 상세 정보를 요청하였습니다.
    public ResponseEntity<Product> detail(@PathVariable Long id){
        Product product = this.productService.getProductById(id) ;

        if(product == null){ // 404 응답
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build() ;

        }else{ // 200 ok 응답
            return ResponseEntity.ok(product) ;
        }
    }

    // 필드 검색 조건과 페이징 관련 파라미터를 사용하여 상품 목록을 조회합니다.
    @GetMapping("/list")
    public ResponseEntity<Page<Product>> listProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "6") int pageSize,
            @RequestParam(defaultValue = "all") String searchDateType,
            @RequestParam(defaultValue = "") Category category,
            @RequestParam(defaultValue = "") String searchMode ,
            @RequestParam(defaultValue = "") String searchKeyword
    ){
        SearchDto searchDto = new SearchDto(searchDateType, category, searchMode, searchKeyword);

        Page<Product> products = productService.listProducts(searchDto, pageNumber, pageSize) ;

        System.out.println("검색 조건 : " + searchDto);
        System.out.println("총 상품 개수 : " + products.getTotalElements());
        System.out.println("총 페이지 번호 : " + products.getTotalPages());
        System.out.println("현재 페이지 번호 : " + products.getNumber());

        // Http 응답 코드 200과 함께 상품 정보를 json 형태로 반환해 줍니다.
        return ResponseEntity.ok(products) ;
    }

    @GetMapping("") // 홈 페이지에 보여줄 큰 이미지들에 대한 정보를 읽어 옵니다.
    public List<Product> getBigsizeProducts(@RequestParam(required = false) String filter){
        return productService.getProductsByFilter(filter) ;
    }

}
