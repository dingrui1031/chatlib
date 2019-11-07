package com.basecare.ding192.chatlib.photo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.basecare.ding192.chatlib.R;
import com.basecare.ding192.chatlib.R2;
import com.bm.library.PhotoView;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import cn.dr.basemvp.base.BaseActivity;
import cn.dr.basemvp.glide.GlideUtils;
import cn.dr.basemvp.utils.CommonUtils;

/**
 * Created by dingrui 2019/11/6
 */

public class SinglePhotoViewActivity extends BaseActivity {
    @BindView(R2.id.photoView)
    PhotoView photoView;
    @BindView(R2.id.iv_back)
    ImageView ivBack;
    private String mPhoto_url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_photo;
    }

    @Override
    protected void getIntent(Intent intent) {
        mPhoto_url = intent.getStringExtra("url");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //透明且黑字
        StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.transparent), 0);
        StatusBarUtil.setDarkMode(this);

    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(v -> finish());
        photoView.setOnClickListener(v -> finish());
    }

    @Override
    protected void initData() {
        // 启用图片缩放功能
        photoView.enable();
        GlideUtils.loadImage(mActivity, photoView, mPhoto_url, 0);
    }

}
