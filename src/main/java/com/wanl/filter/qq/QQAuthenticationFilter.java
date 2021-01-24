/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.filter.qq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanl.config.MerchantConfig;
import com.wanl.entity.OpenUser;
import com.wanl.entity.QQUser;
import com.wanl.entity.User;
import com.wanl.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QQAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private static MerchantConfig merchantConfig;
    @SuppressWarnings("unused")
    private static final Logger logger = LogManager.getLogger(QQAuthenticationFilter.class.getName());

    private final UserService userService;

    private static final String CODE = "code";

    private final Pattern pattern;

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken));
        String code = request.getParameter(CODE);
        String tokenAccessApi = String.format(merchantConfig.getTOKENACCESSAPI(),
                merchantConfig.getACCESSTOKEN_URI(), merchantConfig.getGRANTTYPE(), merchantConfig.getCLIENTID(), merchantConfig.getCLIENTSECRET(), code, merchantConfig.getREDIRECTURI());
        QQAccessToken qqToken = getToken(tokenAccessApi);
        if (qqToken != null) {
            String openId = getOpenId(qqToken.getAccessToken());
            if (openId != null) {
                if (!isAuthenticated) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            qqToken.getAccessToken(), openId);
                    return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
                }
                Object principal = authentication.getPrincipal();
                if (principal instanceof User) {
                    User principalUser = (User) principal;
                    User user = this.userService.getUser(principalUser.getId());
                    OpenUser findUserOpen = this.userService.getOpenUserByOpenIdAndUserId(openId, user.getId(), "QQ");
                    OpenUser findUserOpenByUserId = this.userService.getOpenUserByUserId(user.getId(), "QQ");
                    if (findUserOpen != null || findUserOpenByUserId != null) {
                        response.sendRedirect(request.getContextPath() + "/401?authorized");
                        return null;
                    }
                    OpenUser openUserByOpenId = this.userService.getOpenUserByOpenId(openId, "QQ");
                    if (openUserByOpenId != null) {
                        response.sendRedirect(request.getContextPath() + "/401?qqauthorized");
                        return null;
                    }
                    OpenUser openUser = new OpenUser();
                    openUser.setUser(user);
                    openUser.setOpenId(openId);
                    openUser.setOpenType("QQ");
                    QQUser qqUserInfo = getUserInfo(qqToken.getAccessToken(), openId);
                    openUser.setNickname(qqUserInfo.getNickname());
                    openUser.setAvatar(qqUserInfo.getAvatar());
                    Integer row = this.userService.authorizationOpenInfo(openUser);
                    if (row != 0) {
                        response.sendRedirect(request.getContextPath() + "/admin/open/oauth2/qq/success");
                        return null;
                    }
                }
                throw new BadCredentialsException("坏的凭据!您没有被授权!");
            }
        }
        throw new BadCredentialsException("坏的凭据!您没有被授权!");
    }

    public QQAuthenticationFilter(String defaultFilterProcessesUrl, UserService userService) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, "GET"));

        this.pattern = Pattern.compile("\"openid\":\"(.*?)\"");
        this.userService = userService;
    }

    private String getOpenId(String accessToken) throws IOException {
        String url = merchantConfig.getOPENIDURI() + accessToken;
        Document document = Jsoup.connect(url).get();
        String resultText = document.text();
        Matcher matcher = this.pattern.matcher(resultText);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private QQAccessToken getToken(String tokenAccessApi) throws IOException {
        Document document = Jsoup.connect(tokenAccessApi).get();
        String tokenResult = document.text();
        String[] results = tokenResult.split("&");
        int condition = 3;
        if (results.length == condition) {
            QQAccessToken qqToken = new QQAccessToken();
            String accessToken = results[0].replace("access_token=", "");
            int expiresIn = Integer.parseInt(results[1].replace("expires_in=", ""));
            String refreshToken = results[2].replace("refresh_token=", "");
            qqToken.setAccessToken(accessToken);
            qqToken.setExpiresIn(expiresIn);
            qqToken.setRefreshToken(refreshToken);
            return qqToken;
        }
        return null;
    }

    protected static QQUser getUserInfo(String accessToken, String openId) {
        Document document;
        String url = String.format(merchantConfig.getUSERINFOAPI(), merchantConfig.getUSERINFOURI(), accessToken, merchantConfig.getCLIENTID(), openId);

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BadCredentialsException("Bad Credentials!");
        }
        String resultText = document.text();
        JSONObject json = JSON.parseObject(resultText);
        QQUser user = new QQUser();
        user.setNickname(json.getString("nickname"));
        user.setGender(json.getString("gender"));
        user.setProvince(json.getString("province"));
        user.setYear(json.getString("year"));
        user.setAvatar(json.getString("figureurl_qq_2"));
        return user;
    }
   

}
