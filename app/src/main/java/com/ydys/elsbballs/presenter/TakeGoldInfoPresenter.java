package com.ydys.elsbballs.presenter;

import com.ydys.elsbballs.bean.TakeGoldInfo;

/**
 * Created by admin on 2017/4/7.
 */

public interface TakeGoldInfoPresenter {
    void takeLuckGold(TakeGoldInfo takeGoldInfo);

    void takeStepGold(TakeGoldInfo takeGoldInfo);

    void takeStageGold(TakeGoldInfo takeGoldInfo);

    void takeTaskGold(TakeGoldInfo takeGoldInfo);
}
