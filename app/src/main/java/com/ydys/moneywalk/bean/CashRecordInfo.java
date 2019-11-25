package com.ydys.moneywalk.bean;

public class CashRecordInfo {
    private String id;
    private int cashType;
    private long cashDate;
    private double cashMoney;
    private int cashState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCashType() {
        return cashType;
    }

    public void setCashType(int cashType) {
        this.cashType = cashType;
    }

    public long getCashDate() {
        return cashDate;
    }

    public void setCashDate(long cashDate) {
        this.cashDate = cashDate;
    }

    public double getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(double cashMoney) {
        this.cashMoney = cashMoney;
    }

    public int getCashState() {
        return cashState;
    }

    public void setCashState(int cashState) {
        this.cashState = cashState;
    }
}
