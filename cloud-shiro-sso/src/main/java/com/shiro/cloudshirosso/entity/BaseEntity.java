package com.shiro.cloudshirosso.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * MappedSuperclass ：标注不是一个完整的实体类,不会直接映射到数据表,会映射到字表上
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 监听当发生数据库持久花和更新,对创建时间和修改时间更新
public abstract class BaseEntity implements Serializable {

    /**
     * GenerationType.IDENTITY 对应mysql的id自动生成规则
     * MySQL：@GeneratedValue(strategy = GenerationType.AUTO)
     * Sql Server：@GeneratedValue(strategy = GenerationType.IDENTITY)
     * Oracle：@GeneratedValue(strategy = GenerationType.SEQUENCE)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    private Timestamp createTime;

    @LastModifiedDate
    private Timestamp modifyTime;

    @Version
    private long version;

    @CreatedBy
    private Long founder;

    @LastModifiedBy
    private Long lastFounder;
}
