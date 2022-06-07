package com.shiro.cloudshirosso.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name = "sys_user_info")
@Entity
@Data
public class UserInfo extends BaseEntity {

    /**
     * 用户名,登录名称
     */
    private String userName;
    /**
     * 用户姓名
     */
    private String name;

    private String salt;

    /**
     * 密码
     */
    private String password;

    private String email;

    private String phone;

    // 删除用户时不能删除角色,但是需要删除中间表的关联关系
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    // name 自己表名称,user_id,role_id两个外键的名称,referencedColumnName实际关联的主表字段
    @JoinTable(name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

}
