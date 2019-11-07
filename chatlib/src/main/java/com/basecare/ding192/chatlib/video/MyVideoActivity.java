package com.basecare.ding192.chatlib.video;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.basecare.ding192.chatlib.R;
import com.basecare.ding192.chatlib.R2;

import butterknife.BindView;
import cn.dr.basemvp.base.BaseActivity;
import cn.dr.basemvp.glide.GlideUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by dingrui 2019/11/6
 */

public class MyVideoActivity extends BaseActivity {

    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.jz_video)
    JzvdStd jzVideo;
    private String mVideo_url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_video;
    }

    @Override
    protected void getIntent(Intent intent) {
        mVideo_url = intent.getStringExtra("video_url");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void initData() {
        jzVideo.setUp(mVideo_url, "");
        GlideUtils.loadImage(mActivity, jzVideo.thumbImageView, mVideo_url, 0);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

}
