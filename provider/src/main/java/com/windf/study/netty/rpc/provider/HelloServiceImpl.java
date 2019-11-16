package com.windf.study.netty.rpc.provider;

import com.windf.study.netty.rpc.api.HelloService;

public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        return "你好：" + name;
    }
}
