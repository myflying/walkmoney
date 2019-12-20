package com.ydys.elsbballs.bean;

import com.google.gson.annotations.SerializedName;

public class QuestionInfo {

    @SerializedName("question_type_id")
    private String questionTypeId;
    private String title;//问题标题
    private String content;//问题解答

    private boolean isShow;

    public String getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(String questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
