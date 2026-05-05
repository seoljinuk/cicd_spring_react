package com.coffee.config;

import com.coffee.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/*
메소드	역할
createToken	JWT 생성
getEmail	토큰에서 email 추출
validateToken	토큰 유효성 검사
*/

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration; // 만료 1 시간

    private Key signingKey; // 위조 방지를 위한 서명

    @PostConstruct
    protected void init() {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Key getSigningKey() {
        return signingKey;
    }

    // MemberController 클래스에서 인증 성공한 사용자를 위하여 로그인 증명서(토큰)를 발급하는 데 사용될 예정입니다.
    public String createToken(Member member){
        return Jwts.builder() 
                .setSubject(member.getEmail()) // 토큰 주인
                .setIssuedAt(new Date()) // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 토큰 만료 시간
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.claim("role", member.getRole().name()) // 권한 정보
                .compact(); // 최종 문자열 생성하기
    }

    public String getEmail(String token){ // JWT 토큰에서 사용자 정보 가져 오기
        return this.getClaims(token).getSubject() ;
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token){ // JWT 토큰 유효성 검사
        System.out.println("validateToken 메소드");
        System.out.println(token);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) {
            System.out.println("토큰 만료됨");

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("토큰 서명/형식 오류");

        } catch (Exception e) {
            System.out.println("기타 토큰 오류");
        }
        return false ;
    }
}
