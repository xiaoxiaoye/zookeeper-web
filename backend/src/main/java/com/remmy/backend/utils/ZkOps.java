package com.remmy.backend.utils;

import com.remmy.backend.pojo.ZkClusterConfig;
import com.remmy.backend.pojo.ZkClusterStatus;
import com.remmy.backend.pojo.ZkNode;
import com.remmy.backend.pojo.ZkServerStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * zookeeper操作类
 * </p>
 *
 * @author yejiaxin
 */
@Component
@Slf4j
public class ZkOps {
    private static final String STAT_CMD = "stat";
    private static final String ROLE_REGEX = "^Mode: ([a-z]+)$";

    private final ZkFactory zkFactory;
    private final Pattern rolePattern;

    public ZkOps(ZkFactory zkFactory) {
        this.zkFactory = zkFactory;
        this.rolePattern = Pattern.compile(ROLE_REGEX);
    }

    public ZkNode getNode(ZkClusterConfig config, String path) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zk = zkFactory.getZookeeper(config);
        ZkNode node = getData(zk, path);
        if (node.getStat().getNumChildren() > 0) {
            List<String> childPaths = zk.getChildren(path, false);
            List<ZkNode> childNodes = new ArrayList<>(childPaths.size());
            for (String childPath : childPaths) {
                childNodes.add(getData(zk, new File(path, childPath).getAbsolutePath()));
            }
            node.setChildrenNodes(childNodes);
        }
        return node;
    }

    private ZkNode getData(ZooKeeper zk, String path) throws InterruptedException, KeeperException {
        Stat stat = new Stat();
        byte[] dataBytes = zk.getData(path, false, stat);
        ZkNode node = new ZkNode();
        node.setPath(path);
        if (dataBytes != null) {
            node.setValue(new String(dataBytes));
        } else {
            node.setValue("");
        }
        node.setStat(stat);
        return node;
    }

    public ZkClusterStatus status(ZkClusterConfig config) throws Exception {

        long start = System.currentTimeMillis();
        String[] addresses = config.getUrl().split(",");
        if (addresses.length == 0) {
            return null;
        }

        ZkClusterStatus zkClusterStatus = new ZkClusterStatus();
        zkClusterStatus.setId(config.getId());
        zkClusterStatus.setName(config.getName());
        zkClusterStatus.setDescription(config.getDescription());
        zkClusterStatus.setUrl(config.getUrl());

        List<ZkServerStatus> statuses = new ArrayList<>();
        for (String address : addresses) {
            statuses.add(getServerStatus(address));
        }
        zkClusterStatus.setStatus(statuses);
        log.info("zkId {} get status, cost:{}ms", config.getId(), System.currentTimeMillis() - start);
        return zkClusterStatus;
    }

    private ZkServerStatus getServerStatus(String address) throws Exception {
        long start = System.currentTimeMillis();
        String[] sp = address.split(":");
        if (sp.length != 2) {
            throw new IllegalArgumentException(address + "is not a valid address");
        }

        ZkServerStatus status = new ZkServerStatus();
        status.setAddress(address);
        String host = sp[0];
        int port = Integer.parseInt(sp[1]);
        try (
                Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            out.println(STAT_CMD);
            String line;
            while ((line = in.readLine()) != null) {
                Matcher matcher = rolePattern.matcher(line);
                if (matcher.find()) {
                    status.setRole(matcher.group(1));
                    status.setHealth(true);
                    break;
                }
            }
        } catch (IOException e) {
            log.error("stat zk[{}] error", address, e);
            status.setRole("无法获取主从关系");
            status.setHealth(false);
        }
        log.info("stat server {}, role:{}, health:{}, cost:{}ms", address, status.getRole(), status.getHealth(), System.currentTimeMillis() - start);
        return status;
    }
}
