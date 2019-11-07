package com.basecare.ding192.data.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by dingrui 2019/11/5
 */

public class ChatEntity implements MultiItemEntity {

    private int type;
    private int id;
    private int duration;
    private String date;
    private String content;
    private String url;
    private String sendAvatar;
    private String receiveAvatar;
    private int voiceState = DEFAULT_VOICE;

    public static final int SEND_TEXT = 1;
    public static final int RECEIVE_TEXT = 2;
    public static final int SEND_VOICE = 3;
    public static final int RECEIVE_VOICE = 4;
    public static final int SEND_IMG = 5;
    public static final int RECEIVE_IMG = 6;
    public static final int SEND_VIDEO = 7;
    public static final int RECEIVE_VIDEO = 8;
    public static final int SEND_REPORT = 9;
    public static final int RECEIVE_REPORT = 10;
    public static final int TIPS = 11;
    public static final int DEFAULT_VOICE = 1001;
    public static final int START_VOICE = 1002;
    public static final int STOP_VOICE = 1003;
    public static final int COMPLITED_VOICE = 1004;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVoiceState() {
        return voiceState;
    }

    public void setVoiceState(int voiceState) {
        this.voiceState = voiceState;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSendAvatar() {
        return sendAvatar;
    }

    public void setSendAvatar(String sendAvatar) {
        this.sendAvatar = sendAvatar;
    }

    public String getReceiveAvatar() {
        return receiveAvatar;
    }

    public void setReceiveAvatar(String receiveAvatar) {
        this.receiveAvatar = receiveAvatar;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
