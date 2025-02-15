package com.example.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //view를 리턴 하겠다
public class IndexController {

    //localhost:8080
    //localhost:8080
    @GetMapping({"","/"})
    public String index() {

        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정 :templates(prefix), .mustache (suffix) 생략가능
        return "index"; //src/main.resources/templates/index.mustache
    }


    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    //로그인은 지금 아무 설정도 안해서 스프링 시큐리티가 해당주소를 낚아챈다 - securityconfig 파일 생성 후 작동안함
    @GetMapping("/login")
    public @ResponseBody String login() {
        return "login";
    }

    //회원가입
    @GetMapping("/join")
    public @ResponseBody String join() {
        return "join";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc() {
        return "회원가입 완료됨";
    }



}
