package com.coffee.common;

import com.coffee.constant.Category;
import com.coffee.entity.Product;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public class GenerateData {

    private final String imageFolder = "c:\\shop\\images";

    // 🔥 상품명 중복 카운트용 Map
    private Map<String, Integer> nameCountMap = new HashMap<>();

    // 📌 이미지 파일 목록 가져오기
    public List<String> getImageFileNames() {
        File folder = new File(imageFolder);
        List<String> imageFiles = new ArrayList<>();

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println(imageFolder + " 폴더가 존재하지 않습니다");
            return imageFiles;
        }

        String[] imageExtensions = {".jpg", ".jpeg", ".png"};
        File[] fileList = folder.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && Arrays.stream(imageExtensions)
                        .anyMatch(ext -> file.getName().toLowerCase().endsWith(ext))) {
                    imageFiles.add(file.getName());
                }
            }
        }

        return imageFiles;
    }

    // 📌 상품 생성
    public Product createProduct(int index, String imageName) {
        Product product = new Product();

        // 1️⃣ 확장자 제거
        String fileName = imageName.substring(0, imageName.lastIndexOf(".")).toLowerCase();

        // 2️⃣ _bigsize 제거
        fileName = fileName.replace("_bigsize", "");

        // 3️⃣ 숫자 제거 → 기본 이름 추출
        String baseName = fileName.replaceAll("[0-9]", "");

        // 4️⃣ "_" 제거
        baseName = baseName.replace("_", "");

        // 5️⃣ 한글 상품명 변환
        String koreanName = convertToKoreanName(baseName);

        // 6️⃣ 동일 상품명에 번호 붙이기
        int count = nameCountMap.getOrDefault(koreanName, 0) + 1;
        nameCountMap.put(koreanName, count);

        String productName = koreanName + String.format("%02d", count);

        // 7️⃣ 카테고리 자동 분류
        product.setCategory(getCategoryFromName(baseName));

        // 8️⃣ 상품명
        product.setName(productName);

        // 9️⃣ 설명
        product.setDescription(getRandomDescription(productName));

        // 🔟 이미지
        product.setImage(imageName);

        // 11️⃣ 가격 / 재고
        product.setPrice(1000 * getRandomDataRange(2, 10));
        product.setStock(100 * getRandomDataRange(1, 10));

        // 12️⃣ 날짜
        product.setInputdate(LocalDate.now().minusDays(index));

        return product;
    }

    // 📌 맛 표현 랜덤 리스트
    private String getRandomDescription(String productName) {
        String[] descriptions = {
                productName + "는 깊고 진한 풍미가 일품입니다.",
                productName + "는 부드럽고 달콤한 맛을 자랑합니다.",
                productName + "는 고소하고 풍부한 향이 매력적입니다.",
                productName + "는 신선하고 깔끔한 맛이 특징입니다.",
                productName + "는 입안 가득 퍼지는 진한 맛을 느낄 수 있습니다.",
                productName + "는 달콤하면서도 부드러운 식감이 좋습니다.",
                productName + "는 은은한 향과 깊은 맛이 조화롭습니다.",
                productName + "는 누구나 좋아하는 클래식한 맛입니다."
        };

        return descriptions[new Random().nextInt(descriptions.length)];
    }

    // 📌 카테고리 자동 분류
    private Category getCategoryFromName(String name) {
        name = name.toLowerCase();

        if (name.contains("americano") || name.contains("cappuccino") || name.contains("latte") || name.contains("espresso") || name.contains("milk") || name.contains("juice")) {
            return Category.BEVERAGE;
        } else if (name.contains("cake") || name.contains("chocolate") || name.contains("sponge")) {
            return Category.CAKE;
        } else if (name.contains("bread") || name.contains("croissant") || name.contains("ciabatta") || name.contains("brioche") || name.contains("baguette") || name.contains("scone")) {
            return Category.BREAD;
        } else if (name.contains("wine")) {
            return Category.BEVERAGE;
        } else {
            return Category.BREAD; // 기본값
        }
    }

    // 📌 랜덤 범위 값
    private int getRandomDataRange(int start, int end) {
        return new Random().nextInt(end) + start;
    }

    // 📌 영문 → 한글 상품명 변환
    private String convertToKoreanName(String name) {
        name = name.toLowerCase();

        if (name.contains("americano")) return "아메리카노";
        if (name.contains("cappuccino")) return "카푸치노";
        if (name.contains("latte")) return "바닐라라떼";
        if (name.contains("espresso")) return "에스프레소";
        if (name.contains("milk")) return "우유";
        if (name.contains("juice")) return "주스";

        if (name.contains("croissant")) return "크로아상";
        if (name.contains("ciabatta")) return "치아바타";
        if (name.contains("brioche")) return "브리오슈";
        if (name.contains("baguette")) return "바게트";
        if (name.contains("scone")) return "스콘";

        if (name.contains("cake")) return "케이크";
        if (name.contains("chocolate")) return "초코케이크";
        if (name.contains("sponge")) return "스펀지케이크";

        if (name.contains("macaron")) return "마카롱";

        if (name.contains("wine")) return "와인";

        // 매칭 안될 경우
        return name;
    }
}