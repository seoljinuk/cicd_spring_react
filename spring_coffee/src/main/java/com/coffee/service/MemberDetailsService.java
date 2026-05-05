package com.coffee.service;

import com.coffee.entity.Member;
import com.coffee.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // final 키워드와 연관
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository ;

    @Override // Spring Security가 인증 처리를 시작하면서, loadUserByUsername(email) 메소드를 호출합니다.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 주의) loadUserByUsername 메소드에 들어오는 매개 변수는 로그인시 사용했던 식별자(email)입니다.
        // DB에서 사용자를 이메일로 조회해 봅니다.
        Member member = memberRepository.findByEmail(email) ;

        if(member == null){
            String message = "이메일이 " + email + "인 회원은 존재하지 않습니다." ;
            throw new UsernameNotFoundException(message);
        }else{
            System.out.println(member);
        }

        // User 객체는 Spring Security에서 사용하는 사용자를 의미하는 객체
        return User.builder()
                .username(member.getEmail()) // 로그인시 사용했던 ID(사실은 email)
                .password(member.getPassword()) // 데이터 베이스에 저장된 암호화된 비밀 번호
                .roles(member.getRole().name()) // 사용자의 권한 정보(Role.USER, Role.ADMIN 등등 )
                .build();
    }
}