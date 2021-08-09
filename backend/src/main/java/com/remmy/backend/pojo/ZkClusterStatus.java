package com.remmy.backend.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * zk集群状态
 * </p>
 *
 * @author yejiaxin
 */
@Data
@ToString
public class ZkClusterStatus {
    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 服务地址
     */
    private String url;

    /**
     * 集群状态
     */
    private List<ZkServerStatus> status;
}
