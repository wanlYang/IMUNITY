/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.entity.CommunityTheme;
import com.wanl.mapper.CommunityThemeMapper;
import com.wanl.service.ThemeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SwitchingDataSource
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    private CommunityThemeMapper communityThemeMapper;

    public List<CommunityTheme> getThemeList() {
        return this.communityThemeMapper.findList();
    }

    public CommunityTheme getTheme(Integer themeId) {
        CommunityTheme theme = null;
        if (themeId != null) {
            theme = this.communityThemeMapper.findThemeById(themeId);
        }
        return theme;
    }

    public Integer getCount() {
        return this.communityThemeMapper.getAllCount();
    }
}
