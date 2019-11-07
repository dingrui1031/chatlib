package com.basecare.ding192.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dingrui 2019/11/6
 */

public class SendMsgBean {

    /**
     * data : {"id":474335,"uuid":"40dac5fe-34a0-492d-8265-7d2e27f65dd7"}
     * session_info : {"user_context":{},"expiration_reason":"trial","name":false,"db":"lims","inbox_action":88,"web.base.url":"http://192.168.10.185:8069","fcm_project_id":"","uid":null,"username":false,"user_companies":false,"expiration_date":"2049-10-18 03:08:30","warning":false,"partner_id":null,"max_time_between_keys_in_ms":55,"is_system":false,"device_subscription_ids":[],"company_id":null,"is_superuser":false,"session_id":"62665b31f0600a56b86727e92c8de0576158ce67"}
     */

    private DataBean data;
    private SessionInfoBean session_info;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public SessionInfoBean getSession_info() {
        return session_info;
    }

    public void setSession_info(SessionInfoBean session_info) {
        this.session_info = session_info;
    }

    public static class DataBean {
        /**
         * id : 474335
         * uuid : 40dac5fe-34a0-492d-8265-7d2e27f65dd7
         */

        private int id;
        private String uuid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }

    public static class SessionInfoBean {
        /**
         * user_context : {}
         * expiration_reason : trial
         * name : false
         * db : lims
         * inbox_action : 88
         * web.base.url : http://192.168.10.185:8069
         * fcm_project_id :
         * uid : null
         * username : false
         * user_companies : false
         * expiration_date : 2049-10-18 03:08:30
         * warning : false
         * partner_id : null
         * max_time_between_keys_in_ms : 55
         * is_system : false
         * device_subscription_ids : []
         * company_id : null
         * is_superuser : false
         * session_id : 62665b31f0600a56b86727e92c8de0576158ce67
         */

        private UserContextBean user_context;
        private String expiration_reason;
        private boolean name;
        private String db;
        private int inbox_action;
        @SerializedName("web.base.url")
        private String _$WebBaseUrl66; // FIXME check this code
        private String fcm_project_id;
        private Object uid;
        private boolean username;
        private boolean user_companies;
        private String expiration_date;
        private boolean warning;
        private Object partner_id;
        private int max_time_between_keys_in_ms;
        private boolean is_system;
        private Object company_id;
        private boolean is_superuser;
        private String session_id;
        private List<?> device_subscription_ids;

        public UserContextBean getUser_context() {
            return user_context;
        }

        public void setUser_context(UserContextBean user_context) {
            this.user_context = user_context;
        }

        public String getExpiration_reason() {
            return expiration_reason;
        }

        public void setExpiration_reason(String expiration_reason) {
            this.expiration_reason = expiration_reason;
        }

        public boolean isName() {
            return name;
        }

        public void setName(boolean name) {
            this.name = name;
        }

        public String getDb() {
            return db;
        }

        public void setDb(String db) {
            this.db = db;
        }

        public int getInbox_action() {
            return inbox_action;
        }

        public void setInbox_action(int inbox_action) {
            this.inbox_action = inbox_action;
        }

        public String get_$WebBaseUrl66() {
            return _$WebBaseUrl66;
        }

        public void set_$WebBaseUrl66(String _$WebBaseUrl66) {
            this._$WebBaseUrl66 = _$WebBaseUrl66;
        }

        public String getFcm_project_id() {
            return fcm_project_id;
        }

        public void setFcm_project_id(String fcm_project_id) {
            this.fcm_project_id = fcm_project_id;
        }

        public Object getUid() {
            return uid;
        }

        public void setUid(Object uid) {
            this.uid = uid;
        }

        public boolean isUsername() {
            return username;
        }

        public void setUsername(boolean username) {
            this.username = username;
        }

        public boolean isUser_companies() {
            return user_companies;
        }

        public void setUser_companies(boolean user_companies) {
            this.user_companies = user_companies;
        }

        public String getExpiration_date() {
            return expiration_date;
        }

        public void setExpiration_date(String expiration_date) {
            this.expiration_date = expiration_date;
        }

        public boolean isWarning() {
            return warning;
        }

        public void setWarning(boolean warning) {
            this.warning = warning;
        }

        public Object getPartner_id() {
            return partner_id;
        }

        public void setPartner_id(Object partner_id) {
            this.partner_id = partner_id;
        }

        public int getMax_time_between_keys_in_ms() {
            return max_time_between_keys_in_ms;
        }

        public void setMax_time_between_keys_in_ms(int max_time_between_keys_in_ms) {
            this.max_time_between_keys_in_ms = max_time_between_keys_in_ms;
        }

        public boolean isIs_system() {
            return is_system;
        }

        public void setIs_system(boolean is_system) {
            this.is_system = is_system;
        }

        public Object getCompany_id() {
            return company_id;
        }

        public void setCompany_id(Object company_id) {
            this.company_id = company_id;
        }

        public boolean isIs_superuser() {
            return is_superuser;
        }

        public void setIs_superuser(boolean is_superuser) {
            this.is_superuser = is_superuser;
        }

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public List<?> getDevice_subscription_ids() {
            return device_subscription_ids;
        }

        public void setDevice_subscription_ids(List<?> device_subscription_ids) {
            this.device_subscription_ids = device_subscription_ids;
        }

        public static class UserContextBean {
        }
    }
}
