package com.remmy.backend.service;

import com.remmy.backend.pojo.ZkNode;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 * @author yejiaxin
 */
public interface ZkOperationService {
    /**
     * 获取节点信息
     * @param zkId 集群id
     * @param path 节点路径
     * @return 节点信息
     */
    ZkNode getNode(int zkId, String path) throws InterruptedException, IOException, KeeperException;
}
