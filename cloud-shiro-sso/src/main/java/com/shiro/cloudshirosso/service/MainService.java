package com.shiro.cloudshirosso.service;

import com.shiro.cloudshirosso.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MainService {

    private JpaRepository jpaRepository;

    @Autowired
    public void setUserInfoRepositories(JpaRepository<UserInfo,Long> jpaRepository){
        this.jpaRepository = jpaRepository;
    }

    public List<UserInfo> findAllUserInfo(){
        return jpaRepository.findAll();
    }
}
