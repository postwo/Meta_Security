package com.example.springsecurity.controller;

import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //view를 리턴 하겠다
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository; // 지금은 그냥 서비스없이 사용함 연습용이여서

    private final BCryptPasswordEncoder passwordEncoder;

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
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    //회원가입
    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    //하나만 걸때 사용
    @Secured("ROLE_ADMIN") // 이거는 특정 메서드에 간단하게 권한을 걸어버릴수 있는 어노테이션이다
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }


    //PreAuthorize 이거는 다중으로 권한을 걸고 싶을때 사용
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 이 data라는 메서드가 실행하기 직전에 실행됨 , ROLE_ADMIN 이렇게 작성하면 동작을 안한다 hasRole로 감싸줘야 한다
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
