package com.basecare.ding192.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.basecare.ding192.chatlib.R;
import com.basecare.ding192.chatlib.photo.SinglePhotoViewActivity;
import com.basecare.ding192.chatlib.video.MyVideoActivity;
import com.basecare.ding192.constants.ChatConstant;
import com.basecare.ding192.data.entity.ChatEntity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpiz.android.bubbleview.BubbleRelativeLayout;
import com.lqr.audio.AudioPlayManager;
import com.lqr.audio.IAudioPlayListener;

import java.util.List;

import cn.dr.basemvp.glide.GlideUtils;
import cn.dr.basemvp.utils.CommonUtils;
import cn.dr.basemvp.utils.DisplayUtils;
import cn.dr.basemvp.utils.PermissonUtils;
import cn.dr.basemvp.utils.TimeUtils;
import cn.dr.basemvp.utils.ToastUtils;
import cn.dr.basemvp.widget.pdf.PdfActivity;

/**
 * Created by dingrui 2019/11/5
 */

public class ChatAdapter extends BaseMultiItemQuickAdapter<ChatEntity, BaseViewHolder> {
    private Activity mActivity;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatAdapter(List<ChatEntity> data, Activity activity) {
        super(data);
        this.mActivity = activity;
        addItemType(ChatEntity.SEND_TEXT, R.layout.item_send_text);
        addItemType(ChatEntity.RECEIVE_TEXT, R.layout.item_receive_text);
        addItemType(ChatEntity.SEND_VOICE, R.layout.item_send_voice);
        addItemType(ChatEntity.RECEIVE_VOICE, R.layout.item_receive_voice);
        addItemType(ChatEntity.SEND_IMG, R.layout.item_send_img);
        addItemType(ChatEntity.RECEIVE_IMG, R.layout.item_receive_img);
        addItemType(ChatEntity.SEND_VIDEO, R.layout.item_send_video);
        addItemType(ChatEntity.RECEIVE_VIDEO, R.layout.item_receive_video);
        addItemType(ChatEntity.SEND_REPORT, R.layout.item_send_report);
        addItemType(ChatEntity.RECEIVE_REPORT, R.layout.item_receive_report);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatEntity item) {
        //设置时间戳
        setTimeStamp(helper, item);
        switch (item.getType()) {
            //发送文字
            case ChatEntity.SEND_TEXT:
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getSendAvatar(), 0);
                //内容
                helper.setText(R.id.tv_content, Html.fromHtml(item.getContent() == null ? "" : item.getContent().replace("<p>","").replace("</p>","")));
                break;
            //接收文字
            case ChatEntity.RECEIVE_TEXT:
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getReceiveAvatar(), 0);
                //内容
                helper.setText(R.id.tv_content, Html.fromHtml(item.getContent() == null ? "" : item.getContent().replace("<p>","").replace("</p>","")));
                break;
            //发送语音
            case ChatEntity.SEND_VOICE:
                //发送的语音
                switch (item.getVoiceState()) {
                    case ChatEntity.DEFAULT_VOICE:
                        setVoiceBg(helper.getView(R.id.iv_audio), R.drawable.ic_audio_unplay_right);
                        break;
                    case ChatEntity.START_VOICE:
                        setVoiceBg(helper.getView(R.id.iv_audio), R.drawable.ic_audio_play_right_gif);
                        break;
                    case ChatEntity.STOP_VOICE:
                        setVoiceBg(helper.getView(R.id.iv_audio), R.drawable.ic_audio_unplay_right);
                        break;
                    case ChatEntity.COMPLITED_VOICE:
                        setVoiceBg(helper.getView(R.id.iv_audio), R.drawable.ic_audio_unplay_right);
                        break;
                }
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getSendAvatar(), 0);
                //动态控制语音背景长度
                int incrementSend = (int) (DisplayUtils.getScreenWidth(mContext) / 4 / ChatConstant.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND * item.getDuration());
                BubbleRelativeLayout rlAudioSend = helper.getView(R.id.rl_audio);
                ViewGroup.LayoutParams paramsSend = rlAudioSend.getLayoutParams();
                paramsSend.width = CommonUtils.dp2px(65) + CommonUtils.dp2px(incrementSend);
                rlAudioSend.setLayoutParams(paramsSend);
                //语音时长
                helper.setText(R.id.tv_duration, item.getDuration() + "''");
                //播放语音
                rlAudioSend.setOnClickListener(v -> {
                    //播放语音
                    if (item.getVoiceState() != ChatEntity.START_VOICE) {
                        startPlay(item);
                    } else {
                        AudioPlayManager.getInstance().stopPlay();
                    }
                });
                break;
            //接收的语音
            case ChatEntity.RECEIVE_VOICE:
                switch (item.getVoiceState()) {
                    case ChatEntity.DEFAULT_VOICE:
                        setVoiceBg(helper.getView(R.id.iv_audio), R.drawable.ic_audio_unplay_left);
                        break;
                    case ChatEntity.START_VOICE:
                        setVoiceBg(helper.getView(R.id.iv_audio), R.drawable.ic_audio_play_left_gif);
                        break;
                    case ChatEntity.STOP_VOICE:
                        setVoiceBg(helper.getView(R.id.iv_audio), R.drawable.ic_audio_unplay_left);
                        break;
                    case ChatEntity.COMPLITED_VOICE:
                        setVoiceBg(helper.getView(R.id.iv_audio), R.drawable.ic_audio_unplay_left);
                        break;
                }
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getReceiveAvatar(), 0);
                //动态控制语音背景长度
                int incrementReceive = (int) (DisplayUtils.getScreenWidth(mContext) / 4 / ChatConstant.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND * item.getDuration());
                BubbleRelativeLayout rlAudioReceive = helper.getView(R.id.rl_audio);
                ViewGroup.LayoutParams paramsReceive = rlAudioReceive.getLayoutParams();
                paramsReceive.width = CommonUtils.dp2px(65) + CommonUtils.dp2px(incrementReceive);
                rlAudioReceive.setLayoutParams(paramsReceive);
                //语音时长
                helper.setText(R.id.tv_duration, item.getDuration() + "''");
                //播放语音
                rlAudioReceive.setOnClickListener(v -> {
                    //播放语音
                    if (item.getVoiceState() != ChatEntity.START_VOICE) {
                        startPlay(item);
                    } else {
                        AudioPlayManager.getInstance().stopPlay();
                    }
                });
                break;
            //发送的图片
            case ChatEntity.SEND_IMG:
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getSendAvatar(), 0);
                //显示图片
                GlideUtils.loadImage(mActivity, helper.getView(R.id.iv_photo), item.getUrl(), 0);
                //点击预览图片
                helper.getView(R.id.rl_photo).setOnClickListener(v -> {
                    //预览图片
                    Intent intent = new Intent(mActivity, SinglePhotoViewActivity.class);
                    intent.putExtra("url", item.getUrl());
                    mActivity.startActivity(intent);
                });
                break;
            //接收的图片
            case ChatEntity.RECEIVE_IMG:
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getReceiveAvatar(), 0);
                //显示图片
                GlideUtils.loadImage(mActivity, helper.getView(R.id.iv_photo), item.getUrl(), 0);
                //点击预览图片
                helper.getView(R.id.rl_photo).setOnClickListener(v -> {
                    //预览图片
                    Intent intent = new Intent(mActivity, SinglePhotoViewActivity.class);
                    intent.putExtra("url", item.getUrl());
                    mActivity.startActivity(intent);
                });
                break;
            //发送的视频
            case ChatEntity.SEND_VIDEO:
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getSendAvatar(), 0);
                //时长
                if (item.getDuration() < 10) {
                    helper.setText(R.id.tv_duration, "0:0" + item.getDuration() + "");
                } else {
                    helper.setText(R.id.tv_duration, "0:" + item.getDuration() + "");
                }
                //显示封面图片
                GlideUtils.loadImage(mActivity, helper.getView(R.id.iv_thumb), item.getUrl(), 0);
                //点击预览视频
                helper.getView(R.id.rl_video).setOnClickListener(v -> {
                    //预览视频
                    Intent intent = new Intent(mContext, MyVideoActivity.class);
                    intent.putExtra("video_url", item.getUrl());
                    mActivity.startActivity(intent);
                });
                break;
            //接收的视频
            case ChatEntity.RECEIVE_VIDEO:
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getReceiveAvatar(), 0);
                //时长
                if (item.getDuration() < 10) {
                    helper.setText(R.id.tv_duration, "0:0" + item.getDuration() + "");
                } else {
                    helper.setText(R.id.tv_duration, "0:" + item.getDuration() + "");
                }
                //显示封面图片
                GlideUtils.loadImage(mActivity, helper.getView(R.id.iv_thumb), item.getUrl(), 0);
                //点击预览视频
                helper.getView(R.id.rl_video).setOnClickListener(v -> {
                    //预览视频
                    Intent intent = new Intent(mContext, MyVideoActivity.class);
                    intent.putExtra("video_url", item.getUrl());
                    mActivity.startActivity(intent);
                });
                break;
            //发送的报告
            case ChatEntity.SEND_REPORT:
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getSendAvatar(), 0);
                //名称
                String fromHtml = Html.fromHtml(item.getContent().replace("<p>","").replace("</p>","")).toString();
                if (fromHtml.contains(",")) {
                    String[] split = fromHtml.split(",");
                    helper.setText(R.id.tv_name, split[0]);
                }
                //点击
                helper.addOnClickListener(R.id.rl_report);
                helper.getView(R.id.rl_report).setOnClickListener(v -> {
                    //查看pdf
                    if (fromHtml.contains(",")) {
                        String[] split = fromHtml.split(",");
                        Intent intent = new Intent(mContext, PdfActivity.class);
                        intent.putExtra("url", split[1]);
                        mContext.startActivity(intent);
                    }
                });
                break;
            //接收的报告
            case ChatEntity.RECEIVE_REPORT:
                //头像
                GlideUtils.loadCircleImage(mContext, helper.getView(R.id.avatar), item.getReceiveAvatar(), 0);
                //名称
                String fromHtmlReceive = Html.fromHtml(item.getContent().replace("<p>","").replace("</p>","")).toString();
                if (fromHtmlReceive.contains(",")) {
                    String[] split = fromHtmlReceive.split(",");
                    helper.setText(R.id.tv_name, split[0]);
                }
                //点击
                helper.getView(R.id.rl_report).setOnClickListener(v -> {
                    //查看pdf
                    if (fromHtmlReceive.contains(",")) {
                        String[] split = fromHtmlReceive.split(",");
                        Intent intent = new Intent(mContext, PdfActivity.class);
                        intent.putExtra("url", split[1]);
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
    }

    //根据状态设置voice
    private void setVoiceBg(ImageView view, int resId) {
        if (view != null) {
            view.setImageResource(resId);
        }
    }

    /**
     * 播放语音
     *
     * @param item
     */
    private void startPlay(ChatEntity item) {
        if (PermissonUtils.hasPermissions(mContext, Manifest.permission.WAKE_LOCK,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // 用户已经同意该权限
            realPlayAudio(item);
        } else {
            //用户拒绝权限
            PermissonUtils.requestPermission(mActivity, "播放语音需要调用读取权限", new PermissonUtils.OnPermissionAgreeListener() {
                        @Override
                        public void onAgree() {
                            realPlayAudio(item);
                        }

                        @Override
                        public void onRefuse() {

                        }
                    }, Manifest.permission.WAKE_LOCK,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void realPlayAudio(ChatEntity item) {
        AudioPlayManager.getInstance().startPlay(mActivity, Uri.parse(item.getUrl()), new IAudioPlayListener() {
            @Override
            public void onStart(Uri var1) {
                item.setVoiceState(ChatEntity.START_VOICE);
                notifyItemChanged(mData.indexOf(item));
            }

            @Override
            public void onStop(Uri var1) {
                item.setVoiceState(ChatEntity.STOP_VOICE);
                notifyItemChanged(mData.indexOf(item));
            }

            @Override
            public void onComplete(Uri var1) {
                item.setVoiceState(ChatEntity.COMPLITED_VOICE);
                notifyItemChanged(mData.indexOf(item));
            }
        });
    }

    /**
     * 设置时间戳
     *
     * @param helper
     * @param item
     */
    private void setTimeStamp(BaseViewHolder helper, ChatEntity item) {
        if (item.getDate() != null) {
            int position = helper.getLayoutPosition();
            long msgTime = TimeUtils.string2Millis(item.getDate());
            if (position > 0) {
                ChatEntity preChatEntity = getData().get(position - 1);
                if (preChatEntity.getDate() != null) {
                    long preMsgTime = TimeUtils.string2Millis(preChatEntity.getDate());
                    //超过5分钟
                    if (msgTime - preMsgTime > (5 * 60 * 1000)) {
                        helper.setVisible(R.id.tv_time, true);
                        helper.setText(R.id.tv_time, TimeUtils.getNewChatTime(msgTime));
                    } else {
                        helper.getView(R.id.tv_time).setVisibility(View.GONE);
                    }
                }
            } else if (position == 0) {
                helper.setVisible(R.id.tv_time, true);
                helper.setText(R.id.tv_time, TimeUtils.getNewChatTime(msgTime));
            }
        }
    }
}
