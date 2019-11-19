package com.basecare.ding192.chatlib;

import com.basecare.ding192.data.entity.ReportDetailBean;
import com.basecare.ding192.data.entity.SendMsgBean;
import com.basecare.ding192.data.entity.UploadAttachmentBean;

import java.io.File;

import cn.dr.basemvp.mvp.BasePresenter;
import cn.dr.basemvp.net.BaseHttpResult;
import cn.dr.basemvp.net.BaseObserver;
import cn.dr.basemvp.rx.RxSchedulers;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by dingrui 2019/10/30
 */

public class ChatPresenter extends BasePresenter<ChatContract.Model, ChatContract.View> {
    @Override
    protected ChatContract.Model createModel() {
        return new ChatModel();
    }


    /**
     * 加载聊天记录
     *
     * @param channelId
     * @param uuid
     * @param limit
     * @param firstId
     */
    public void getData(int channelId, String uuid, int limit, int firstId) {
        getModel().getData(channelId, uuid, limit, firstId)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                        if (s != null) {
                            getView().showData(s);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 发送消息（无附件）
     *
     * @param uuid
     * @param body
     * @param partner_id
     */
    public void sendMsgWithText(String uuid, String body, int partner_id) {
        getModel().sendMsgWithText(uuid, body, partner_id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<SendMsgBean>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<SendMsgBean> result) {
                        if (result != null) {
                            getView().showSendMsgWithText(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, boolean isNetError) {
                        getView().showError(errMsg);
                    }
                });
    }

    /**
     * 发送报告消息
     *  @param uuid
     * @param body
     * @param partner_id
     * @param bean
     */
    public void sendReportMsg(String uuid, String body, int partner_id, ReportDetailBean.ReportsBean bean) {
        getModel().sendMsgWithText(uuid, body, partner_id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<SendMsgBean>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<SendMsgBean> result) {
                        if (result != null) {
                            getView().showSendReportMsg(result.getData(),bean);
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, boolean isNetError) {
                        getView().showError(errMsg);
                    }
                });
    }

    /**
     * 发送消息（有附件）
     *
     * @param uuid
     * @param attachment_ids
     * @param partner_id
     */
    public void sendMsgWithAttachment(String uuid, String attachment_ids, int partner_id, UploadAttachmentBean bean) {
        getModel().sendMsgWithAttachment(uuid, attachment_ids, partner_id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<SendMsgBean>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<SendMsgBean> result) {
                        if (result != null) {
                            getView().showSendMsgWithAttachment(result.getData(), bean);
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, boolean isNetError) {
                        getView().showError(errMsg);
                    }
                });
    }

    /**
     * 上传附件
     *
     * @param model
     * @param id
     * @param file
     * @param description
     */
    public void uploadAttatchment(String model, int id, File file, String description,String mimeType) {
        getModel().uploadAttachment(model, id, file, description,mimeType)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<UploadAttachmentBean>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<UploadAttachmentBean> result) {
                        if (result != null) {
                            getView().showUploadAttachment(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, boolean isNetError) {
                        getView().showError(errMsg);
                    }
                });
    }
}
