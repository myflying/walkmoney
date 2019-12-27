package com.ydys.elsbballs.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/1/25.
 */
public class UserInfo {
    private String id;
    private String mobile;
    private String nickname;//用户昵称
    private String face;//用户头像
    private int gold;//用户金币
    private double amount;//用户金币对应金额
    private int status;//账号状态(1正常，2封号)
    @SerializedName("cash_out_status")
    private int cashOutStatus;//提现封禁状态 1可以提现 2禁止提现
    @SerializedName("is_bind")
    private int isBind; //该设备是否绑定微信或者手机号 1是 0否

    @SerializedName("bind_wechat")
    private int bindWechat;//是否绑定微信 1是 0否

    @SerializedName("bind_mobile")
    private int bindMobile;//是否绑定手机号 1是 0否

    @SerializedName("new_user_task_config")
    private NewUserTaskConfig newUserTaskConfig;

    @SerializedName("login_task_config")
    private LoginTaskConfig loginTaskConfig;

    @SerializedName("new_user_tx")
    private int newUserTx;//是否已使用新人专享提现 1是 0否

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCashOutStatus() {
        return cashOutStatus;
    }

    public void setCashOutStatus(int cashOutStatus) {
        this.cashOutStatus = cashOutStatus;
    }

    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public int getBindWechat() {
        return bindWechat;
    }

    public void setBindWechat(int bindWechat) {
        this.bindWechat = bindWechat;
    }

    public int getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(int bindMobile) {
        this.bindMobile = bindMobile;
    }

    public NewUserTaskConfig getNewUserTaskConfig() {
        return newUserTaskConfig;
    }

    public void setNewUserTaskConfig(NewUserTaskConfig newUserTaskConfig) {
        this.newUserTaskConfig = newUserTaskConfig;
    }

    public LoginTaskConfig getLoginTaskConfig() {
        return loginTaskConfig;
    }

    public void setLoginTaskConfig(LoginTaskConfig loginTaskConfig) {
        this.loginTaskConfig = loginTaskConfig;
    }

    public int getNewUserTx() {
        return newUserTx;
    }

    public void setNewUserTx(int newUserTx) {
        this.newUserTx = newUserTx;
    }

    public class NewUserTaskConfig {
        private String id;//任务id
        private String title;//新人福利",
        private String type;//
        @SerializedName("is_double")
        private int isDouble;//
        @SerializedName("use_weight_config")
        private int useWeightConfig;//
        @SerializedName("new_user_gold")
        private int newUserGold;//是否已经领取新人红包 1已经领取 0未领取
        private double money;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getIsDouble() {
            return isDouble;
        }

        public void setIsDouble(int isDouble) {
            this.isDouble = isDouble;
        }

        public int getUseWeightConfig() {
            return useWeightConfig;
        }

        public void setUseWeightConfig(int useWeightConfig) {
            this.useWeightConfig = useWeightConfig;
        }

        public int getNewUserGold() {
            return newUserGold;
        }

        public void setNewUserGold(int newUserGold) {
            this.newUserGold = newUserGold;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }
    }

    public class LoginTaskConfig {
        private String id;//任务id
        private String title;//新人福利",
        private String type;//
        @SerializedName("is_double")
        private int isDouble;//
        @SerializedName("use_weight_config")
        private int useWeightConfig;//
        @SerializedName("login_task_gold")
        private int loginTaskGold;//今天是否已经领取登录任务红包 1已经领取 0未领取

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getIsDouble() {
            return isDouble;
        }

        public void setIsDouble(int isDouble) {
            this.isDouble = isDouble;
        }

        public int getUseWeightConfig() {
            return useWeightConfig;
        }

        public void setUseWeightConfig(int useWeightConfig) {
            this.useWeightConfig = useWeightConfig;
        }

        public int getLoginTaskGold() {
            return loginTaskGold;
        }

        public void setLoginTaskGold(int loginTaskGold) {
            this.loginTaskGold = loginTaskGold;
        }
    }

}

