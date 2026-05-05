package com.coffee.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Spring Security가 로그인 성공했을 때 자동으로 호출하는 콜백 클래스입니다.
// 로그인 성공 후, 클라이언트(React)에 JSON 데이터를 내려주는 역할을 합니다.
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }
//    private MemberService memberService ;
//
//    @Autowired // setter 메소드를 이용한 의존성 객체 주입(DI)
//    public void setMemberService(MemberService memberService) {
//        this.memberService = memberService;
//    }
//
//    @Autowired
//    JwtTokenProvider jwtTokenProvider ;
//
///*
//# JSON 전송 예시
//{
//  "message": "success",
//  "member": {
//    "id": 1,
//    "name": "홍길동",
//    "email": "test@test.com",
//    "role": "USER"
//  }
//}
//*/
//
//    @Override // 이 메소드는 로그인 성공시 자동 실행이 됩니다.(콜백 메소드)
//    public void onAuthenticationSuccess(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Authentication authentication) throws IOException, ServletException {
//
//        // 클라이언트에 대한 응답을 json 타입으로 지정(UTF-8 타입 인코딩 포함)
//        response.setContentType("application/json;charset=UTF-8");
//
//        // import org.springframework.security.core.userdetails.User;
//        User user = (User)authentication.getPrincipal();
//        String email = user.getUsername() ; // 우리가 사용한 username은 사실 email입니다.
//        Member member = memberService.findByEmail(email) ;
//
//        String token = jwtTokenProvider.createToken(email); //
//
//        Map<String, Object> data = new HashMap<>() ;
//        data.put("accessToken", token);
//        data.put("message", "success"); // 로그인 성공 메시지
//        //data.put("member", member); // Member 객체를 JSON 형식으로 변환
//        data.put("id", member.getId());
//        data.put("name", member.getName());
//        data.put("email", member.getEmail());
//        data.put("role", member.getRole().toString());
//
//        System.out.println("로그인 성공 했으므로 회원 객체 정보를 보도록 합니다.");
//        System.out.println(member);
//
//        // ObjectMapper는 Jackson 라이브러리에 들어 있는 자바 객체를 json 형식으로 변환해주는 클래스입니다.
//        ObjectMapper mapper = new ObjectMapper() ;
//
//        // Java 날짜, 시간 처리 모듈을 등록합니다.
//        mapper.registerModules(new JavaTimeModule()) ;
//
//        // 시간의 TimeStamp 타입 대신 문자열로 변환합니다.
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) ;
//
//        // JSON 문자열을 http의 응답 객체로 전송합니다.
//        response.getWriter().write(mapper.writeValueAsString(data)) ;
//    }
}
