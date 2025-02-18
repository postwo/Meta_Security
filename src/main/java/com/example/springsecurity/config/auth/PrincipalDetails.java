package com.example.springsecurity.config.auth;


import com.example.springsecurity.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다
// 로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다 (Security ContextHolder 키값에 세션정보를 저장시킨다)
// 이때 세션에 들어갈수 있는 정보는 오브젝트이다 -=> Authentication 타입 개체 여야한다
// Authentication 안에 user정보가 있어야 된다
// 아때 User 오브젝트 타입은 => UserDetails 타입 객체여야 한다

//  Security 가지고 있는 Security 세션 영역이 있다 그곳에 session 정보를 저장한다 그리고 그곳에 들어갈수 있는 객체가 Authentication 객체 여야 한다
// 그리고 Authentication 객체 안의 저장할 user 정보는 UserDetails 타입 이여야 한다

// 지금 메모리에 안띄우고 나중에 new로 메모리에 띄워줄거다
@Data // 이걸 붙여줌으로 get을 통해서 user를 가지고 올 수 있다
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; //콤포지션
    private Map<String,Object> attributes;

    // 일반 로그인 용도
    public PrincipalDetails(User user) { // 생성자
        this.user = user;
    }

    // oauth 로그인 용도
    //authentication 에 principal 정보를 저장하기 위해 attributes를 생성자에 추가한다
    public PrincipalDetails(User user,Map<String,Object> attributes) { // 생성자
        this.user = user;
        this.attributes =attributes;
    }

    // 해당 user의 권한을 리턴하는 곳
    // User 에 있는 권한은 STring 이여서 collection으로 변경
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectt = new ArrayList<>(); // ArrayList는 Collection의 자식이다

        //new GrantedAuthority 를 하면 스트링 타입으로 리턴이가능 하다 람다 적용해도 상관없다
        collectt.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collectt;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 니 계정 만료됐니 true는 아니요 false는 맞아요
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 니 계정 잠겼니
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 니 계정의 비밀번호가 유효기간이 지났니
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 니 계정이 활성화 되어있니
    @Override
    public boolean isEnabled() {
        // ex) 로그인한 시간기준으로 가지고 와서 처리하면 된다
        // 1년 동안 회원이 로그인을 안하면 휴먼걔정으로 처리
        // 현재시간 - 로그인한시간  => 1년을 초과하면 return false로 설정
        return true ;
    }

    // OAuth2User를 구현하면 이렇게 두가지가 추가된다

    @Override
    public Map<String, Object> getAttributes() {
        return attributes; //오브젝트를 리턴
    }

    @Override
    public String getName() {
//        return attributes.get("sub"); // 오브젝트에 있는 sub를 리턴 //그렇게 중요하지 않아서 지금은 null로 체크
        return null;
    }
}
