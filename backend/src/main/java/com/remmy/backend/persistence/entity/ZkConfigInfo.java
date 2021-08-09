package com.remmy.backend.persistence.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yejiaxin
 */
@Getter
@Setter
@Builder
@Entity
@Table(name = "zk_config_info")
@NoArgsConstructor
@AllArgsConstructor
public class ZkConfigInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private String url;

    private String acl;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
