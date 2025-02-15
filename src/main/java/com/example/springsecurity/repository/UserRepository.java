package com.example.springsecurity.repository;

import com.example.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//crud  함수를 jparepository가 들고 있음
//@repository라는 어노테이션이 없어도 ioc.되요 이유는 JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User,Integer> {

}
