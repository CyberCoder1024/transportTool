package com.aodingkun.netty.server;

import com.aodingkun.netty.common.ByteUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ServerHandler
 * @Description
 * @Author jonas.ao
 * @Date 2020/4/18
 * Project transportTool
 * @Version 1.0
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private ObjectMapper objectMapper= ByteUtils.InstanceObjectMapper();
    private Logger logger = Logger.getLogger(this.getClass());
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof List){
            logger.info("这是一个List:"+(List)msg);
        }else if (msg instanceof Map){
            logger.info("这是一个Map:"+(Map)msg);
        }else{
            logger.info("这是一个对象："+msg.getClass().getName());
            logger.info("这是一个对象："+msg);
        }
    }
}
