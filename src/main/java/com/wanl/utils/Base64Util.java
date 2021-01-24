/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {
    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static String decoder(String data) {
        byte[] dataBytes = null;
        try {
            dataBytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoder.encodeToString(dataBytes);
    }
}
