package com.windf.study.netty.rpc.registry.register;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServiceAddress {
    private String host;
    private int port;

    public ServiceAddress(int port) {
        this.port = port;
        try {
            this.host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public ServiceAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }
}
