package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;
import com.ydys.elsbballs.bean.TakeGoldInfo;

/**
 * Created by admin on 2017/4/7.
 */

public interface TakeGoldInfoModel<T> {
    void takeLuckGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void takeStepGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void takeStageGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void takeTaskGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
