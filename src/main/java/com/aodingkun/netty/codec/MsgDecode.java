package com.aodingkun.netty.codec;

import com.aodingkun.netty.common.ByteUtils;
import com.aodingkun.netty.common.DataToObject;
import com.aodingkun.netty.common.MsgUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MsgDecode
 * @Description 编码器 自定义解码协议 粘包 拆包
 * @Author jonas.ao
 * @Date 2020/4/17
 * Project transportTool
 * @Version 1.0
 **/
public class MsgDecode extends ByteToMessageDecoder {
    ///最小的数据长度：开头标准位1字节
    private static int MIN_DATA_LEN = 7;
    private  Logger logger = Logger.getLogger(this.getClass());

    /**
     * @param ctx
     * @param inBuf
     * @param out
     * @Description : 功能说明
     * @MethodName decode
     * @Description:
     * @Return : void
     * @Author : AoDingKun
     * @Date : 2020/4/18 0:34
     */

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf inBuf, List<Object> out) throws Exception {

        if (inBuf.readableBytes() > MIN_DATA_LEN) {
            logger.debug("开始解码数据");
            //标记 读操作的指针
            inBuf.markReaderIndex();
            //读取第一个字节
            byte header = inBuf.readByte();
            if (header == MsgUtils.HEADER) {
                logger.debug("数据帧头正确");
                //读取数据类型标志位
                byte flag = inBuf.readByte();//读取标志位
                byte cmd = inBuf.readByte();//读取命令位
                //读取字节数据的长度
                int len = inBuf.readInt();
               /* if (len>=inBuf.readableBytes()){
                    //数据可读长度必须要大于len 因为结尾还有  crc tail
                    System.out.println(String.format("数据长度不够，数据协议len长度为：%1$d,数据包实际长度内容：%2$d 正在等待拆包···",len,inBuf.readableBytes()));
                    inBuf.resetReaderIndex();
                    *//*
                 **结束解码，这种情况说明数据没有到齐，在父类ByteToMessageDecoder的callDecode中会对out和in进行判断
                 * 如果in里面还有可读内容即in.isReadable为true,cumulation中的内容会进行保留，，直到下一次数据到来，将两帧的数据合并起来，再解码。
                 * 以此解决拆包问题
                 *//*
                    return;

                }*/
                if (len < inBuf.readableBytes()) {
                    byte[] data = new byte[len];
                    inBuf.readBytes(data);//可读核心的数据
                    //short crc = inBuf.readShort();//读取crc校验位
                    byte tail = inBuf.readByte();
                    try {
                        if (tail == MsgUtils.TAIL) {
                            ObjectMapper objectMapper = ByteUtils.InstanceObjectMapper();
                            DataToObject dtObject = objectMapper.readValue(data, DataToObject.class);
                            Class<?> Type = Class.forName(dtObject.getClassName());
                            logger.debug("数据解析成功，开始封装数据");
                            if (flag == MsgUtils.OBJ_TYPE) {
                                Object o= objectMapper.readValue(dtObject.getObject(), Type);
                                out.add(o);
                            }else if (flag==MsgUtils.LIST_TYPE){
                                JavaType javaType=TypeFactory.defaultInstance().constructCollectionType(List.class,Type);
                                Object o = objectMapper.readValue(dtObject.getObject(), javaType);
                                out.add(o);

                            }else if (flag==MsgUtils.MAP_TYPE){
                                JavaType javaType= TypeFactory.defaultInstance().constructMapType(Map.class,String.class,Type);
                                Object o = objectMapper.readValue(dtObject.getObject(), javaType);
                                out.add(o);
                            }
                        }else {
                            logger.debug(String.format("数据解码协议结束标志位:%1$d [错误!]，期待的结束标志位是：%2$d",tail,MsgUtils.TAIL));
                            return;
                        }
                    }catch (ClassNotFoundException e){
                        logger.error(String.format("反序列化对象的类找不到,注意包名匹配！ "));
                        return;
                    }catch (Exception e){
                        logger.error(e);
                        return;
                    }
                }else {
                    logger.debug(String.format("数据长度不够，数据协议len长度为：%1$d,数据包实际可读内容为：%2$d正在等待处理拆包……",len,inBuf.readableBytes()));
                    inBuf.resetReaderIndex();
                    return;
                    /*
                     **结束解码，这种情况说明数据没有到齐，在父类ByteToMessageDecoder的callDecode中会对out和in进行判断
                     * 如果in里面还有可读内容即in.isReadable位true,cumulation中的内容会进行保留，，直到下一次数据到来，将两帧的数据合并起来，再解码。
                     * 以此解决拆包问题
                     */
                }

            } else {
                logger.debug("开头不对 可能不是期待的客户端发送的数据 将自动略过一个字节");
            }
        } else {
            logger.debug("数据长度不符合要求，期待的最小长度是：" + MIN_DATA_LEN + "字节");
            return;
        }
    }
    /**
     * @Description : 功能说明 回调
     * @MethodName callDecode
     * @Description:
     * @param ctx
     * @param in
     * @param out
     * @Return : void
     * @Author : AoDingKun
     * @Date : 2020/4/17 23:00
     */

 /*   @Override
    protected void callDecode(ChannelHandlerContext ctx, ByteBuf inBuf, List<Object> out) {
        try {
            while (inBuf.isReadable()){
                //buf 是否还有数据
                int outSize=out.size();//标记out的size 解析成功的数据会添加到out中
                if (outSize>0){
                    fireChannelRead(ctx,out,outSize){//这个是回调业务的handler的channelRead方法
                        out.clear();
                        if (ctx.isRemoved()){
                            break;
                        }
                        outSize=0;//清空了out 将标记size清零
                    }
                    int oldInputLength=inBuf.readableBytes();//这里开始 准备调用decode 方法 标记解码前的可读内容

                    decode(ctx, inBuf, out);//调用 decoedr中的decode方法

                    if (ctx.isRemoved()){
                        break;
                    }

                    if (outSize==out.size()){//相等说明 并没有解析出来新的object到 0ut 中
                        if ((oldInputLength==inBuf.readableBytes())){
                            //如果相等说明 decode中没有读取任何内容出来；
                            //这是一般发生再拆包之后 将ButBufde指针重置；
                            // 重置后这个方法break出来 让bytemessageDecoder 去处理拆包问题 这里体向按照netty的原则
                            break;
                        }else {
                            continue;//直接continue 考虑让开发者跳过某些字节 比如收到了socket攻击时 数据不按照协议来的适合直接跳过这些字节
                        }
                    }
                    if (oldInputLength==inBuf.readableBytes()){
                        throw new DecoderException(
                                StringUtil.simpleClassName(getClass())+".decode() did not read anything but decode a message"
                        );
                    }
                    if (isSingleDecode()){//默认为false 用来设置只是解析一条数据
                        break;
                    }
                    //这里结束之后 计系while循环 因为bytebuf 仍然由可读的内容 将会继续调用decode方法解析bytebuf中字节码 以此解决粘包问题
                }
            }
        }catch(DecoderException e){
            throw e;
        }catch (Throwable cause){
            throw new DecoderException(cause);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf){
            CodecOutputList out=CodecOutputList.newInstance();
            try {
                ByteBuf data=(ByteBuf)msg;
                first=setCumulator();
            }

        }
    }*/
}
