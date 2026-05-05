package com.coffee.test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
    public static void main(String[] args) {
        Map<String, String> errors = new HashMap<>();

        errors.put("password", "비밀 번호 누락");
        errors.put("email", "이메일 잘못 들어옴");

        System.out.println(errors);

        Map<String, String> colors
                = Map.of("red", "빨강", "blue", "파랑", "yellow", "노랑");
        System.out.println(colors);
    }
}
