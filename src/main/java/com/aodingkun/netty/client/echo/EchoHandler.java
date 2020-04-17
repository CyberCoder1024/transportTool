package com.aodingkun.netty.client.echo;

import com.aodingkun.netty.common.MsgProtocol;
import com.aodingkun.netty.common.MsgUtils;
import com.aodingkun.pojo.Device;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @ClassName EchoHandler
 * @Description
 * @Author jonas.ao
 * @Date 2020/4/18
 * Project transportTool
 * @Version 1.0
 **/
public class EchoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Device device = new Device();
        device.setDevcode("zg");
        device.setDevId(24);
        device.setSendTime(new Date());
        device.setUID(UUID.randomUUID().toString());
        HashMap<String, Device> map = new HashMap<>();
        map.put("数据1111111", device);
        map.put("数据2222222", device);
        ArrayList<Device> list = new ArrayList<>();
        list.add(device);
        list.add(device);
        list.add(device);
        MsgProtocol msgProtocol = MsgUtils.prtclInstance(map, device.getClass().getName());
        ctx.writeAndFlush(msgProtocol);
    }
}
