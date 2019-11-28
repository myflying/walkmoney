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
        @SerializedName("is_double")
        private int isDouble;

        private List<SignInfo> list;

        public int getIsDouble() {
            return isDouble;
        }

        public void setIsDouble(int isDouble) {
            this.isDouble = isDouble;
        }

        public List<SignInfo> getList() {
            return list;
        }

        public void setList(List<SignInfo> list) {
            this.list = list;
        }
    }

}
