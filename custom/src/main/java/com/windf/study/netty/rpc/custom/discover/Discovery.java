package com.windf.study.netty.rpc.custom.discover;

public interface Discovery {
    ServiceAddress discovery(String serviceName);
}
