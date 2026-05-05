package com.coffee.test;

import com.coffee.constant.Role;
import com.coffee.entity.Member;
import com.coffee.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDate;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class MemberTest {

    private final MemberRepository memberRepository;


    private final PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 몇 명 추가하기")
    void insertMember() {
        // 회원 몇 명을 추가해 봅니다.
        Member mem01 = new Member();
        mem01.setName("관리자");
        mem01.setEmail("admin@naver.com");
        mem01.setPassword(passwordEncoder.encode("Admin@123"));
        mem01.setAddress("마포구 공덕동");
        mem01.setRole(Role.ADMIN);
        mem01.setRegdate(LocalDate.now());

        memberRepository.save(mem01) ; // 데이터 베이스에 인서트
        System.out.println("----------------------------------------");

        Member mem02 = new Member();
        mem02.setName("유영석");
        mem02.setEmail("bluesky@naver.com");
        mem02.setPassword(passwordEncoder.encode("Bluesky@456"));
        mem02.setAddress("용산구 이태원동");
        mem02.setRole(Role.USER);
        mem02.setRegdate(LocalDate.now());
        memberRepository.save(mem02) ;
        System.out.println("----------------------");

        Member mem03 = new Member();
        mem03.setName("곰돌이");
        mem03.setEmail("gomdori@naver.com");
        mem03.setPassword(passwordEncoder.encode("Gomdori@789"));
        mem03.setAddress("동대문구 휘경동");
        mem03.setRole(Role.USER);
        mem03.setRegdate(LocalDate.now());
        memberRepository.save(mem03) ;
        System.out.println("----------------------");
    }
}
