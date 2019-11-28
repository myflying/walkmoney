package com.ydys.moneywalk.presenter;

import com.ydys.moneywalk.bean.TakeGoldInfo;

/**
 * Created by admin on 2017/4/7.
 */

public interface TakeGoldInfoPresenter {
    void takeLuckGold(TakeGoldInfo takeGoldInfo);

    void takeStepGold(TakeGoldInfo takeGoldInfo);

    void takeStageGold(TakeGoldInfo takeGoldInfo);

    void takeTaskGold(TakeGoldInfo takeGoldInfo);
}
