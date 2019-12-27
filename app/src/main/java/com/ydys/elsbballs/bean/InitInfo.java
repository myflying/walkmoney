package com.ydys.elsbballs.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InitInfo {

    @SerializedName("app_config")
    private AppConfig appConfig;

    @SerializedName("user_data")
    private UserStepData userStepData;

    @SerializedName("invite_config")
    private InviteConfig inviteConfig;

    @SerializedName("stage_task_finish")
    private int stageTaskFinish; //步数阶段任务是否完成 1是 0否

    @SerializedName("ad_list")
    private List<AdInfo> adList;

    public int getStageTaskFinish() {
        return stageTaskFinish;
    }

    public void setStageTaskFinish(int stageTaskFinish) {
        this.stageTaskFinish = stageTaskFinish;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public UserStepData getUserStepData() {
        return userStepData;
    }

    public void setUserStepData(UserStepData userStepData) {
        this.userStepData = userStepData;
    }

    public InviteConfig getInviteConfig() {
        return inviteConfig;
    }

    public void setInviteConfig(InviteConfig inviteConfig) {
        this.inviteConfig = inviteConfig;
    }

    public List<AdInfo> getAdList() {
        return adList;
    }

    public void setAdList(List<AdInfo> adList) {
        this.adList = adList;
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
        @SerializedName("exchange_rate")
        private int exchangeRate;

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

        public int getExchangeRate() {
            return exchangeRate;
        }

        public void setExchangeRate(int exchangeRate) {
            this.exchangeRate = exchangeRate;
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

    public class UserStepData {
        @SerializedName("step_num")
        private int stepNum;
        @SerializedName("luck_rest_num")
        private int luckRestNum;//当天幸运金币剩余领取次数

        private int weight;

        public int getStepNum() {
            return stepNum;
        }

        public void setStepNum(int stepNum) {
            this.stepNum = stepNum;
        }

        public int getLuckRestNum() {
            return luckRestNum;
        }

        public void setLuckRestNum(int luckRestNum) {
            this.luckRestNum = luckRestNum;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    public class InviteConfig {
        private int gold;//金币
        @SerializedName("is_del")
        private int isDel;//是否下架 1是 0否

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

    public class AdInfo {
        private int id;//广告id
        private String position;//广告位置 home_bottom_banner(首页横幅)，game_start_screen(游戏开屏)，gold_settle_pop(金币结算)，game_revive_video(游戏复活)，gold_double_video(金币翻倍)，
        private String code;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
