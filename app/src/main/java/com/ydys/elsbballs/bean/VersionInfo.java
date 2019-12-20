package com.ydys.elsbballs.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangdinghui on 2019/4/25.
 */
public class VersionInfo {
    @SerializedName("force_update")
    private int forceUpdate;//是否强制更新 1是 0否
    @SerializedName("update_content")
    private String updateContent;//版本内容描述
    @SerializedName("version_code")
    private int versionCode;//版本code
    @SerializedName("version_num")
    private String versionNum;//版本号

    @SerializedName("download_url")
    private String downUrl;

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }
}
