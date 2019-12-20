package com.ydys.elsbballs.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class QuestionCategoryRet extends ResultInfo {

    private List<QuestionCategory> data;

    public List<QuestionCategory> getData() {
        return data;
    }

    public void setData(List<QuestionCategory> data) {
        this.data = data;
    }
}
