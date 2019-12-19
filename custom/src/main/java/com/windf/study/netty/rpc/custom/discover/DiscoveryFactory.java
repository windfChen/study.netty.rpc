package com.windf.study.netty.rpc.custom.discover;

public class DiscoveryFactory {
    private static Discovery discovery = new DiscoveryByZookeeper();
    public static Discovery getBean() {
        return discovery;
    }
}
