package com.aodingkun.pojo;

import java.util.Date;

/**
 * @ClassName Device
 * @Description
 * @Author jonas.ao
 * @Date 2020/4/18
 * Project transportTool
 * @Version 1.0
 **/
public class Device {
    private String devcode;
    private int devId;
    private String UID;
    private Date sendTime;

    public String getDevcode() {
        return devcode;
    }

    public void setDevcode(String devcode) {
        this.devcode = devcode;
    }

    public int getDevId() {
        return devId;
    }

    public void setDevId(int devId) {
        this.devId = devId;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "Device{" +
                "devcode='" + devcode + '\'' +
                ", devId=" + devId +
                ", UID='" + UID + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
