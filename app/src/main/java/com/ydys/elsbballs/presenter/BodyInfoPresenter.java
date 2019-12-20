package com.ydys.elsbballs.presenter;

/**
 * Created by admin on 2017/4/7.
 */

public interface BodyInfoPresenter {
    void getBodyInfo(String userId);

    void updateBodyInfo(String userId, String filed, String value);
}
