package com.windf.study.netty.rpc.custom;

import com.windf.study.netty.rpc.protocol.InvokerProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class RpcInvoker {

    private InvokerProtocol invokerProtocol;

    public RpcInvoker(InvokerProtocol invokerProtocol) {
        this.invokerProtocol = invokerProtocol;
    }

    public Object invoker() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        final CustomHandler customHandler = new CustomHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline channelPipeline = socketChannel.pipeline();

                        channelPipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        channelPipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                        channelPipeline.addLast("encoder", new ObjectEncoder());
                        channelPipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                        channelPipeline.addLast("handler", customHandler);
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6789).sync();
            channelFuture.channel().writeAndFlush(this.invokerProtocol).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }

        return customHandler.getResponse();
    }
}
