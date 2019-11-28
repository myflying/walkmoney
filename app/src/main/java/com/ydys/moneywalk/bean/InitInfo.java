package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

public class InitInfo {

    @SerializedName("app_config")
    private AppConfig appConfig;

    @SerializedName("new_task_config")
    private NewTaskConfig newTaskConfig;

    @SerializedName("user_data")
    private UserStepData userStepData;

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public NewTaskConfig getNewTaskConfig() {
        return newTaskConfig;
    }

    public void setNewTaskConfig(NewTaskConfig newTaskConfig) {
        this.newTaskConfig = newTaskConfig;
    }

    public UserStepData getUserStepData() {
        return userStepData;
    }

    public void setUserStepData(UserStepData userStepData) {
        this.userStepData = userStepData;
    }

    public class AppConfig {
        @SerializedName("invite_rule")
        private String inviteRule;
        @SerializedName("share_pic")
        private String sharePic;//分享底图
        private String privacy;////隐私政策（链接）
        private String agreement;//用户协议（链接）
        private String strategy;//赚钱攻略（图片）
        private String mobile;//联系方式
        @SerializedName("app_logo")
        private String appLogo;//应用logo
        @SerializedName("app_name")
        private String appName;//应用名称

        public String getInviteRule() {
            return inviteRule;
        }

        public void setInviteRule(String inviteRule) {
            this.inviteRule = inviteRule;
        }

        public String getSharePic() {
            return sharePic;
        }

        public void setSharePic(String sharePic) {
            this.sharePic = sharePic;
        }

        public String getPrivacy() {
            return privacy;
        }

        public void setPrivacy(String privacy) {
            this.privacy = privacy;
        }

        public String getAgreement() {
            return agreement;
        }

        public void setAgreement(String agreement) {
            this.agreement = agreement;
        }

        public String getStrategy() {
            return strategy;
        }

        public void setStrategy(String strategy) {
            this.strategy = strategy;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAppLogo() {
            return appLogo;
        }

        public void setAppLogo(String appLogo) {
            this.appLogo = appLogo;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }
    }

    public class NewTaskConfig {
        private int gold;//新人红包金币
        @SerializedName("is_del")
        private int isDel;

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getIsDel() {
            return isDel;
        }

        public void setIsDel(int isDel) {
            this.isDel = isDel;
        }
    }

    public class UserStepData{
        @SerializedName("step_num")
        private int stepNum;

        public int getStepNum() {
            return stepNum;
        }

        public void setStepNum(int stepNum) {
            this.stepNum = stepNum;
        }
    }

}
