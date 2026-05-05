package com.coffee.controller;

import com.coffee.entity.Fruit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController // 컨트롤러는 특정 요청에 대한 처리를 수행해 줍니다.
public class FruitController {
    @GetMapping("/fruit")
    public Fruit test(){
        Fruit bean = new Fruit();
        bean.setId("banana");
        bean.setName("바나나");
        bean.setPrice(1000);
        return bean ;
    }

    @GetMapping("/fruit/list")
    public List<Fruit> test02(){
        List<Fruit> fruitList = new ArrayList<>();
        fruitList.add(new Fruit("apple", "사과", 1000));
        fruitList.add(new Fruit("pear", "나주배", 2000));
        fruitList.add(new Fruit("grape", "포도", 3000));
        return fruitList ;
    }
}
