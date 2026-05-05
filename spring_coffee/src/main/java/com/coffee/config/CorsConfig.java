package com.coffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean // Spring Security가 이 Bean을 찾아서 CORS 정책으로 사용
    public CorsConfigurationSource corsConfigurationSource() {
        // 요청이 들어왔을 때 어떤 CORS 정책을 적용할지 제공해주는 객체
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용 Origin 주소 프론트 주소 (React 개발 서버)
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173"
        ));

        // 허용 HTTP 메서드
        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // 허용 헤더 : 특히  Authorization는 JWT에서 반드시 필요합니다.
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        // 인증 정보 포함 요청 허용 (JWT 사용 시 중요)
        // 쿠키 / Authorization 헤더 포함 요청 허용
        configuration.setAllowCredentials(true);


        // 모든 URL 요청에 대해 이 configuration(CORS 규칙)을 적용하겠습니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        /*
        source.registerCorsConfiguration("/member/**", memberConfig);
        source.registerCorsConfiguration("/product/**", productConfig);
        source.registerCorsConfiguration("/cart/**", cartConfig);
        * */

        return source;
    }
}