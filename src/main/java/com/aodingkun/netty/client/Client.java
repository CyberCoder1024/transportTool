package com.aodingkun.netty.client;

import com.aodingkun.netty.client.echo.EchoHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @ClassName Client
 * @Description
 * @Author jonas.ao
 * @Date 2020/4/18
 * Project transportTool
 * @Version 1.0
 **/
public class Client {
    private String ip;
    private int port;
    public  void init() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.handler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));
                   // ch.pipeline().addLast(new EncoderHandler_3_0());
                    ch.pipeline().addLast(new EchoHandler());
                }
            });
            bootstrap.remoteAddress(ip,port);
            ChannelFuture future = bootstrap.connect().sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new Client("127.0.0.1",8777).init();
    }
}
