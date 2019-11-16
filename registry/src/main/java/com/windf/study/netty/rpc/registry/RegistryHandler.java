package com.windf.study.netty.rpc.registry;

import com.windf.study.netty.rpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryHandler extends ChannelInboundHandlerAdapter {
    private static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<String, Object>();

    private List<String> classNames = new ArrayList<String>();

    public RegistryHandler() {
        this.scannerClass("com.windf.study.netty.rpc.provider");

        this.doRegister();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object obj) {
        Object result = null;

        InvokerProtocol invokerProtocol = (InvokerProtocol) obj;

        if (registryMap.containsKey(invokerProtocol.getClassName())) {
            Object object = registryMap.get(invokerProtocol.getClassName());

            Class[] paramClasses = this.getParamClasses(invokerProtocol.getParameters());

            try {
                Method method = object.getClass().getMethod(invokerProtocol.getMethodName(), paramClasses);
                result = method.invoke(object, invokerProtocol.getParameters());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        channelHandlerContext.write(result);
        channelHandlerContext.flush();
        channelHandlerContext.close();
    }

    private Class[] getParamClasses(Object[] parameters) {
        Class[] result = new Class[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Object param = parameters[i];
            result[i] = param.getClass();
        }

        return result;
    }

    private void scannerClass(String basePackage) {
        URL url = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
                scannerClass(basePackage + "." + file.getName());
            }else{
                classNames.add(basePackage + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    private void doRegister() {
        for (String className : classNames) {
            try {
                Class clazz = Class.forName(className);
                Class interfaceClass = clazz.getInterfaces()[0];
                registryMap.put(interfaceClass.getName(), clazz.newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
