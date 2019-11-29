package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskInfoWrapper {

    @SerializedName("sign_task")
    private SignTaskInfo signTaskInfo;

    @SerializedName("currency_task")
    private List<TaskInfo> taskList;

    public SignTaskInfo getSignTaskInfo() {
        return signTaskInfo;
    }

    public void setSignTaskInfo(SignTaskInfo signTaskInfo) {
        this.signTaskInfo = signTaskInfo;
    }

    public List<TaskInfo> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskInfo> taskList) {
        this.taskList = taskList;
    }

    public class SignTaskInfo {

        @SerializedName("continue_num")
        private int continueNum;//连续签到天数
        @SerializedName("tomorrow_gold")
        private int tomorrowGold;//明天签到可得金币

        @SerializedName("is_double")
        private int isDouble;

        @SerializedName("task_id")
        private int taskId;

        private List<SignInfo> list;

        public int getIsDouble() {
            return isDouble;
        }

        public void setIsDouble(int isDouble) {
            this.isDouble = isDouble;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public List<SignInfo> getList() {
            return list;
        }

        public void setList(List<SignInfo> list) {
            this.list = list;
        }

        public int getContinueNum() {
            return continueNum;
        }

        public void setContinueNum(int continueNum) {
            this.continueNum = continueNum;
        }

        public int getTomorrowGold() {
            return tomorrowGold;
        }

        public void setTomorrowGold(int tomorrowGold) {
            this.tomorrowGold = tomorrowGold;
        }
    }

}
