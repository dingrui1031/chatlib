package com.basecare.ding192.chatlib;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basecare.ding192.adapter.ChatAdapter;
import com.basecare.ding192.constants.ChatConstant;
import com.basecare.ding192.data.entity.ChatEntity;
import com.basecare.ding192.data.entity.ReportDetailBean;
import com.basecare.ding192.data.entity.SendMsgBean;
import com.basecare.ding192.data.entity.UploadAttachmentBean;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;
import cn.dr.basemvp.base.BaseMvpActivity;
import cn.dr.basemvp.utils.KeyBoardUtils;
import cn.dr.basemvp.utils.PermissonUtils;
import cn.dr.basemvp.utils.SPUtils;
import cn.dr.basemvp.utils.StringUtils;
import cn.dr.basemvp.utils.TimeUtils;
import cn.dr.basemvp.utils.ToastUtils;

/**
 * Created by dingrui 2019/11/1
 */
public class ChatActivity extends BaseMvpActivity<ChatPresenter> implements ChatContract.View {
    private static final String TAG = "ChatActivity";
    //lib中使用butterknife 要用R2 并且无法用switch...case
    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.iv_chat_voice)
    ImageView ivChatVoice;
    @BindView(R2.id.et_chat_input)
    EditText etChatInput;
    @BindView(R2.id.iv_chat_more)
    ImageView ivChatMore;
    @BindView(R2.id.tv_chat_speak)
    TextView tvChatSpeak;
    @BindView(R2.id.tv_take_photo)
    TextView tvTakePhoto;
    @BindView(R2.id.tv_gallery)
    TextView tvGallery;
    @BindView(R2.id.tv_take_video)
    TextView tvTakeVideo;
    @BindView(R2.id.tv_file)
    TextView tvFile;
    @BindView(R2.id.tv_chat_send)
    TextView tvChatSend;
    @BindView(R2.id.expandableLayout)
    ExpandableLayout expandableLayout;
    @BindView(R2.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R2.id.layout_edit)
    LinearLayout layoutEdit;
    private ChatAdapter mChatAdapter;
    private boolean isVoice;
    private List<LocalMedia> selectList;
    private int mChannel_id;
    private String mUuid;
    private int mFirstId;
    private int mPartner_id;
    private List<ChatEntity> mList = new ArrayList<>();
    private boolean showMore;
    private List<ReportDetailBean.ReportsBean> mReports;
    private boolean mFromReport;
    private String mInput;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //滚动到底部
                    scrollToBottom(false);
                    break;
            }
        }

    };

    /**
     * 滚动到底部
     *
     * @param smooth
     */
    private void scrollToBottom(boolean smooth) {
        if (smooth) {
            if (mChatAdapter.getItemCount() > 0) {
                recyclerView.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
            }
        } else {
            if (mChatAdapter.getItemCount() > 0) {
                recyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
            }
        }
    }

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void getIntent(Intent intent) {
        mFromReport = intent.getBooleanExtra("fromReport", false);
        mReports = (List<ReportDetailBean.ReportsBean>) intent.getSerializableExtra("reports");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mChannel_id = SPUtils.getInstance(mActivity).getInt("channel_id", 0);
        mUuid = SPUtils.getInstance(mActivity).getString("uuid", "");
        mPartner_id = SPUtils.getInstance(mActivity).getInt("partner_id", 0);
        Log.e(TAG, "channel_id:" + mChannel_id + ",uuid:" + mUuid + ",partner_id:" + mPartner_id);
        tvTitle.setText("在线答疑");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new ChatAdapter(null, mActivity);
        recyclerView.setAdapter(mChatAdapter);
        //初始化语音
        initAudioRecordManager();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        ivBack.setOnClickListener(v -> {
            if (recyclerView != null) {
                KeyBoardUtils.hideSoftInput(recyclerView, mActivity);
            }
            finish();
        });
        //监听expandable
        expandableLayout.setOnExpansionUpdateListener((expansionFraction, state) -> {
            if (state == ExpandableLayout.State.EXPANDED) {
                scrollToBottom(true);
            }
        });
        //刷新加载更多
        smartRefresh.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh();
            if (mFirstId != 0) {
                Log.e(TAG, mFirstId + "");
                getMoreData();
            }
        });
        //参照qq，解决EditText后弹出的输入法会遮盖RecyclerView内容的方法
        layoutEdit.setOnClickListener(v -> {
            //让View 也就是EditText获得焦点
            etChatInput.requestFocus();
            KeyBoardUtils.showSoftInput(etChatInput, mActivity);
            expandableLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (expandableLayout.isExpanded()) {
                        expandableLayout.collapse();
                    }
                }
            }, 300);
            //通过handler保证在主线程中进行滑动操作
            handler.sendEmptyMessageDelayed(0, 200);
        });
        //发送
        tvChatSend.setOnClickListener(v -> {
            mInput = etChatInput.getText().toString();
            if (!mInput.equals("")) {
                //发送文字
                mPresenter.sendMsgWithText(mUuid, mInput, mPartner_id);
                KeyBoardUtils.showSoftInput(etChatInput, mActivity);
            }
        });
        //语音文字按钮切换
        ivChatVoice.setOnClickListener(v -> {
            ivChatVoice.setImageResource(isVoice ? R.drawable.ic_chat_voice : R.drawable.ic_chat_keyboard);
            tvChatSpeak.setVisibility(isVoice ? View.GONE : View.VISIBLE);
            etChatInput.setVisibility(isVoice ? View.VISIBLE : View.GONE);
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse();
//                animToRotate(45, 0);
            }
            isVoice = !isVoice;
            KeyBoardUtils.hideSoftInput(ivChatVoice, mActivity);
        });
        //更多
        ivChatMore.setOnClickListener(v -> {
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse();
                // 旋转，Y轴位移，弹性差值器，时间
//                animToRotate(45, 0);
            } else {
                KeyBoardUtils.hideSoftInput(ivChatMore, mActivity);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expandableLayout.expand();
                    }
                }, 150);
                // 旋转，Y轴位移，弹性差值器，时间
//                animToRotate(0, 45);
            }
        });
        //监听输入框点击
        etChatInput.setOnTouchListener((v, event) -> {
            if (expandableLayout.isExpanded()) {
                KeyBoardUtils.showSoftInput(expandableLayout, mActivity);
//                animToRotate(45, 0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expandableLayout.collapse();
                    }
                }, 300);
            }
            return false;
        });
        //监听输入框
        etChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isEmpty(s.toString())) {
                    ivChatMore.setVisibility(View.GONE);
                    tvChatSend.setVisibility(View.VISIBLE);
                } else {
                    ivChatMore.setVisibility(View.VISIBLE);
                    tvChatSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //监听recyclerview触摸
        recyclerView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    if (expandableLayout.isExpanded()) {
                        // 隐藏
                        expandableLayout.collapse();
                        // 开始动画
                        // 旋转，Y轴位移，弹性差值器，时间
//                        animToRotate(45, 0);
                    }
                    //收起键盘
                    KeyBoardUtils.hideSoftInput(recyclerView, mActivity);
                    break;
            }
            return false;
        });
        //说话
        tvChatSpeak.setOnTouchListener((view, motionEvent) -> {
            float startY = 0;
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startY = motionEvent.getY();
                    if (PermissonUtils.hasPermissions(mActivity, Manifest.permission.RECORD_AUDIO)) {
                        tvChatSpeak.setBackgroundResource(R.drawable.rect_gray_4dp_shape);
                        AudioRecordManager.getInstance(mActivity).startRecord();
                        tvChatSpeak.setText("松开 结束");
                    } else {
                        PermissonUtils.requestPermission(mActivity, "录音需要请求您的录音权限", new PermissonUtils.OnPermissionAgreeListener() {
                            @Override
                            public void onAgree() {
                                AudioRecordManager.getInstance(mActivity).startRecord();
                                tvChatSpeak.setText("松开 结束");
                                tvChatSpeak.setBackgroundResource(R.drawable.rect_gray_4dp_shape);
                            }

                            @Override
                            public void onRefuse() {

                            }
                        }, Manifest.permission.RECORD_AUDIO);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isCancelled(motionEvent, startY)) {
                        AudioRecordManager.getInstance(mActivity).willCancelRecord();
                        tvChatSpeak.setText("松开手指 取消录音");
                        tvChatSpeak.setBackgroundResource(R.drawable.rect_gray_white_stroke_4dp_shape);
                    } else {
                        AudioRecordManager.getInstance(mActivity).continueRecord();
                        tvChatSpeak.setText("松开 结束");
                        tvChatSpeak.setBackgroundResource(R.drawable.rect_gray_4dp_shape);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    tvChatSpeak.setText("按住 说话");
                    tvChatSpeak.setBackgroundResource(R.drawable.rect_gray_white_stroke_4dp_shape);
                    AudioRecordManager.getInstance(mActivity).stopRecord();
                    AudioRecordManager.getInstance(mActivity).destroyRecord();
                    break;
            }
            return true;
        });
        //拍照
        tvTakePhoto.setOnClickListener(v -> {
            if (PermissonUtils.hasPermissions(mActivity, Manifest.permission.CAMERA)) {
                PictureSelector.create(ChatActivity.this)
                        .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                        .compress(true)// 是否压缩 true or false
                        .forResult(PictureConfig.REQUEST_CAMERA);
            } else {
                PermissonUtils.requestPermission(mActivity, "拍照需要打开您的拍照权限", new PermissonUtils.OnPermissionAgreeListener() {
                    @Override
                    public void onAgree() {
                        PictureSelector.create(ChatActivity.this)
                                .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                                .compress(true)// 是否压缩 true or false
                                .forResult(PictureConfig.REQUEST_CAMERA);
                    }

                    @Override
                    public void onRefuse() {

                    }
                }, Manifest.permission.CAMERA);
            }
        });
        //相册
        tvGallery.setOnClickListener(v -> PictureSelector.create(ChatActivity.this)//这里在fragment里就传fragment  activity里传activty  传什么取决于回调哪里的onActivityResult
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(9)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST));
        //视频
        tvTakeVideo.setOnClickListener(v -> {
            if (PermissonUtils.hasPermissions(mActivity, Manifest.permission.CAMERA)) {
                PictureSelector.create(ChatActivity.this)
                        .openCamera(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                        .compress(true)// 是否压缩 true or false
                        .videoQuality(0)
                        .recordVideoSecond(10)//视频秒数录制 默认60s int
                        .previewVideo(true)//预览视频
                        .forResult(PictureConfig.TYPE_VIDEO);
            } else {
                PermissonUtils.requestPermission(mActivity, "拍摄视频需要打开您的拍照权限", new PermissonUtils.OnPermissionAgreeListener() {
                    @Override
                    public void onAgree() {
                        PictureSelector.create(ChatActivity.this)
                                .openCamera(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                                .compress(true)// 是否压缩 true or false
                                .videoQuality(1)
                                .recordVideoSecond(10)//视频秒数录制 默认60s int
                                .previewVideo(true)//预览视频
                                .forResult(PictureConfig.TYPE_VIDEO);
                    }

                    @Override
                    public void onRefuse() {

                    }
                }, Manifest.permission.CAMERA);
            }
        });
        //文件
        tvFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("未开放");
            }
        });
    }

    /**
     * 加载更多
     */
    private void getMoreData() {
        if (mFirstId != 0) {
            showMore = true;
            mPresenter.getData(mChannel_id, mUuid, 25, mFirstId);
        }
    }


    @Override
    protected void initData() {
        getData();
    }

    /**
     * 获取聊天记录
     */
    private void getData() {
        mPresenter.getData(mChannel_id, mUuid, 25, mFirstId);
    }

    @Override
    public void showData(String data) {
        if (data != null) {
            try {
                JSONArray jsonArray = new JSONArray(data);
                if (jsonArray.length() > 0) {
                    for (int i = jsonArray.length() - 1; i >= 0; i--) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String body = jsonObject.optString("body");
                        mFirstId = jsonArray.optJSONObject(jsonArray.length() - 1).optInt("id");
                        JSONArray authorIds = jsonObject.optJSONArray("author_id");
                        JSONArray attachmenIds = jsonObject.optJSONArray("attachment_ids");
                        String date = jsonObject.optString("date");
                        //校验有无附件
                        checkAttachments(authorIds, body, date, attachmenIds);
                    }
                } else {
                    ToastUtils.showShort("没有更多记录了~");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //是否从报告跳转过来
        if (mFromReport) {
            if (mReports != null && mReports.size() > 0) {
                for (ReportDetailBean.ReportsBean bean : mReports) {
                    //发送pdf
                    mPresenter.sendReportMsg(mUuid, "[报告]" + bean.getName() + "," + bean.getUrl(), mPartner_id, bean);
                }
            }
            mFromReport = false;
        }
    }

    /**
     * 发送文字
     *
     * @param bean
     */
    @Override
    public void showSendMsgWithText(SendMsgBean bean) {
        if (bean != null) {
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setType(ChatEntity.SEND_TEXT);
            chatEntity.setContent(mInput);
            chatEntity.setDate(TimeUtils.millis2String(System.currentTimeMillis()));
            chatEntity.setSendAvatar(ChatConstant.CHAT_AVATAR_URL + mPartner_id + "/image");
            mChatAdapter.addData(chatEntity);
            //发送成功,清除
            mInput = "";
            etChatInput.setText("");
            scrollToBottom(true);
        }
    }

    /**
     * 发送报告消息
     *
     * @param bean
     */
    @Override
    public void showSendReportMsg(SendMsgBean bean, ReportDetailBean.ReportsBean reportsBean) {
        if (bean != null) {
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setType(ChatEntity.SEND_REPORT);
            chatEntity.setContent("[报告]" + reportsBean.getName() + "," + reportsBean.getUrl());
            chatEntity.setDate(TimeUtils.millis2String(System.currentTimeMillis()));
            chatEntity.setSendAvatar(ChatConstant.CHAT_AVATAR_URL + mPartner_id + "/image");
            mChatAdapter.addData(chatEntity);
            scrollToBottom(true);
        }
    }


    /**
     * 发送附件
     *
     * @param bean
     */
    @Override
    public void showSendMsgWithAttachment(SendMsgBean sendMsgBean, UploadAttachmentBean bean) {
        if (bean != null) {
            String mimetype = bean.getMimetype();
            if (mimetype != null) {
                if (mimetype.contains("image")) {
                    //图片
                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setType(ChatEntity.SEND_IMG);
                    chatEntity.setUrl(ChatConstant.CHAT_IMAGE_URL + bean.getId());
                    chatEntity.setDate(TimeUtils.millis2String(System.currentTimeMillis()));
                    chatEntity.setSendAvatar(ChatConstant.CHAT_AVATAR_URL + mPartner_id + "/image");
                    mChatAdapter.addData(chatEntity);
                } else if (mimetype.contains("audio")) {
                    //语音
                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setType(ChatEntity.SEND_VOICE);
                    chatEntity.setUrl(ChatConstant.CHAT_IMAGE_URL + bean.getId());
                    chatEntity.setDate(TimeUtils.millis2String(System.currentTimeMillis()));
                    if (!StringUtils.isEmpty(bean.getDescription())) {
                        chatEntity.setDuration(Integer.parseInt(bean.getDescription()));
                    }
                    chatEntity.setSendAvatar(ChatConstant.CHAT_AVATAR_URL + mPartner_id + "/image");
                    mChatAdapter.addData(chatEntity);
                } else if (mimetype.contains("video")) {
                    //视频
                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setType(ChatEntity.SEND_VIDEO);
                    chatEntity.setUrl(ChatConstant.CHAT_IMAGE_URL + bean.getId());
                    chatEntity.setDate(TimeUtils.millis2String(System.currentTimeMillis()));
                    if (!StringUtils.isEmpty(bean.getDescription())) {
                        chatEntity.setDuration(Integer.parseInt(bean.getDescription()));
                    }
                    chatEntity.setSendAvatar(ChatConstant.CHAT_AVATAR_URL + mPartner_id + "/image");
                    mChatAdapter.addData(chatEntity);
                }
                scrollToBottom(true);
            }
        }
    }

    /**
     * 上传附件
     *
     * @param bean
     */
    @Override
    public void showUploadAttachment(UploadAttachmentBean bean) {
        if (bean != null) {
            Log.e(TAG, bean.toString());
            int id = bean.getId();
            mPresenter.sendMsgWithAttachment(mUuid, "[" + id + "]", mPartner_id, bean);
        }
    }


    /**
     * 判断是否有附件
     *
     * @param attachmenIds
     */
    private void checkAttachments(JSONArray authorIds, String body, String date, JSONArray attachmenIds) {
        if (attachmenIds.length() > 0) {
            //有附件
            if (authorIds.length() > 0) {
                int authorId = (int) authorIds.opt(0);
                if (authorId == mPartner_id) {
                    //发送
                    checkWithAttachmentSend(attachmenIds, date, authorId);
                } else {
                    //接收
                    checkWithAttachmentReceive(attachmenIds, date, authorId);
                }
            }
        } else {
            //无附件
            if (authorIds.length() > 0) {
                int authorId = (int) authorIds.opt(0);
                if (authorId == mPartner_id) {
                    //发送
                    if (body.contains("[报告]")) {
                        //pdf
                        setTextMsg(ChatEntity.SEND_REPORT, true, body, date, authorId);
                    } else {
                        setTextMsg(ChatEntity.SEND_TEXT, true, body, date, authorId);
                    }
                } else {
                    //接收
                    if (body.contains("[报告]")) {
                        //pdf
                        setTextMsg(ChatEntity.RECEIVE_REPORT, false, body, date, authorId);
                    } else {
                        setTextMsg(ChatEntity.RECEIVE_TEXT, false, body, date, authorId);
                    }
                }
            }
        }
        if (!showMore) {
            mChatAdapter.setNewData(mList);
            //滚到底部
            if (mChatAdapter.getItemCount() >= 1) {
                recyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
            }
        }
    }

    //接收
    private void checkWithAttachmentReceive(JSONArray attachmenIds, String date, int authorId) {
        for (int i = 0; i < attachmenIds.length(); i++) {
            JSONObject attachment = attachmenIds.optJSONObject(i);
            if (attachment.optString("mimetype").contains("image")) {
                //图片
                setAttachment(ChatEntity.RECEIVE_IMG, false, attachment, date, authorId);
            } else if (attachment.optString("mimetype").contains("audio")) {
                //音频
                setAttachment(ChatEntity.RECEIVE_VOICE, false, attachment, date, authorId);
            } else if (attachment.optString("mimetype").contains("video")) {
                //视频
                setAttachment(ChatEntity.RECEIVE_VIDEO, false, attachment, date, authorId);
            }
        }
    }

    //发送
    private void checkWithAttachmentSend(JSONArray attachmenIds, String date, int authorId) {
        for (int i = 0; i < attachmenIds.length(); i++) {
            JSONObject attachment = attachmenIds.optJSONObject(i);
            if (attachment.optString("mimetype").contains("image")) {
                //图片
                setAttachment(ChatEntity.SEND_IMG, true, attachment, date, authorId);
            } else if (attachment.optString("mimetype").contains("audio")) {
                //音频
                setAttachment(ChatEntity.SEND_VOICE, true, attachment, date, authorId);
            } else if (attachment.optString("mimetype").contains("video")) {
                //视频
                setAttachment(ChatEntity.SEND_VIDEO, true, attachment, date, authorId);
            }
        }
    }

    //处理文字
    private void setTextMsg(int type, boolean send, String body, String date, int authorId) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setType(type);
        if (send) {
            chatEntity.setSendAvatar(ChatConstant.CHAT_AVATAR_URL + mPartner_id + "/image");
        } else {
            chatEntity.setReceiveAvatar(ChatConstant.CHAT_AVATAR_URL + authorId + "/image");
        }
        chatEntity.setContent(body);
        chatEntity.setDate(TimeUtils.millis2String(TimeUtils.string2MillisAdd8Hours(date)));
        if (showMore) {
            mChatAdapter.addData(0, chatEntity);
        } else {
            mList.add(chatEntity);
        }
    }

    //处理附件
    private void setAttachment(int type, boolean send, JSONObject attachment, String date, int authorId) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setType(type);
        if (send) {
            chatEntity.setSendAvatar(ChatConstant.CHAT_AVATAR_URL + mPartner_id + "/image");
        } else {
            chatEntity.setReceiveAvatar(ChatConstant.CHAT_AVATAR_URL + authorId + "/image");
        }
        String description = attachment.optString("description");
        if (StringUtils.isInteger(description)) {
            chatEntity.setDuration(Integer.parseInt(attachment.optString("description")));
        }
        chatEntity.setUrl(ChatConstant.CHAT_IMAGE_URL + attachment.optInt("id"));
        chatEntity.setDate(TimeUtils.millis2String(TimeUtils.string2MillisAdd8Hours(date)));
        if (showMore) {
            mChatAdapter.addData(0, chatEntity);
        } else {
            mList.add(chatEntity);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                case PictureConfig.CHOOSE_REQUEST:
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    selectList = PictureSelector.obtainMultipleResult(data);
                    Log.e("selectList_size", selectList.size() + "");
                    for (LocalMedia localMedia : selectList) {
                        final String path = localMedia.getCompressPath();
                        Log.e("selectListpath", path);
                        Log.e("length", localMedia.getCompressPath().length() + "");
                        File file = new File(path);
                        //上传图片
                        mPresenter.uploadAttatchment("mail.channel", mChannel_id, file, file.getName(), "image/jpg");
                    }
                    break;
                case PictureConfig.TYPE_VIDEO:
                    // 拍摄视频结果回调
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    LocalMedia localMedia = localMedias.get(0);
                    String path = localMedia.getPath();
                    //获取的是毫秒数，需要四舍五入
                    long duration = localMedia.getDuration();
                    Log.e("video_path", path);
                    Log.e("video_length", path.length() + "");
                    Log.e("video_duration", duration + "");
                    //上传video
                    mPresenter.uploadAttatchment("mail.channel", mChannel_id, new File(path), duration / 1000 + "", "video/mp4");
                    break;
            }
        }
    }

    /**
     * 旋转
     *
     * @param from
     * @param to
     */
    private void animToRotate(int from, int to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivChatMore, "rotation", from, to);
        animator.setDuration(500);
        animator.setInterpolator(new AnticipateOvershootInterpolator(1));
        animator.start();
    }

    //是否上滑取消
    private boolean isCancelled(MotionEvent event, float startY) {
        float moveY = event.getY();
        int instance = (int) (moveY - startY);
        Log.e("voiceList", "--action move--instance:" + instance);
        if (instance < -300) {
            return true;
        }
        return false;
    }


    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
    }

    //初始化语音
    private void initAudioRecordManager() {
        AudioRecordManager.getInstance(mActivity).setMaxVoiceDuration(120);//设置最大语音时间
        if (PermissonUtils.hasPermissions(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // 用户已经同意该权限
            initAudio();
        } else {
            PermissonUtils.requestPermission(mActivity, "录音需要您的读写权限", new PermissonUtils.OnPermissionAgreeListener() {
                @Override
                public void onAgree() {
                    initAudio();
                }

                @Override
                public void onRefuse() {

                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        AudioRecordManager.getInstance(mActivity).setAudioRecordListener(new IAudioRecordListener() {

            private TextView mTimerTV;
            private TextView mStateTV;
            private ImageView mStateIV;
            private PopupWindow mRecordWindow;

            /**
             * 初始化提示视图
             */
            @Override
            public void initTipView() {
                View view = View.inflate(mActivity, R.layout.popup_audio_wi_vo, null);
                mStateIV = (ImageView) view.findViewById(R.id.rc_audio_state_image);
                mStateTV = (TextView) view.findViewById(R.id.rc_audio_state_text);
                mTimerTV = (TextView) view.findViewById(R.id.rc_audio_timer);
                mRecordWindow = new PopupWindow(view, -1, -1);
                mRecordWindow.showAtLocation(recyclerView, 18, 0, 0);
                mRecordWindow.setFocusable(true);
                mRecordWindow.setOutsideTouchable(false);
                mRecordWindow.setTouchable(false);
            }

            /**
             * 设置倒计时提示视图
             *
             * @param counter 10秒倒计时
             */
            @Override
            public void setTimeoutTipView(int counter) {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.GONE);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
                    this.mTimerTV.setVisibility(View.VISIBLE);
                }
            }

            /**
             * 设置正在录制提示视图
             */
            @Override
            public void setRecordingTipView() {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.drawable.ic_volume_1);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.corner_voice_style_record_shape);
                    this.mTimerTV.setVisibility(View.GONE);
                }
            }

            /**
             * 设置语音长度太短提示视图
             */
            @Override
            public void setAudioShortTipView() {
                if (this.mRecordWindow != null) {
                    mStateIV.setImageResource(R.drawable.ic_volume_wraning);
                    mStateTV.setText(R.string.voice_short);
                }
            }

            /**
             * 设置取消提示视图
             */
            @Override
            public void setCancelTipView() {
                if (this.mRecordWindow != null) {
                    this.mTimerTV.setVisibility(View.GONE);
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.drawable.ic_volume_cancel);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_cancel);
                    this.mStateTV.setBackgroundResource(R.drawable.corner_voice_shape);
                }
            }

            /**
             * 销毁提示视图
             */
            @Override
            public void destroyTipView() {
                if (this.mRecordWindow != null) {
                    this.mRecordWindow.dismiss();
                    this.mRecordWindow = null;
                    this.mStateIV = null;
                    this.mStateTV = null;
                    this.mTimerTV = null;
                }
            }

            /**
             * 开始录制
             * 如果是做IM的话，这里可以发送一个消息，如：对方正在讲话
             */
            @Override
            public void onStartRecord() {
            }

            /**
             * 录制结束
             *
             * @param audioPath 语音文件路径
             * @param duration  语音文件时长
             */
            @Override
            public void onFinish(final Uri audioPath, final int duration) {
                //发送文件
                if (audioPath != null) {
                    final File rawFile = new File(audioPath.getPath());
                    AndroidAudioConverter.with(mActivity)
                            // Your current audio file
                            .setFile(rawFile)
                            // Your desired audio format
                            .setFormat(AudioFormat.MP3)
                            // An callback to know when conversion is finished
                            .setCallback(new IConvertCallback() {
                                @Override
                                public void onSuccess(File file) {
                                    //上传语音
                                    mPresenter.uploadAttatchment("mail.channel", mChannel_id, file, duration + "", "audio/mp3");
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    e.printStackTrace();
                                    ToastUtils.showShort("语音格式转换失败！");
                                }
                            })
                            // Start conversion
                            .convert();
                }
            }

            /**
             * 分贝改变
             *
             * @param db 分贝
             */
            @Override
            public void onAudioDBChanged(int db) {
                switch (db / 5) {
                    case 0:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_1);
                        break;
                    case 1:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_2);
                        break;
                    case 2:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_3);
                        break;
                    case 3:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_4);
                        break;
                    case 4:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_5);
                        break;
//                    case 5:
//                        this.mStateIV.setImageResource(R.drawable.ic_volume_6);
//                        break;
//                    case 6:
//                        this.mStateIV.setImageResource(R.drawable.ic_volume_7);
//                        break;
                    default:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_5);
                }
            }
        });
    }

    /**
     * 初始化语音一些变量
     */
    private void initAudio() {
        File audioDir = new File(ChatConstant.AUDIO_SAVE_DIR);
        if (!audioDir.exists()) {
            audioDir.mkdirs();
            Log.e("audiodir", audioDir + "");
        }
        AudioRecordManager.getInstance(mActivity).setAudioSavePath(audioDir.getAbsolutePath());
    }

    @Override
    public void onBackPressed() {
        if (recyclerView != null) {
            KeyBoardUtils.hideSoftInput(recyclerView, mActivity);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (handler != null)
            handler.removeCallbacksAndMessages(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
