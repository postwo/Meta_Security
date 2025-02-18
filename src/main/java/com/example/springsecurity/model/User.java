package com.example.springsecurity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN

    //oauth 회원가입 유저 구분하기 위해 추가
    private String provider; // 어떤 oauth로 로그인했는지 구분 용도 = naver,kakao
    private String providerId; // 구글 에서 사용하고 있는 아이디 값 추가 == sub

    @CreationTimestamp
    private Timestamp createDate;
}