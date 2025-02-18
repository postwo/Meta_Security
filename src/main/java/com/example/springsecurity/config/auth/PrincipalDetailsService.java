package com.example.springsecurity.config.auth;

import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 1. 발동 = 시큐리티 설정에서 loginProcessingUrl("/login");
// 2. /login 요청이 오면 자동으로 UserDetailsService 타입으로 ioc되어 있는 loadUserByUsername 함수가 실행

@Service //이거는 메모리에 띄워준다
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // String username 을 다르게 하고 싶으면 프론트에서 받아오는 파라미터 변수랑 일치 시켜줘야 한다
    // loginform.html   <input type="text" name="username" placeholder="Username"/> <br/> 여기에 있는 name 부분이랑 일치 해야한다(중요)
    // 만약에 username 말고 다른걸 쓰고 싶으면  securityconfig usernameParameter에 변경 해주면 된다
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByUsername(username); // user가 있는지 조회 없을 경우도 있기 때문에 원래는 orelsethrow를 해줘야 하는데 지금은 연습이여서 사요안함

        if(userEntity != null) {
            // 이게 반환이 되면 시큐리티 session(authentication(UserDetails)) 이 순서대로 내부에 들어가 있다
            // 결론 return 될때 session안에 authentication 만들어지면서 자동으로 UserDetails가 authentication안으로 대입이 된다
            return new PrincipalDetails(userEntity);
        }else {
            return null;
        }

    }

}