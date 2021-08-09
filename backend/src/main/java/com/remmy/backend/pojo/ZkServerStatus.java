package com.remmy.backend.pojo;

import lombok.*;

/**
 * <p>
 * zookeeper实例状态
 * </p>
 *
 * @author yejiaxin
 */
@Data
@ToString
public class ZkServerStatus {
    /**
     * 地址
     */
    private String address;

    /**
     * 角色 leader or follower
     */
    private String role;

    /**
     * 健康状态
     */
    private Boolean health;
}
