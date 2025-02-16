package com.example.springsecurity.repository;

import com.example.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//crud  함수를 jparepository가 들고 있음
//@repository라는 어노테이션이 없어도 ioc.되요 이유는 JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User,Integer> {

    //findBy는 규칙이다 -> Username은 문법 이다
    // select * from user where username = ?(?에는 파라미터로 받아온 username이 들어간다 )
    User findByUsername(String username); //궁금하면 Jpa 쿼리메서드
}
