package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionCategory {
    private String id;
    private String name;//问题类别名称
    private int sort;
    @SerializedName("question_list")
    private List<QuestionInfo> questionInfoList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<QuestionInfo> getQuestionInfoList() {
        return questionInfoList;
    }

    public void setQuestionInfoList(List<QuestionInfo> questionInfoList) {
        this.questionInfoList = questionInfoList;
    }
}
