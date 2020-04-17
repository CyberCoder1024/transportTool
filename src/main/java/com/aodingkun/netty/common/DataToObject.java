package com.aodingkun.netty.common;

/**
 * @ClassName DataToObject
 * @Description 反序列化 传输数据时 传递的对象较多 将传输对象进行二次包装
 * 这样在反序列化的时候使用Class.forName()获取Class,
 * 避免了要写很多if循环判断反序列化的对象的Class。前提是要类名和包路径要完全匹配
 * @Author jonas.ao
 * @Date 2020/4/18
 * Project transportTool
 * @Version 1.0
 **/

import java.util.Arrays;

/**
 * 通过全类名字符串解析成具体对象
 */
public class DataToObject<T> {
    private String className;
    private byte[] object;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public byte[] getObject() {
        return object;
    }

    public void setObject(byte[] object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "DataToObject{" +
                "className='" + className + '\'' +
                ", object=" + Arrays.toString(object) +
                '}';
    }
}
