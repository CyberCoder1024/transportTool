package com.aodingkun.netty.common;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MsgUtils
 * @Description
 * @Author jonas.ao
 * @Date 2020/4/17 16:02
 * Project transportTool
 * @Version 1.0
 **/
public class MsgUtils {
    public static final byte HEADER=0x58;

    public final static byte OBJ_TYPE=0x51;
    public final static byte LIST_TYPE=0x52;
    public final static byte MAP_TYPE=0x53;

    public static final byte TAIL=0X63;


    public static int PORT=8777;
    /**
     * @Description : 功能说明 创建集合类 list map 对象
     * @MethodName prtclInstance
     * @Description:
     * @param o
     * @param className
     * @Return : com.aodingkun.netty.common.MsgProtocol
     * @Author : AoDingKun
     * @Date : 2020/4/18 3:16
    */

    public static MsgProtocol prtclInstance(Object o, String className){
        MsgProtocol msgProtocol = new MsgProtocol();
        if (o instanceof List){
            msgProtocol.setFlag(LIST_TYPE);
        }else if (o instanceof Map){
            msgProtocol.setFlag(MAP_TYPE);
        }else if (o instanceof Object){
            msgProtocol.setFlag(OBJ_TYPE);
        }
        initProtocol(o, className, msgProtocol);

        return msgProtocol;
    }
    /**
     * @Description : 功能说明 创建单一的对象
     * @MethodName prtclInstance
     * @Description:
     * @param o
     * @Return : com.aodingkun.netty.common.MsgProtocol
     * @Author : AoDingKun
     * @Date : 2020/4/18 3:23
    */

    public static MsgProtocol prtclInstance(Object o){
        MsgProtocol msgProtocol = new MsgProtocol();
        msgProtocol.setFlag(OBJ_TYPE);
        initProtocol(o,o.getClass().getName(),msgProtocol);
        return msgProtocol;
    }

    public static void initProtocol(Object o,String className,MsgProtocol msgProtocol) {
        try {
            DataToObject dtObject = new DataToObject();
            byte [] objectBytes= ByteUtils.InstanceObjectMapper().writeValueAsBytes(o);
            dtObject.setObject(objectBytes);
            dtObject.setClassName(className);
            byte[] bytes = ByteUtils.InstanceObjectMapper().writeValueAsBytes(dtObject);
            msgProtocol.setLen(bytes.length);
            msgProtocol.setData(bytes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }



}
