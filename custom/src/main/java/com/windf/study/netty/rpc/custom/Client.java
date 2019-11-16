package com.windf.study.netty.rpc.custom;

import com.windf.study.netty.rpc.api.HelloService;

public class Client {
    public static void main(String[] args) {
        HelloService helloService = BeanFactory.getBean(HelloService.class);
        System.out.println(helloService.sayHello("cyf"));
    }
}
