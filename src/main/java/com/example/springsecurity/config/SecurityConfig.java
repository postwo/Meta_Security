package com.example.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // IoC 빈(bean)을 등록

//EnableMethodSecurity 결론적으로 이거는 내가 지금 하는 프로젝트에서 거의 사용하지 않음, 하지만 하나씩 권한을 걸고 싶을때는 이방식 사용 그게 아니면  SecurityFilterChain 울 통해 글로벌로 걸면 된다
//@EnableMethodSecurity는 최신 Spring Security에서 @EnableGlobalMethodSecurity를 대체할 수 있습니다. 두 애노테이션 모두 메서드 수준의 보안을 활성화하는 기능을 제공하지만, @EnableMethodSecurity는 최신 버전에서 권장되는 방식
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화 // 이거는 특정 메서드에 간단하게 권한을 걸어버릴수 있는 어노테이션이다 ,prePostEnabled = preAuthorize,postAuthorize 어노테이션 활성화 = 이거는 권한을 여러개 주어지고 싶을때 사용
public class SecurityConfig {

    // Bean = 해당 메서드의 리턴되는 오브젝트를 ioc로 등록해준다
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화

                // 여러개의 권한을 넣고 싶으면 hasAnyRole("ROLE1", "ROLE2") 이거를 사용하면 된다
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").authenticated() //인증된 회원만 들어갈 수 있다
                        .requestMatchers("/admin/**").hasRole("ADMIN") //admin 권한이 있는 회원만 접근가능
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN")
                        .anyRequest().permitAll() // 위 두가지 주소가 아니면 누구나 접속 가능하다
                )
                .formLogin(login -> login
                        .loginPage("/loginForm") // 위 권한이 없거나 인증된 회원 유저가 아니면 다 로그인페이지로 이동 시켜 버린다 // 내가 커스텀한 로그인 페이지로 이동
                        // .usernameParameter("name")  // loginform.html   <input type="text" name="username" placeholder="Username"/>  여기에 있는 username 말고 만약에 name으로 했으면 여기서 변형시켜주면 된다
                        .loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다
                        .defaultSuccessUrl("/") // 1. 로그인 성공시 메인페이지로 이동
                        // 2. 니가 loginForm으로 와서 로그인을 하면 디폴트페이지로 이동시켜 줄건데 근데 니가 어느 특정페이지로 접속할려고 로그인을 하면 그페이지로 보내줄게
                        // 3. ex) http://localhost:8080/user 이렇게 페이지로 접속했는데 로그인페이지가 리턴되고 거기서 로그인을 하면 user 페이지로 다시 이동시켜줄게 라는 뜻이다
                )
                .build();
    }
}
