package com.aodingkun.netty.common;

import java.util.Arrays;

/**
 * @ClassName MsgProtocol
 * @Description 自定义基于TCP的传输协议
 * @Author jonas.ao
 * @Date 2020/4/17 15:12
 * Project transportTool
 * @Version 1.0
 **/
public class MsgProtocol {
    /**
     * +---+-------+-------+-------+---+
     * |帧头|数据类型|数据长度|传输数据 |帧尾|
     * +---+-------+-------+-------+---+
     */
    private byte header;//用16进制表示  开始和结束位   1
    private byte flag;//类型标志                      1
    private byte cmd;//命令                          1
    //private byte clasLen;
    private int len;//数据位长度                      4
   // private byte[] className;
    private byte[] data;
    //private short crc;
    private byte tail;

    public MsgProtocol(){

    }
    public MsgProtocol(int len, byte[] data){
        this.len=len;
        this.data=data;
    }
    public MsgProtocol(byte header, byte flag, byte cmd, byte[] data){
        this.header=header;
        this.flag=flag;
        this.cmd=cmd;
        this.len=(byte)data.length;
        this.data=data;
    }


    public byte getHeader() {
        return header;
    }

    public void setHeader(byte header) {
        this.header = header;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte getTail() {
        return tail;
    }

    public void setTail(byte tail) {
        this.tail = tail;
    }

    @Override
    public String toString() {
        return "MsgProtocol{" +
                "header=" + header +
                ", flag=" + flag +
                ", cmd=" + cmd +
                ", len=" + len +
                ", data=" + Arrays.toString(data) +
                ", tail=" + tail +
                '}';
    }
}
