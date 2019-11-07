package com.basecare.ding192.chatlib;

import com.basecare.ding192.data.RetrofitUtils;
import com.basecare.ding192.data.entity.SendMsgBean;
import com.basecare.ding192.data.entity.UploadAttachmentBean;

import java.io.File;

import cn.dr.basemvp.mvp.BaseModel;
import cn.dr.basemvp.net.BaseHttpResult;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by dingrui 2019/10/30
 */

public class ChatModel extends BaseModel implements ChatContract.Model {
    @Override
    public Observable<String> getData(int channelId, String uuid, int limit, int firstId) {
        return RetrofitUtils.getChatService().getData(channelId, uuid, limit, firstId);
    }

    @Override
    public Observable<BaseHttpResult<SendMsgBean>> sendMsgWithText(String uuid, String body, int partner_id) {
        return RetrofitUtils.getChatService().sendMsgWithText(uuid, body, partner_id);
    }

    @Override
    public Observable<BaseHttpResult<SendMsgBean>> sendReportMsg(String uuid, String body, int partner_id) {
        return RetrofitUtils.getChatService().sendMsgWithText(uuid, body, partner_id);
    }

    @Override
    public Observable<BaseHttpResult<SendMsgBean>> sendMsgWithAttachment(String uuid, String attachment_ids, int partner_id) {
        return RetrofitUtils.getChatService().sendMsgWithAttachment(uuid, attachment_ids, partner_id);
    }

    @Override
    public Observable<BaseHttpResult<UploadAttachmentBean>> uploadAttachment(String model, int id, File ufile, String description, String mimeType) {
        RequestBody modelBody = convertToRequestBody(model);
        RequestBody idBody = convertToRequestBody(id + "");
        RequestBody descriptionBody = convertToRequestBody(description);
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), ufile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("ufile", ufile.getName(), requestFile);
        return RetrofitUtils.getChatService().uploadAttachment(modelBody, idBody, descriptionBody, part);
    }

    private RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }
}
