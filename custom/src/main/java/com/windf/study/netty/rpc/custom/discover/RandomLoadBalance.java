package com.windf.study.netty.rpc.custom.discover;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalanceStrategy {
    @Override
    public ServiceAddress select(List<String> addresses) {
        String addressStr = addresses.get(new Random().nextInt(addresses.size()));
        ServiceAddress serviceAddress = new ServiceAddress();
        String[] ss = addressStr.split(":");
        serviceAddress.setHost(ss[0]);
        serviceAddress.setPort(new Integer(ss[1]));
        return serviceAddress;
    }
}
