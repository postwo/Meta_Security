package com.example.springsecurity.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //*Tip. 구글 로그인이 완료된 후에는 코드를 받는게 아니다, 엑세스토큰 + 사용자프로필정보르 한번에 받아온다* 이 정보들이 OAuth2UserRequest 여기에 리턴되는거다
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userREquest: "+userRequest.getClientRegistration()); //registrationid로 어떤 Oauth로 로그인했는지 알 수 있다
        System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
        //*중요*
        //구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴(oauth2 라이브러리가 받아준다) -> code를 통해서 accesstoken을 요청한다
        //그정보를 받은게 userRequest 정보 이다  -> userRequest를 통해서 회원프로필을 받아야 한다 그때 받는 함수가 loaduser 함수이다
        //loaduser를 통해서 회원프로필을 받을수 있다 즉 loaduser는 naver나 google로부터 회원프로필을 받아준다는 것이다
        System.out.println("userAttributes: "+super.loadUser(userRequest).getAttributes()); // oauth 로그인한 사용자 프로필 정보가 들어있다 sub는 id 값이다

        //usernmae 에는 google_sub 이렇게 합쳐서 하면 중복될일이 없다
        //password는 우리 서버만 아는 비밀번호를 암호화해서 넣을거다
        //email은 그대로 가지고 와서 넣는다
        //role = ROLE_USER

        OAuth2User oAuth2User= super.loadUser(userRequest);
        // 회원가입을 강제로 진행해볼 예정

        return super.loadUser(userRequest);
    }
}
