package com.windf.study.netty.rpc.custom;

import com.windf.study.netty.rpc.api.HelloService;

public class Client {
    public static void main(String[] args) {
        HelloService helloService = BeanFactory.getBean(HelloService.class);

        for (int i = 0; i < 100; i++) {
            System.out.println(helloService.sayHello("cyf"));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
