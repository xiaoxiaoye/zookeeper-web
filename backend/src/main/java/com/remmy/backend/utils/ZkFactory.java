package com.remmy.backend.utils;

import com.remmy.backend.pojo.ZkClusterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author yejiaxin
 */

@Component
@Slf4j
public class ZkFactory implements Watcher {
    private int sessionTimeout = 1000*60*10;
    private static final String ZK_SESSION_KEY = "zookeeper_session_client";
    private final Object lock = new Object();
    private volatile boolean connected = true;

    private HttpSession getSession() {
        ServletRequestAttributes ra =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ra != null) {
            HttpServletRequest request = ra.getRequest();
            return request.getSession();
        }
        return null;
    }

    private ZooKeeper getZkFromSession(int zkId) {
        HttpSession session = getSession();
        if (session != null) {
            String sessionKey = ZK_SESSION_KEY + ":" + zkId;
            return (ZooKeeper) session.getAttribute(sessionKey);
        }
        return null;
    }

    private void setZkToSession(int zkId, ZooKeeper zk) {
        HttpSession session = getSession();
        if (session != null) {
            String sessionKey = ZK_SESSION_KEY + ":" + zkId;
            session.setAttribute(sessionKey, zk);
        }
    }

    public ZooKeeper getZookeeper(ZkClusterConfig config) throws IOException,InterruptedException {
        ZooKeeper zk = getZkFromSession(config.getId());
        if (zk == null || !zk.getState().isConnected() || !zk.getState().isAlive()) {
            synchronized (lock) {
                zk = new ZooKeeper(config.getUrl(), sessionTimeout, this);
                if (!zk.getState().isConnected()) {
                    log.info("waiting for connected");
                    connected = false;
                    lock.wait(2000);
                }
                connected = true;
                setZkToSession(config.getId(), zk);
            }
        }
        return zk;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.None) {
            if (event.getState() == Event.KeeperState.SyncConnected) {
                if (!connected) {
                    synchronized (lock) {
                        log.info("notify state to connected");
                        lock.notifyAll();
                    }
                }
            }
        }
    }
}
