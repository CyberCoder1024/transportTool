package com.aodingkun.netty.codec;

import com.aodingkun.netty.common.MsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

/**
 * @ClassName MsgEncode
 * @Description 编码器 服务器 按照协议格式返回数据给客户端时调用
 * @Author jonas.ao
 * @Date 2020/4/17
 * Project transportTool
 * @Version 1.0
 **/
public class MsgEncode extends MessageToByteEncoder {
    private  Logger logger = Logger.getLogger(this.getClass());
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf outBuf) throws Exception {
        if (msg instanceof MsgProtocol){
            MsgProtocol protocol=(MsgProtocol) msg;
            outBuf.writeByte(protocol.getHeader());
            outBuf.writeByte(protocol.getFlag());//数据类型标志
            outBuf.writeByte(protocol.getCmd());
            outBuf.writeInt(protocol.getLen());
            outBuf.writeBytes(protocol.getData());
            outBuf.writeByte(protocol.getTail());
            logger.debug("数据编码成功"+outBuf);

        }else {
            logger.info("不支持本数据协议："+msg.getClass()+"\t期待的数据协议类："+ MsgProtocol.class);
        }

    }
}
