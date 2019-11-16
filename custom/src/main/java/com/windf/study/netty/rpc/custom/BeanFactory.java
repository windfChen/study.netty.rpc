package com.windf.study.netty.rpc.custom;

import com.windf.study.netty.rpc.protocol.InvokerProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BeanFactory {
    public static <T> T getBean(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new RpcProxy());
    }

    private static class RpcProxy implements InvocationHandler {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            InvokerProtocol invokerProtocol = new InvokerProtocol();
            invokerProtocol.setClassName(method.getDeclaringClass().getName());
            invokerProtocol.setParameters(args);
            invokerProtocol.setMethodName(method.getName());

            RpcInvoker rpcInvoker = new RpcInvoker(invokerProtocol);
            return rpcInvoker.invoker();
        }
    }
}
