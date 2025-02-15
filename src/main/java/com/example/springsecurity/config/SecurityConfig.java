package com.example.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // IoC 빈(bean)을 등록
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화

                // 여러개의 권한을 넣고 싶으면 hasAnyRole("ROLE1", "ROLE2") 이거를 사용하면 된다
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").authenticated() //인증된 회원만 들어갈 수 있다
                        .requestMatchers("/admin/**").hasRole("ADMIN") //admin 권한이 있는 회원만 접근가능
                        .anyRequest().permitAll() // 위 두가지 주소가 아니면 누구나 접속 가능하다
                )
                .formLogin(login -> login
                        .loginPage("/login") // 위 권한이 없거나 인증된 회원 유저가 아니면 다 로그인페이지로 이동 시켜 버린다
                )
                .build();
    }
}
