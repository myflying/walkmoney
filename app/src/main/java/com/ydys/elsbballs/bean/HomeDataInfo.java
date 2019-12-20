package com.ydys.elsbballs.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeDataInfo {

    @SerializedName("two_step_max")
    private int twoDtepMax;//二次随机步数（最大值）
    @SerializedName("two_step_min")
    private int twoStepMin;//二次随机步数（最小值）
    @SerializedName("one_step_max")
    private int oneStepMax;//初次随机步数（最大值）
    @SerializedName("one_step_min")
    private int oneStepMin;//初次随机步数（最小值）

    @SerializedName("step_num_task_config")
    public StepTaskInfo stepTaskInfo;

    @SerializedName("lucky_task_config")
    public BubbleTaskInfo bubbleTaskInfo;

    @SerializedName("stage_task_config")
    public StageTaskInfo stageTaskInfo;

    public StepTaskInfo getStepTaskInfo() {
        return stepTaskInfo;
    }

    public void setStepTaskInfo(StepTaskInfo stepTaskInfo) {
        this.stepTaskInfo = stepTaskInfo;
    }

    public BubbleTaskInfo getBubbleTaskInfo() {
        return bubbleTaskInfo;
    }

    public void setBubbleTaskInfo(BubbleTaskInfo bubbleTaskInfo) {
        this.bubbleTaskInfo = bubbleTaskInfo;
    }

    public StageTaskInfo getStageTaskInfo() {
        return stageTaskInfo;
    }

    public void setStageTaskInfo(StageTaskInfo stageTaskInfo) {
        this.stageTaskInfo = stageTaskInfo;
    }

    public int getTwoDtepMax() {
        return twoDtepMax;
    }

    public void setTwoDtepMax(int twoDtepMax) {
        this.twoDtepMax = twoDtepMax;
    }

    public int getTwoStepMin() {
        return twoStepMin;
    }

    public void setTwoStepMin(int twoStepMin) {
        this.twoStepMin = twoStepMin;
    }

    public int getOneStepMax() {
        return oneStepMax;
    }

    public void setOneStepMax(int oneStepMax) {
        this.oneStepMax = oneStepMax;
    }

    public int getOneStepMin() {
        return oneStepMin;
    }

    public void setOneStepMin(int oneStepMin) {
        this.oneStepMin = oneStepMin;
    }

    public class StepTaskInfo {
        private String id;
        private String title;//步数任务",
        private String type;
        @SerializedName("is_double")
        private int isDouble;//是否翻倍 1是 0否

        @SerializedName("gold_min")
        private int goldMin;//随机金币-最小值

        @SerializedName("gold_max")
        private int goldMax;//随机金币-最大值

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

        public int getGoldMin() {
            return goldMin;
        }

        public void setGoldMin(int goldMin) {
            this.goldMin = goldMin;
        }

        public int getGoldMax() {
            return goldMax;
        }

        public void setGoldMax(int goldMax) {
            this.goldMax = goldMax;
        }
    }

    public class BubbleTaskInfo {
        private String id;
        private String title;
        private String type;
        @SerializedName("is_double")
        private int isDouble;//是否翻倍 1是 0否
        private int num;//金币数量
        @SerializedName("gold_max")
        private int goldMax;//随机金额-最小值
        @SerializedName("gold_min")
        private int goldMin;//随机金额-最大值
        private int interval;//时间间隔

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

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getGoldMax() {
            return goldMax;
        }

        public void setGoldMax(int goldMax) {
            this.goldMax = goldMax;
        }

        public int getGoldMin() {
            return goldMin;
        }

        public void setGoldMin(int goldMin) {
            this.goldMin = goldMin;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }
    }

    public class StageTaskInfo {
        @SerializedName("is_double")
        private int isDouble;

        private List<StageInfo> list;

        public int getIsDouble() {
            return isDouble;
        }

        public void setIsDouble(int isDouble) {
            this.isDouble = isDouble;
        }

        public List<StageInfo> getList() {
            return list;
        }

        public void setList(List<StageInfo> list) {
            this.list = list;
        }
    }


    public class StageInfo {
        private int stage;//对应阶段 （整数 1-5）
        @SerializedName("step_num")
        private int stepNum;//步数
        private int gold;//金币

        public int getStage() {
            return stage;
        }

        public void setStage(int stage) {
            this.stage = stage;
        }

        public int getStepNum() {
            return stepNum;
        }

        public void setStepNum(int stepNum) {
            this.stepNum = stepNum;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }
    }
}
