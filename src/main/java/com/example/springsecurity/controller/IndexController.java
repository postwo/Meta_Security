package com.example.springsecurity.controller;

import com.example.springsecurity.config.auth.PrincipalDetails;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //view를 리턴 하겠다
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository; // 지금은 그냥 서비스없이 사용함 연습용이여서

    private final BCryptPasswordEncoder passwordEncoder;

    //AuthenticationPrincipal 이거는 세션정보에 접근할수 있게 할수 있다
    //UserDetails 를 구현한 principalDetails 타입 으로도 받을 수 있다
    // 이거는 일반로그인 유저정보를 가지고 온다 oauth는 x
    @GetMapping("test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) { //Authentication DI(의존성 주입) 한거다
        System.out.println("/test/login ================");

        //1. 이방법은 다운캐스팅을해서 유정 정보를 찾을 수 있다
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //getPrincipal 타입이 오브젝트여서 다운 캐스팅을 해준다
        System.out.println("authentication:"+principalDetails.getUser());

        //2. 이방법은 AuthenticationPrincipal 이라는 어노테이션을 통해서 getuser를 찾을수 있다
        System.out.println("userDetails:"+userDetails.getUser());
        return "세션 정보 확인하기";
    }

    //oauth2 사용자 유저정보 가지고 오기
    @GetMapping("test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication,@AuthenticationPrincipal OAuth2User oauth) { //Authentication DI(의존성 주입) 한거다
        System.out.println("/test/oauth/login ================");

        //1. 이방법은 다운캐스팅을해서 유정 정보를 찾을 수 있다
        //oauth 로 로그인
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal(); //getPrincipal 타입이 오브젝트여서 다운 캐스팅을 해준다
        System.out.println("authentication:"+oAuth2User.getAttributes());

        System.out.println("oAuth2User:"+oauth.getAttributes());

        return "Oauth 세션 정보 확인하기";
    }


    @GetMapping({"","/"})
    public String index() {

        // 머스테치 기본폴더 src/main/resources/

        // 뷰리졸버 설정 :templates(prefix), .mustache (suffix) 생략가능
        return "index"; //src/main.resources/templates/index.mustache
    }

    //oauth 로그인을 해도 principalDetails로 받을 수 있다
    //일반 로그인을 해도 principalDetails로 받을 수 있다
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails: "+principalDetails.getUser());
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
