package com.remmy.backend.service.impl;

import com.remmy.backend.persistence.dao.ZkConfigInfoDao;
import com.remmy.backend.persistence.entity.ZkConfigInfo;
import com.remmy.backend.pojo.ZkClusterConfig;
import com.remmy.backend.service.ZkOperationService;
import com.remmy.backend.pojo.ZkNode;
import com.remmy.backend.utils.ZkOps;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author yejiaxin
 */
@Service
public class ZkOperationServiceImpl implements ZkOperationService {
    private final ZkConfigInfoDao zkConfigInfoDao;
    private final ZkOps zkOps;

    public ZkOperationServiceImpl(ZkConfigInfoDao zkConfigInfoDao, ZkOps zkOps) {
        this.zkConfigInfoDao = zkConfigInfoDao;
        this.zkOps = zkOps;
    }

    @Override
    public ZkNode getNode(int zkId, String path) throws InterruptedException, IOException, KeeperException {
        ZkConfigInfo configInfo = zkConfigInfoDao.getById(zkId);
        ZkClusterConfig config = ZkClusterConfig.builder()
                .id(zkId)
                .name(configInfo.getName())
                .url(configInfo.getUrl())
                .acl(configInfo.getAcl())
                .build();

        return zkOps.getNode(config, path);
    }
}
