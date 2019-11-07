package com.basecare.ding192.data.api;

import com.basecare.ding192.constants.ChatConstant;
import com.basecare.ding192.data.entity.SendMsgBean;
import com.basecare.ding192.data.entity.UploadAttachmentBean;

import cn.dr.basemvp.app.AppConfig;
import cn.dr.basemvp.net.BaseHttpResult;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by dingrui 2019/11/6
 */

public interface IChatService {

    /**
     * 加载聊天记录
     *
     * @param channel_id
     * @param uuid
     * @param limit
     * @param first_id
     * @return
     */
    @GET(AppConfig.BASE_URL + ChatConstant.CHAT_HIS_LIST_URL)
    Observable<String> getData(@Query("channel_id") int channel_id,
                               @Query("uuid") String uuid,
                               @Query("limit") int limit,
                               @Query("first_id") int first_id);

    /**
     * 发送消息(文字)
     *
     * @param uuid
     * @param body
     * @param partner_id
     * @return
     */
    @POST(AppConfig.BASE_URL + ChatConstant.SEND_MSG_URL)
    @FormUrlEncoded
    Observable<BaseHttpResult<SendMsgBean>> sendMsgWithText(@Field("uuid") String uuid,
                                                            @Field("body") String body,
                                                            @Field("partner_id") int partner_id);

    /**
     * 发送消息(附件)
     *
     * @param uuid
     * @param attachment_ids
     * @param partner_id
     * @return
     */
    @POST(AppConfig.BASE_URL + ChatConstant.SEND_MSG_URL)
    @FormUrlEncoded
    Observable<BaseHttpResult<SendMsgBean>> sendMsgWithAttachment(@Field("uuid") String uuid,
                                                                  @Field("attachment_ids") String attachment_ids,
                                                                  @Field("partner_id") int partner_id);

    /**
     * 上传附件
     *
     * @return
     */
    @Multipart
    @POST(AppConfig.BASE_URL + ChatConstant.UPLOAD_ATTACHMENT_URL)
    Observable<BaseHttpResult<UploadAttachmentBean>> uploadAttachment(@Part("model") RequestBody model,
                                                                      @Part("id") RequestBody id,
                                                                      @Part("description") RequestBody description,
                                                                      @Part() MultipartBody.Part part);
}
