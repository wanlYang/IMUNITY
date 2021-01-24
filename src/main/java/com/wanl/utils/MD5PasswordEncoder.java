/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5PasswordEncoder implements PasswordEncoder {
    public String encode(CharSequence rawPassword) {
        return Base64Util.decoder(MD5Util.MD5Encode((String) rawPassword));
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(Base64Util.decoder(MD5Util.MD5Encode((String) rawPassword)));
    }
}
