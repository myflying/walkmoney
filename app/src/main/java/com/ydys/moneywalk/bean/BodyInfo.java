package com.ydys.moneywalk.bean;

public class BodyInfo {

    private String user_id;//用户id
    private String field;//要更新的字段名
    private String value;

    private int sex;//性别
    private int age;//年龄
    private int height;//身高
    private int weight;//体重
    private int target_step;//目标步数

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getTarget_step() {
        return target_step;
    }

    public void setTarget_step(int target_step) {
        this.target_step = target_step;
    }
}
