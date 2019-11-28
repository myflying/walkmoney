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

}
