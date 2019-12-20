package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;
import com.ydys.elsbballs.bean.UserStepInfo;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserStepInfoModel<T> {
    void updateStepInfo(UserStepInfo userStepInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
