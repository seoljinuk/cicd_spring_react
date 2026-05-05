package com.coffee.test;

/*startsWith(), endsWith(), substring(), length(), toLowerCase()
toUpperCase(), lastIndexOf(), replace(), contains()
        String.format("%02d", count);*/

public class StringTest {
    public static void main(String[] args) {
        String sample = "Bearer hello world" ;

        String result ;
        result = sample.toLowerCase() ;
        System.out.println("소문자 : " + result);

        result = sample.toUpperCase() ;
        System.out.println("대문자 : " + result);

        boolean bool = sample.contains("hello");
        System.out.println("hello 존재 여부 : " + bool);

        bool = sample.startsWith("hello");
        System.out.println("hello로 시작 여부 : " + bool);

        bool = sample.endsWith("world");
        System.out.println("world로 끝이 납니까? : " + bool);

        int size = "Bearer ".length() ;
        System.out.println("문자열 길이 : " + size);

        result = String.format("%03d", size);
        System.out.println("서식 지정 : " + result);

        result = sample.substring(size);
        System.out.println("추출 결과 01 : " + result);

        result = sample.substring(7, 12);
        System.out.println("추출 결과 02 : " + result);

        result = sample.replace("hello", "bluesky");
        System.out.println("치환 : " + result);
    }
}
