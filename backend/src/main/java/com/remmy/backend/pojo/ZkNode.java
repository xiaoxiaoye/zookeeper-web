package com.remmy.backend.pojo;

import lombok.Data;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * <p>
 * zk节点
 * </p>
 *
 * @author yejiaxin
 */
@Data
public class ZkNode {
    /**
     * 节点路径
     */
    private String path;

    /**
     * 节点值
     */
    private String value;

    /**
     * 节点元信息
     */
    private Stat stat;

    /**
     * 子节点
     */
    private List<ZkNode> childrenNodes;
}
