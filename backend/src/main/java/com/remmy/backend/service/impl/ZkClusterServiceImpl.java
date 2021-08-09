package com.remmy.backend.service.impl;

import com.remmy.backend.persistence.dao.ZkConfigInfoDao;
import com.remmy.backend.persistence.entity.ZkConfigInfo;
import com.remmy.backend.pojo.ZkClusterConfig;
import com.remmy.backend.service.ZkClusterService;
import com.remmy.backend.pojo.ZkClusterStatus;
import com.remmy.backend.utils.ZkOps;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yejiaxin
 */
@Service
public class ZkClusterServiceImpl implements ZkClusterService {
    private final ZkConfigInfoDao zkConfigInfoDao;
    private final ZkOps zkOps;

    public ZkClusterServiceImpl(ZkConfigInfoDao zkConfigInfoDao, ZkOps zkOps) {
        this.zkConfigInfoDao = zkConfigInfoDao;
        this.zkOps = zkOps;
    }

    @Override
    public ZkClusterStatus getClusterStatus(int zkId) throws Exception {
        return zkOps.status(getZkConfig(zkId));
    }

    @Override
    public void newCluster(ZkClusterConfig config) {
        ZkConfigInfo configInfo = convert(config);
        configInfo.setCreateTime(new Date());
        configInfo.setUpdateTime(new Date());
        zkConfigInfoDao.save(configInfo);
    }

    @Override
    public void updateCluster(ZkClusterConfig config) {
        Optional<ZkConfigInfo> configOption = zkConfigInfoDao.findById(config.getId());
        if (!configOption.isPresent()) {
            return;
        }

        ZkConfigInfo configInfo = convert(config);
        configInfo.setUpdateTime(new Date());
        zkConfigInfoDao.save(configInfo);
    }

    @Override
    public void delCluster(int zkId) {
        Optional<ZkConfigInfo> configOptional = zkConfigInfoDao.findById(zkId);
        configOptional.ifPresent(zkConfigInfoDao::delete);
    }

    @Override
    public ZkClusterConfig getZkConfig(int zkId) {
        Optional<ZkConfigInfo> configOptional = zkConfigInfoDao.findById(zkId);
        return configOptional.map(this::convert).orElse(null);

    }

    @Override
    public List<ZkClusterConfig> getZkConfigList() {
        List<ZkConfigInfo> configInfoList = zkConfigInfoDao.findAll();
        return configInfoList.stream().map(this::convert).collect(Collectors.toList());
    }


    private ZkClusterConfig convert(ZkConfigInfo configInfo) {
        return ZkClusterConfig.builder()
                .id(configInfo.getId())
                .name(configInfo.getName())
                .description(configInfo.getDescription())
                .url(configInfo.getUrl())
                .acl(configInfo.getAcl())
                .build();
    }

    private ZkConfigInfo convert(ZkClusterConfig clusterConfig) {
        return ZkConfigInfo.builder()
                .id(clusterConfig.getId())
                .name(clusterConfig.getName())
                .description(clusterConfig.getDescription())
                .url(clusterConfig.getUrl())
                .acl(clusterConfig.getAcl())
                .build();
    }
}
