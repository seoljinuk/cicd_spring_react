package com.coffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
   이 코드는 Spring Boot + React (SPA) 같이 사용할 때 거의 필수로 들어가는 설정입니다.
   핵심은 “React 라우팅을 Spring이 방해하지 않도록 하는 것”입니다.
*/
@Controller
public class WebController {
    @RequestMapping(value = {"/", "/{path:[^\\.]*}"})
    public String forward() {
        return "forward:/index.html";
    }
}