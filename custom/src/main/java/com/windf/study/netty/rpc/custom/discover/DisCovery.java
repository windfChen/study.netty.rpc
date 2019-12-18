package com.windf.study.netty.rpc.custom.discover;

public interface DisCovery {
    ServiceAddress discovery(String serviceName);
}
