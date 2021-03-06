package com.ydys.moneywalk.common;


public class Constants {

    public static final int SUCCESS = 1;

    public static final int FAIL = 0;

    public static final String DEFAULT_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsUu+pxS1yXxGrBk2rp38" +
            "cJ6H2mNGTz//ePyms8i9FE40SoTMSM2G7YzXClXNucU6Km/6KclkpvJP3kvOhO5X" +
            "GnUjKyOC36Q+eusArNy5Lkm2mU8Bde3C08HqrAqnFSelVmKsLdcr/j5DOaiLieoZ" +
            "GxFUwXco/gpoYeMBUYYthgR9w9SxRSD7iC7Xdy0jtbl6lAYgKsneMXBdyDnGf5ai" +
            "Rn/yk5n6veKRYiDYAcKcKmj5DTypo4Sw+tYcfwzFX/Ia5kabgB1lx+AyRLZ1xFWa" +
            "T8VRKsGS1Ky/VQB3htIL6Xr9C3XmW2Iej7wiQj3kDN0t4ufT4gp6zZ7LBwaOjhOS" +
            "FQIDAQAB";

    public static final String BASE_URL = "http://zlb.zhanyu55.com/api/";

    public static final String BASE_IMAGE_URL = "http://zlb.zhanyu55.com/";

    public static final String LOCAL_GIF_URL = BASE_URL + "gifs/";

    public static final String LOCAL_GIF_FUNS_URL = BASE_URL + "giffuns/";

    public static final String LOCAL_PENDANT_URL = BASE_URL + "pendant/";

    public static final String COOL_GIF_PATH = "/cool_gif";

    public static final String USER_INFO = "login_user_info";

    public static final String AGENT_ID = "agent_id";

    public static final String GET_SYS_STEP = "get_sys_step";

    //是否同意用户协议
    public static final String IS_AGREE_PRIVACY = "is_agree_privacy";

    //当日已经兑换过的步数
    public static final String CURRENT_DAY_EXCHANGE_STEP = "current_day_exchange_step";

    //当日已经领取的阶段步数的--阶段，默认0
    public static final String IS_GET_STAGE = "is_get_state";

    //标记APP本地是否登录过账号(主要是考虑app卸载后，与后台综合判断，再次安装app时，此设备是否登录过系统)
    public static final String LOCAL_LOGIN = "local_login";

    //本次提现是否验证过手机，在提现完成后，
    public static final String THIS_CASH_VALIDATE = "this_cash_validate";
    //用户的本地体重
    public static final String USER_WEIGHT = "user_weight";
    //填写邀请码任务是否完成
    public static final String INVITE_WRITE_CODE = "invite_write_done";

    public static final String BANNER_CODE_ID = "938698144";

    public static final String VIDEO_CODE_ID = "938698384";

    public static final String MY_BANNER_CODE_ID = "938698517";

    public static final String NEW_STEP_NUM = "new_step_num";

    public static final String EVERY_DAY_START = "every_day_start";
}
