/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.constant.ImunityConstant;
import com.wanl.entity.Article;
import com.wanl.entity.CommunityTheme;
import com.wanl.entity.User;
import com.wanl.mapper.ArticleMapper;
import com.wanl.service.ArticleService;
import com.wanl.service.ThemeService;
import com.wanl.service.UserService;
import com.wanl.utils.SpringSecurityUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SwitchingDataSource
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ThemeService themeService;

    @Transactional(rollbackFor = { Exception.class })
    public Integer addArticle(Article article) {
        Integer row = Integer.valueOf(0);
        if (article != null) {
            Authentication securityAuthentication = SpringSecurityUtil.getSecurityAuthentication();
            if (securityAuthentication.getPrincipal() instanceof User) {
                User user = (User) securityAuthentication.getPrincipal();
                User findUser = this.userService.getUser(user.getId());
                if (findUser != null) {
                    article.setUser(user);
                }
            }
            article.setReplyCount(Integer.valueOf(0));
            article.setClickCount(Integer.valueOf(0));
            article.setLastReplyUser(null);
            article.setLastReplyTime(null);
            CommunityTheme theme = null;
            if (article.getTheme() != null && article.getTheme().getThemeId() != null) {
                theme = this.themeService.getTheme(article.getTheme().getThemeId());
            }
            article.setTheme(theme);
            if (article.getIsTop() == null) {
                article.setIsTop(Integer.valueOf(0));
            }
            row = this.articleMapper.create(article);
        }
        return row;
    }

    public List<Article> getArticleList(Integer page, Integer limit) {
        return this.articleMapper.findList((page.intValue() - 1) * limit.intValue(), limit);
    }

    public Integer getAllCount() {
        return this.articleMapper.getCount();
    }

    public Integer editArticle(Article article) {
        Integer row = Integer.valueOf(0);
        if (article != null && article.getId() != null) {

            CommunityTheme theme = null;
            if (article.getTheme() != null && article.getTheme().getThemeId() != null) {
                theme = this.themeService.getTheme(article.getTheme().getThemeId());
            }
            article.setTheme(theme);
            if (article.getIsTop() == null) {
                article.setIsTop(Integer.valueOf(0));
            }
            row = this.articleMapper.update(article);
        }
        return row;
    }

    public Integer delArticle(Integer id) {
        Integer row = Integer.valueOf(0);
        if (id != null) {
            row = this.articleMapper.del(id);
        }
        return row;
    }

    @Transactional(rollbackFor = { Exception.class })
    public Integer delArticleBatch(Integer[] newsId) {
        Integer row = Integer.valueOf(0);
        if (newsId != null) {
            for (Integer integer : newsId) {
                row = Integer.valueOf(row.intValue() + delArticle(integer).intValue());
            }
        }
        return row;
    }

    @Transactional(rollbackFor = { Exception.class })
    public Integer changeIsTop(Integer id) {
        Integer row = Integer.valueOf(0);
        if (id != null) {
            Article article = this.articleMapper.getArticle(id);
            if (article != null) {
                if (article.getIsTop().intValue() == 0) {
                    article.setIsTop(ImunityConstant.IS_TOP);
                } else if (article.getIsTop().intValue() == 1) {
                    article.setIsTop(ImunityConstant.NOT_TOP);
                }
                row = editArticle(article);
            }
        }
        return row;
    }
}
