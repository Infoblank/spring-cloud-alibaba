package com.shiro.cloudshirosso.jpa.repositories;

import com.shiro.cloudshirosso.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserInfoRepositories extends JpaRepository<UserInfo, Long>, PagingAndSortingRepository<UserInfo, Long> {

    UserInfo findUserInfoByUserNameAndPassword(String userName, String password);

    //UserInfo updateUserInfoById(Long id);

}
