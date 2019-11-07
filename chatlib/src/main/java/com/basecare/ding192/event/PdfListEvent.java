package com.basecare.ding192.event;

import com.basecare.ding192.data.entity.ReportDetailBean;

import java.util.List;


/**
 * Created by dingrui 2019/11/7
 */

public class PdfListEvent {

    private List<ReportDetailBean.ReportsBean> mList;

    public PdfListEvent(List<ReportDetailBean.ReportsBean> list) {
        mList = list;
    }

    public List<ReportDetailBean.ReportsBean> getList() {
        return mList;
    }

    public void setList(List<ReportDetailBean.ReportsBean> list) {
        mList = list;
    }
}
