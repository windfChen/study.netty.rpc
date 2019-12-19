package com.windf.study.netty.rpc.custom.discover;

import java.util.List;

public interface LoadBalanceStrategy {
    ServiceAddress select(List<String> addresses);
}
