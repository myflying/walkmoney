package com.ydys.moneywalk.model;


import com.ydys.moneywalk.base.IBaseRequestCallBack;
import com.ydys.moneywalk.bean.UserStepInfo;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserStepInfoModel<T> {
    void updateStepInfo(UserStepInfo userStepInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
