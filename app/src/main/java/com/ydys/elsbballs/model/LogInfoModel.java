package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface LogInfoModel<T> {
    void addLogInfo(String userId, String adId, String adCode, String pos, String type, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
