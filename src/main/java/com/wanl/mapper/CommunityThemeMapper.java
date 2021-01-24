/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.CommunityTheme;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CommunityThemeMapper {
    List<CommunityTheme> findList();

    CommunityTheme findThemeById(@Param("themeId") Integer paramInteger);

    Integer getAllCount();
}

