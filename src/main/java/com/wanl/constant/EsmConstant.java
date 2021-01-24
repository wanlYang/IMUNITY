/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.constant;

public interface EsmConstant {
    public static final boolean DEVELOPMENT_MODEL = true;
    public static final boolean NOT_DEVELOPMENT_MODEL = false;
    public static final Integer USER_STATE_NOTACTIVE = Integer.valueOf(0);

    public static final Integer USER_STATE_ACTIVE = Integer.valueOf(1);

    public static final String SAVE_NAMEPASS = "TRUE";

    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_USER = "ROLE_USER";

    public static final Integer IS_HOT_PRODUCT = Integer.valueOf(1);

    public static final Integer IS_NOT_HOT_PRODUCT = Integer.valueOf(0);

    public static final Integer IS_UP = Integer.valueOf(1);

    public static final Integer IS_DOWN = Integer.valueOf(0);

    public static final Integer ORDER_NOTPAY = Integer.valueOf(0);

    public static final Integer ORDER_PAY = Integer.valueOf(1);

    public static final Integer ORDER_TO_BE_SHIPPED = Integer.valueOf(2);

    public static final Integer ORDER_SHIPPED = Integer.valueOf(3);

    public static final Integer ORDER_SUCCESS = Integer.valueOf(4);

    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public static final String X_REQUEST_WITH = "X-Requested-With";

    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    public static final String REQUEST_METHOD_POST = "POST";

    public static final String REQUEST_METHOD_GET = "GET";

    public static final String UTF_8 = "UTF-8";

    public static final String CONTENT_TYPE_APP_JSON_UTF_8 = "application/json;charset=utf-8";

    public static final String CONTENT_TYPR_TEXT_HTML_UTF_8 = "text/html;charset=utf-8";

    public static final String SEND_CODE_SUCCESS = "0000";

    public static final String SMS_CODE = "smsCode";

    public static final String TEMP_PHONE = "TEMP_PHONE";

    public static final String DEFAUTL_HEAD_IMG = "DEFAUTL_HEAD_IMG";

    public static final String USER_ID = "USER_ID";

    public static final String ACCOUNT_ID = "ACCOUNT_ID";

    public static final int STATUS_OK = 200;
    public static final String USER_SESSION = "USER_SESSION";
    public static final Integer CATE_SKIRT = Integer.valueOf(15);
    public static final Integer CATE_CLOTHES = Integer.valueOf(21);
    public static final Integer CATE_BOOTIES = Integer.valueOf(5);
    public static final String PAY_BALANCE = "020";
    public static final String EDIT = "EDIT";
    public static final String ADD = "ADD";
}
