package com.shiro.cloudshirosso.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name = "sys_role")
@Entity
@Data
public class Role extends BaseEntity {

    private String roleName;


    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_role_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id")
    )
    private List<Permissions> permissions;

}
