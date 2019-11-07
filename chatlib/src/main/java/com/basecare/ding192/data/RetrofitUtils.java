package com.basecare.ding192.data;

import com.basecare.ding192.data.api.IChatService;

import cn.dr.basemvp.net.BaseRetrofit;

/**
 * @desc 网络请求管理类
 */
public class RetrofitUtils extends BaseRetrofit {
    private static IChatService chatService;

    /**
     * @return retrofit的底层利用反射的方式, 获取所有的api接口的类
     * chat相关请求
     */
    public static IChatService getChatService() {
        if (chatService == null) {
            chatService = getRetrofit().create(IChatService.class);
        }
        return chatService;
    }

}