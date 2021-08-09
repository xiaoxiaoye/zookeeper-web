package com.remmy.backend.service;

import com.remmy.backend.pojo.ZkClusterConfig;
import com.remmy.backend.pojo.ZkClusterStatus;

import java.util.List;

/**
 * @author yejiaxin
 */
public interface ZkClusterService {
    /**
     * 获取zk集群状态
     * @param zkId 集群id
     * @return zk集群状态
     */
    ZkClusterStatus getClusterStatus(int zkId) throws Exception;

    /**
     * 新建zk集群
     * @param config zk集群配置
     */
    void newCluster(ZkClusterConfig config);

    /**
     * 更新zk集群配置
     * @param config zk集群配置
     */
    void updateCluster(ZkClusterConfig config);

    /**
     * 删除zk集群
     * @param zkId 集群id
     */
    void delCluster(int zkId);

    /**
     * 获取集群配置
     * @param zkId 集群id
     * @return zk集群配置
     */
    ZkClusterConfig getZkConfig(int zkId);

    /**
     * 获取zk集群列表
     * @return zk集群列表
     */
    List<ZkClusterConfig> getZkConfigList();
}
