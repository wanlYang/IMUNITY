/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.google.code.kaptcha.Producer;
import com.wanl.controller.KaptchaController;
import com.wanl.redis.RedisCacheManager;
import com.wanl.utils.CookieUtil;
import com.wanl.utils.UUIDUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KaptchaController {
    @Autowired
    private Producer captchaProducer;
    @Autowired
    private RedisCacheManager redisCacheManager;

    @RequestMapping({ "/get/kaptcha/image" })
    public void getKaptchaCodeImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0L);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = this.captchaProducer.createText();
        String cookie = CookieUtil.getCookie(request, "adminVerifyCode", false);
        if (cookie != null) {
            this.redisCacheManager.del(new String[] { cookie });
            CookieUtil.delCookie(response, request, "adminVerifyCode");
        }
        String uid = UUIDUtils.getUid();
        this.redisCacheManager.set(uid, capText, 300L);
        CookieUtil.addCookie(response, "adminVerifyCode", uid, Integer.valueOf(300), false);
        BufferedImage bufferedImage = this.captchaProducer.createImage(capText);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        try {
            outputStream.flush();
        } finally {
            outputStream.close();
        }
    }
}
