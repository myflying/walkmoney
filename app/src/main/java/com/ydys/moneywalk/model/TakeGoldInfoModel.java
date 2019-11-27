package com.ydys.moneywalk.model;


import com.ydys.moneywalk.base.IBaseRequestCallBack;
import com.ydys.moneywalk.bean.TakeGoldInfo;

/**
 * Created by admin on 2017/4/7.
 */

public interface TakeGoldInfoModel<T> {
    void takeLuckGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void takeStepGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void takeStageGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
