package com.coffee.service;

import com.coffee.constant.Role;
import com.coffee.entity.Member;
import com.coffee.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service // 서비스 역할을 하며, 주로 로직 처리에 활용되는 자바 클래스입니다.
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void insert(Member bean) {
        // 사용자 '역할'과 '등록 일자'는 요기서 넣어 줍시다.
        bean.setRole(Role.USER);
        bean.setRegdate(LocalDate.now());

        // 비밀 번호 암호화
        String encodedPassword = passwordEncoder.encode(bean.getPassword());
        bean.setPassword(encodedPassword);

        // 주의) Repository에서 인서트 작업은 save() 메소드를 사용합니다.
        memberRepository.save(bean);
    }
}
