package com.lloop.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Author lloop
 * @Create 2025/2/6 18:25
 */
public class HelloWorldServer {

    public static void main(String[] args) {

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(new NioEventLoopGroup())                                    // 1. 指定一组工人
                    .channel(NioServerSocketChannel.class)                                    // 2. 指定物流方式
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {                // 3. 指定流水线
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(new StringDecoder());                       // 5. 指定工序1
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() { // 6. 指定工序2
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                    System.out.println("接收到客户端消息:" + msg);
                                }
                            });
                        }
                    });
            Channel channel = serverBootstrap.bind(8080).sync().channel();       // 4. 指定监听端口
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
