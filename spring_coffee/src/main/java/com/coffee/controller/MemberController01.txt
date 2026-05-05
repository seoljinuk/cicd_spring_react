package com.coffee.controller;

import com.coffee.config.JwtTokenProvider;
import com.coffee.dto.LoginDto;
import com.coffee.entity.Member;
import com.coffee.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService ;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody Member bean, BindingResult bindingResult) {
        // 1) 유효성 검사 결과 확인
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // 2) 이메일 중복 체크
        Member member = memberService.findByEmail(bean.getEmail());
        if (member != null) {
            return new ResponseEntity<>(Map.of("email", "이미 존재하는 이메일 주소입니다."), HttpStatus.BAD_REQUEST);
        }

        // 3) 회원가입 처리
        memberService.insert(bean);
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.OK);
    }

    // 아이디/비밀번호가 맞는지 실제로 검증해주는 인증 엔진
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        // 인증 처리 (아이디/비번 체크)
        // UsernamePasswordAuthenticationToken : 사용자의 로그인 정보(아이디/비밀번호)를 담아서 인증을 요청하고, 인증 결과도 담는 객체
        // 이 코드가 실행되면 내부적으로 loadUserByUsername(email) 자동 호출
        // authenticate 메소드는 '사용자가 보낸 로그인 정보가 맞는지 검증하고, 인증된 사용자 객체를 만들어주는 역할'입니다.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        // 사용자 정보 조회
        Member member = memberService.findByEmail(dto.getEmail());

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "사용자 정보를 찾을 수 없습니다."));
        } else {
            // JWT 토큰 생성 : 인증 성공한 사용자에게 로그인 증명서(토큰) 발급
            String token = jwtTokenProvider.createToken(member);

            // 응답
            return ResponseEntity.ok(Map.of(
                    "accessToken", token,
                    "id", member.getId(),
                    "name", member.getName(),
                    "email", member.getEmail(),
                    "role", member.getRole().toString()
            ));
        }
    }
}
