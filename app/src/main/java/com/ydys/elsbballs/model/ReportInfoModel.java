package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface ReportInfoModel<T> {
    void startPlayGame(String userId, IBaseRequestCallBack<T> iBaseRequestCallBack);
    void startSeeVideo(String userId, IBaseRequestCallBack<T> iBaseRequestCallBack);

}
