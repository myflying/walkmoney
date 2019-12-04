package com.ydys.moneywalk.model;


import com.ydys.moneywalk.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface CashRecordInfoModel<T> {
    void cashRecordList(String userId, int page ,IBaseRequestCallBack<T> iBaseRequestCallBack);
}
