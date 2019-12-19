package com.windf.study.netty.rpc.custom.discover;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryByZookeeper implements Discovery {
    private String ZK_CONNECTION_STR = "127.0.0.1:2181";
    private static String ZK_REGISTRY_NODE_NAME = "registry";

    CuratorFramework curatorFramework;

    List<String> serviceAddresses = new ArrayList<>();

    {
        // 初始化zookeeper连接
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZK_CONNECTION_STR)   // 连接地址
                .sessionTimeoutMs(5000) // 设置超时时间
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ZK_REGISTRY_NODE_NAME)
                .build();
        curatorFramework.start();
    }

    @Override
    public ServiceAddress discovery(String serviceName) {
        // 查找地址
        String path = "/" + serviceName;
        if (serviceAddresses.isEmpty()) {
            try {
                serviceAddresses = curatorFramework.getChildren().forPath(path);    // 获取节点数
                registryWatch(path);         // 监控节点，节点发生变化的时候，修改
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 负载均衡获取地址
        LoadBalanceStrategy loadBalanceStrategy = new RandomLoadBalance();
        return loadBalanceStrategy.select(serviceAddresses);
    }

    private void registryWatch(final String path) {
        PathChildrenCache nodeCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener nodeCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            System.out.println("客户端接收到节点变更事件");
            serviceAddresses = curatorFramework.getChildren().forPath(path);// 更新缓存
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        try {
            nodeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
