package com.windf.study.netty.rpc.registry.register;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class RegisterCenterByZookeeper implements RegisterCenter {
    private String ZK_CONNECTION_STR = "127.0.0.1:2181";
    private static String ZK_REGISTRY_NODE_NAME = "registry";

    CuratorFramework curatorFramework = null;

    {
        // 初始化zookeeper连接
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZK_CONNECTION_STR)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ZK_REGISTRY_NODE_NAME)
                .build();
        // 启动连接
        curatorFramework.start();
    }

    public void register(String serviceName, ServiceAddress serviceAddress) {
        String servicePath = "/" + serviceName;

        // 判断节点是否存在
        try {
            if (curatorFramework.checkExists().forPath(servicePath) == null) {
                // 如果不存在，创建
                curatorFramework.create()
                        .creatingParentsIfNeeded()          // 如果父节点不存在，依次创建父节点
                        .withMode(CreateMode.PERSISTENT)    // 创建持久化节点
                        .forPath(servicePath);
            }

            // 设置服务地址和ip为节点，用:分隔
            String addressPath = servicePath + "/" + serviceAddress.toString();
            curatorFramework.create()
                    .withMode(CreateMode.EPHEMERAL) // 创建临时节点
                    .forPath(addressPath);

            System.out.println("服务注册成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
