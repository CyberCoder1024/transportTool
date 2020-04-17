package com.aodingkun.netty.server;

import com.aodingkun.netty.codec.MsgDecode;
import com.aodingkun.netty.codec.MsgEncode;
import com.aodingkun.netty.common.MsgUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.log4j.Logger;

/**
 * @ClassName Server 服务端
 * @Description
 * @Author jonas.ao
 * @Date 2020/4/17
 * Project transportTool
 * @Version 1.0
 **/
public class Server {
    private Logger logger = Logger.getLogger(this.getClass());
    private int port;
    public static void main(String[] args) {
        new Server(MsgUtils.PORT).start();
    }
    /**
     * @Description : 功能说明
     * @MethodName start
     * @Description:
     * @Return : void
     * @Author : AoDingKun
     * @Date : 2020/4/17 16:12
    */
    public void start(){
        //1 第一个线程组用于接收Client 连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //2 第二个线程组用于接收实际业务
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //3 创建一个启动NIO服务的辅助启动类ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //4 绑定两个线程组
            serverBootstrap.group(bossGroup,workGroup)
                    //5 需要指定使用NioSocketServerChannel 这种类型的通道
                    .channel(NioServerSocketChannel.class)
                    //6 一定需要使用ChildHandler 去绑定那个具体事务处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch)throws Exception{
                            ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
                            ch.pipeline().addLast("encode",new MsgEncode());//编码器。发送消息时候用
                            ch.pipeline().addLast("decode",new MsgDecode());//解码器，接收消息时候用
                            ch.pipeline().addLast("handler",new ServerHandler());//业务处理类，最终的消息会在这个handler中进行业务处理
                        }
                    })
                    //8  设置TCP的缓冲区
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 设置发送缓存大小 32k发送缓冲
                    .option(ChannelOption.SO_SNDBUF, 32*1024)
                    //设置接收缓冲大小
                    .option(ChannelOption.SO_RCVBUF,32*1024 )
                    //9 保持连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //10 绑定指定端口进行监听 可以绑定多个端口
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();//对应端口异步等待关闭

        }catch (InterruptedException e){
            System.out.println("启动异常："+e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }
    }

    public Server(int port){
        this.port=port;
    }
}
