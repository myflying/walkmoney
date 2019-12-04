package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

public class TaskInfo {
    @SerializedName("task_id")
    private String taskId;
    @SerializedName("task_type")
    private String taskType;
    private String title;//任务名称
    private String describe;//任务描述
    private String pic;//任务图标
    private int gold;//任务金币
    @SerializedName("has_complete_num")
    private int hasCompleteNum;//任务已完成数量
    private int num;//任务数量
    private int state;//任务状态 1去完成 2领取金币 3已完成

    private int taskState;//任务状态，1,未完成，2，已完成，3倒计时

    private int countDownSecond;//倒计时的S数

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getHasCompleteNum() {
        return hasCompleteNum;
    }

    public void setHasCompleteNum(int hasCompleteNum) {
        this.hasCompleteNum = hasCompleteNum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public int getCountDownSecond() {
        return countDownSecond;
    }

    public void setCountDownSecond(int countDownSecond) {
        this.countDownSecond = countDownSecond;
    }
}
