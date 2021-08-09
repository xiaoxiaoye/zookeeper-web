package com.remmy.backend.controller;

import com.remmy.backend.common.RespBean;
import com.remmy.backend.pojo.ZkClusterConfig;
import com.remmy.backend.pojo.ZkClusterStatus;
import com.remmy.backend.pojo.ZkNode;
import com.remmy.backend.service.ZkClusterService;
import com.remmy.backend.service.ZkOperationService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yejiaxin
 */
@RestController
public class ZkServiceController {
    private final ZkClusterService clusterService;
    private final ZkOperationService zkOperationService;

    public ZkServiceController(ZkClusterService clusterService, ZkOperationService zkOperationService) {
        this.clusterService = clusterService;
        this.zkOperationService = zkOperationService;
    }

    @GetMapping("/zk/configs")
    public RespBean listZkConfig() {
        List<ZkClusterConfig> zkList = clusterService.getZkConfigList();
        return RespBean.markSuccess("", zkList);
    }

    @PostMapping("/zk/configs")
    public RespBean addZkConfig(@RequestBody ZkClusterConfig config) {
        clusterService.newCluster(config);
        return RespBean.markSuccess("add success");
    }

    @DeleteMapping("/zk/configs")
    public RespBean delZkConfig(@Param("zkId") Integer zkId) {
        clusterService.delCluster(zkId);
        return RespBean.markSuccess("del success");
    }

    @PutMapping("/zk/configs")
    public RespBean updateZkConfig(@RequestBody ZkClusterConfig config) {
        clusterService.updateCluster(config);
        return RespBean.markSuccess("update success");
    }

    @GetMapping("/zk/node")
    public RespBean getNode(@RequestParam Integer zkId, @RequestParam String path) throws Exception {
        ZkNode node = zkOperationService.getNode(zkId, path);
        return RespBean.markSuccess("", node);
    }

    @GetMapping("/zk/status")
    public RespBean clusterStatus(@RequestParam Integer zkId) throws Exception {
        ZkClusterStatus status = clusterService.getClusterStatus(zkId);
        return RespBean.markSuccess("", status);
    }
}
