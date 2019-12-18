package com.windf.study.netty.rpc.registry.register;

public interface RegisterCenter {
    /**
     * 注册服务
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName, ServiceAddress serviceAddress);
}
