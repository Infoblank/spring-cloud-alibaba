package com.shiro.cloudshirosso.jpa.repositories;

import com.shiro.cloudshirosso.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepositories extends JpaRepository<Permissions,Long> {

}
