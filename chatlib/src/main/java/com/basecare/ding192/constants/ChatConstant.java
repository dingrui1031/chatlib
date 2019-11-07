package com.basecare.ding192.constants;


import cn.dr.basemvp.app.AppConfig;
import cn.dr.basemvp.utils.FileUtils;

/**
 * Created by dingrui 2019/11/5
 */

public class ChatConstant extends AppConfig {

    //默认语音120秒
    public static final int DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND = 120;

    //语音存放位置
    public static String AUDIO_SAVE_DIR = FileUtils.getDir("base360_doctor_audio");

    //聊天头像前缀
    public static final String CHAT_AVATAR_URL = "https://test.basecare.cn/gc/image/res.partner/";

    //聊天文件前缀
    public static final String CHAT_IMAGE_URL = "https://test.basecare.cn/gy/content/";

    //聊天消息列表
    public static final String CHAT_HIS_LIST_URL = "/gy/message/fetch";
    //发送消息
    public static final String SEND_MSG_URL = "/gy/message/post_nologin";
    //上传附件
    public static final String UPLOAD_ATTACHMENT_URL = "/gy/binary/upload_attachment";

}
