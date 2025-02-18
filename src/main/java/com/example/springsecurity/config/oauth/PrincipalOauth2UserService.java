package com.example.springsecurity.config.oauth;

import com.example.springsecurity.config.auth.PrincipalDetails;
import com.example.springsecurity.config.oauth.provider.GoogleUserInfo;
import com.example.springsecurity.config.oauth.provider.NaverUserInfo;
import com.example.springsecurity.config.oauth.provider.OAuth2UserInfo;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //*Tip. 구글 로그인이 완료된 후에는 코드를 받는게 아니다, 엑세스토큰 + 사용자프로필정보르 한번에 받아온다* 이 정보들이 OAuth2UserRequest 여기에 리턴되는거다
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userREquest: "+userRequest.getClientRegistration()); //registrationid로 어떤 Oauth로 로그인했는지 알 수 있다
        System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());


        //usernmae 에는 google_sub 이렇게 합쳐서 하면 중복될일이 없다
        //password는 우리 서버만 아는 비밀번호를 암호화해서 넣을거다
        //email은 그대로 가지고 와서 넣는다
        //role = ROLE_USER

        OAuth2User oAuth2User= super.loadUser(userRequest);

        //*중요*
        //구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴(oauth2 라이브러리가 받아준다) -> code를 통해서 accesstoken을 요청한다
        //그정보를 받은게 userRequest 정보 이다  -> userRequest를 통해서 회원프로필을 받아야 한다 그때 받는 함수가 loaduser 함수이다
        //loaduser를 통해서 회원프로필을 받을수 있다 즉 loaduser는 naver나 google로부터 회원프로필을 받아준다는 것이다
        System.out.println("userAttributes: "+oAuth2User.getAttributes()); // oauth 로그인한 사용자 프로필 정보가 들어있다 sub는 id 값이다

        // 이걸 굳이 만들필요는 없다 그냥 연습용이다
        //oauth 로그인 사용자에게서 회원정보를 추출해서 회원가입을 강제로 진행 oauth로 로그인은하면 비밀번호도 필요없고 username도 필요없는데 그냥 만들어 주는거다
        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.

        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청~~");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청~~");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("우리는 구글과 네이버만 지원해요 ㅎㅎ");
        }

        // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
        String provider = oAuth2UserInfo.getProvider();// google
        String providerId = oAuth2UserInfo.getProviderId(); //google 의 id값
        String username = provider+"_"+providerId;// google_10412940128498244921 이렇게 된다 그럼 중복이 될리가 없다
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username); // 회원가입한 유저가 있는지 조회

        // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
        if (userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(userEntity);
        }else{
            System.out.println("구글 로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어있습니다 ");
        }

        return new PrincipalDetails(userEntity,oAuth2User.getAttributes()); // 이게 authentication 객체 안으로 들어가 있는거다
    }
}
