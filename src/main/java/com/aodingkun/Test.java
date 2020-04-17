package com.aodingkun;

import com.aodingkun.netty.common.ByteUtils;
import com.aodingkun.pojo.Device;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName Test
 * @Description
 * @Author jonas.ao
 * @Date 2020/4/18
 * Project transportTool
 * @Version 1.0
 **/
public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        Device device = new Device();
        device.setDevcode("1234567890");
        device.setDevId(1234);
        device.setUID(UUID.randomUUID().toString());
        device.setSendTime(new Date());
        System.out.println(Device.class.getName());
        Class<?> type = Class.forName(Device.class.getName());
        ObjectMapper objectMapper = ByteUtils.InstanceObjectMapper();
        try {
            String value = objectMapper.writeValueAsString(device);
            System.out.println(value);
            System.out.println(objectMapper.readValue(value.getBytes(),type));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
