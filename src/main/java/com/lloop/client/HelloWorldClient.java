package com.lloop.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

/**
 * @Author lloop
 * @Create 2025/2/6 18:25
 */
public class HelloWorldClient {


    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                .group(new NioEventLoopGroup())                        // 1. 指定一组工人
                .channel(NioSocketChannel.class)                       // 2. 指定物流方式
                .handler(new ChannelInitializer<Channel>() {           // 3. 指定流水线
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());    // 8. 指定工序
                    }
                })
                .connect("127.0.0.1", 8080)                            // 4. 指定收货地址（连接的服务器）
                .sync()                                                // 5. 阻塞等待连接
                .channel()                                             // 6. 取得物流方式
                .writeAndFlush(new Date() + ": hello world!");      // 7. 发出包裹
    }

}
