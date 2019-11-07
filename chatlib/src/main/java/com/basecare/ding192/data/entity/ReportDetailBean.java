package com.basecare.ding192.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dingrui 2019/11/4
 */

public class ReportDetailBean {

    /**
     * status : 1
     * bg_date : 2018-10-30
     * reports : [{"url":"http://gy2.basecare.cn/gy/content/1797","name":"李彩金 - WES【阳性】- LT19010006 - 20190813.pdf"}]
     * health_center : 生殖中心
     * name : LT19010006
     * doctor : 刘见桥
     * sj_date : 2019-01-08 13:50:55
     * jc_date : 2018-10-10
     * id : 6
     * create_date : 2019-01-30 13:52:02
     * text : [{"doctor":"刘见桥","advise":"评估建议","literature":"参考文献","date":"2019-01-30 13:52:02","result":"本次检测发现1个与受检者临床表型相关或可能相关的变异（详细信息见下表）","introduce":"疾病介绍","remark":""}]
     */

    private int status;
    private String bg_date;
    private String health_center;
    private String name;
    private String doctor;
    private String sj_date;
    private String jc_date;
    private int id;
    private String create_date;
    private List<ReportsBean> reports;
    private List<TextBean> text;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBg_date() {
        return bg_date;
    }

    public void setBg_date(String bg_date) {
        this.bg_date = bg_date;
    }

    public String getHealth_center() {
        return health_center;
    }

    public void setHealth_center(String health_center) {
        this.health_center = health_center;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getSj_date() {
        return sj_date;
    }

    public void setSj_date(String sj_date) {
        this.sj_date = sj_date;
    }

    public String getJc_date() {
        return jc_date;
    }

    public void setJc_date(String jc_date) {
        this.jc_date = jc_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public List<ReportsBean> getReports() {
        return reports;
    }

    public void setReports(List<ReportsBean> reports) {
        this.reports = reports;
    }

    public List<TextBean> getText() {
        return text;
    }

    public void setText(List<TextBean> text) {
        this.text = text;
    }

    public static class ReportsBean implements Serializable {
        /**
         * url : http://gy2.basecare.cn/gy/content/1797
         * name : 李彩金 - WES【阳性】- LT19010006 - 20190813.pdf
         */

        private String url;
        private String name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TextBean {
        /**
         * doctor : 刘见桥
         * advise : 评估建议
         * literature : 参考文献
         * date : 2019-01-30 13:52:02
         * result : 本次检测发现1个与受检者临床表型相关或可能相关的变异（详细信息见下表）
         * introduce : 疾病介绍
         * remark :
         */

        private String doctor;
        private String advise;
        private String literature;
        private String date;
        private String result;
        private String introduce;
        private String remark;

        public String getDoctor() {
            return doctor;
        }

        public void setDoctor(String doctor) {
            this.doctor = doctor;
        }

        public String getAdvise() {
            return advise;
        }

        public void setAdvise(String advise) {
            this.advise = advise;
        }

        public String getLiterature() {
            return literature;
        }

        public void setLiterature(String literature) {
            this.literature = literature;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
