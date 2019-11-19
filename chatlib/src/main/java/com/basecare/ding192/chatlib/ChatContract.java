package com.basecare.ding192.chatlib;

import com.basecare.ding192.data.entity.ReportDetailBean;
import com.basecare.ding192.data.entity.SendMsgBean;
import com.basecare.ding192.data.entity.UploadAttachmentBean;

import java.io.File;

import cn.dr.basemvp.mvp.IModel;
import cn.dr.basemvp.mvp.IView;
import cn.dr.basemvp.net.BaseHttpResult;
import io.reactivex.Observable;

/**
 * Created by dingrui 2019/10/30
 */

interface ChatContract {

    interface View extends IView {

        //聊天记录
        void showData(String data);

        //发送消息
        void showSendMsgWithText(SendMsgBean bean);

        //发送报告
        void showSendReportMsg(SendMsgBean bean, ReportDetailBean.ReportsBean reportsBean);

        //发送消息
        void showSendMsgWithAttachment(SendMsgBean sendMsgBean, UploadAttachmentBean bean);

        //上传附件
        void showUploadAttachment(UploadAttachmentBean bean);
    }

    interface Model extends IModel {

        //聊天记录
        Observable<String> getData(int channelId, String uuid, int limit, int firstId);

        //发送消息
        Observable<BaseHttpResult<SendMsgBean>> sendMsgWithText(String uuid, String body, int partner_id);

        //发送报告
        Observable<BaseHttpResult<SendMsgBean>> sendReportMsg(String uuid, String body, int partner_id);

        //发送附件
        Observable<BaseHttpResult<SendMsgBean>> sendMsgWithAttachment(String uuid, String attachment_ids, int partner_id);

        //上传附件
        Observable<BaseHttpResult<UploadAttachmentBean>> uploadAttachment(String model, int id, File ufile, String description, String mimeType);
    }
}
