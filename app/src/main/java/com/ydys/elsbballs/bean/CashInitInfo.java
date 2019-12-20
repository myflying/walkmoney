package com.ydys.elsbballs.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CashInitInfo {
    @SerializedName("user_info")
    private UserInfo userInfo;

    @SerializedName("cash_out_config")
    private CashConfig cashOutConfig;

    @SerializedName("withdrawal_list")
    private List<CashMoneyItem> cashMoneyItems;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public CashConfig getCashOutConfig() {
        return cashOutConfig;
    }

    public void setCashOutConfig(CashConfig cashOutConfig) {
        this.cashOutConfig = cashOutConfig;
    }

    public List<CashMoneyItem> getCashMoneyItems() {
        return cashMoneyItems;
    }

    public void setCashMoneyItems(List<CashMoneyItem> cashMoneyItems) {
        this.cashMoneyItems = cashMoneyItems;
    }

    public class CashConfig {
        @SerializedName("switch")
        private int cashSwitch;////提现开关 1开 0关
        private String rule;//"提现规则"

        public int getCashSwitch() {
            return cashSwitch;
        }

        public void setCashSwitch(int cashSwitch) {
            this.cashSwitch = cashSwitch;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }
    }
}
