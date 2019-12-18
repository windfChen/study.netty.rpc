package com.windf.study.netty.rpc.custom.discover;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryByZookeeper implements DisCovery {

    CuratorFramework curatorFramework = null;

    List<String> serviceResps = new ArrayList<String>();

    {
        // 初始化zookeeper连接
    }

    public ServiceAddress discovery(String serviceName) {
        return null;
    }
}
