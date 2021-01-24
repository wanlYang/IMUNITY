/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.OpenUser;
import com.wanl.entity.User;
import com.wanl.entity.UserQueryParam;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    Integer createUser(User paramUser);

    User findByUserName(@Param("username") String paramString);

    List<User> findAllUserList();

    List<User> findAllUserListByPage(@Param("start") Integer paramInteger1, @Param("limit") Integer paramInteger2);

    Integer getUserCount();

    User getUserById(@Param("id") Integer paramInteger);

    Integer usableUser(@Param("id") Integer paramInteger1, @Param("status") Integer paramInteger2);

    Integer deleteUser(@Param("id") Integer paramInteger);

    Integer updateUser(User paramUser);

    Integer findCountByUsername(@Param("username") String paramString);

    Integer findCountByEmail(@Param("email") String paramString);

    List<User> findAllUserListByPageAndKey(@Param("start") Integer paramInteger1, @Param("limit") Integer paramInteger2,
            UserQueryParam paramUserQueryParam);

    Integer getUserCountByParam(UserQueryParam paramUserQueryParam);

    Integer updateHeadImg(@Param("fileUrl") String paramString, @Param("id") Integer paramInteger);

    User findUserByOpenId(@Param("openId") String paramString1, @Param("openType") String paramString2);

    Integer createOpenUser(OpenUser paramOpenUser);

    OpenUser getOpenUserById(@Param("userId") String paramString1, @Param("openType") String paramString2);

    Integer cancelOpenUser(@Param("id") Integer paramInteger, @Param("openType") String paramString);

    OpenUser findOpenUserByOpenIdAndUserId(@Param("openId") String paramString1, @Param("id") Integer paramInteger,
            @Param("openType") String paramString2);

    OpenUser findOpenUserByUserId(@Param("id") Integer paramInteger, @Param("openType") String paramString);

    Integer updateOpenUserByUidAndOpenId(OpenUser paramOpenUser);

    OpenUser findOpenUserByOpenId(@Param("openId") String paramString1, @Param("openType") String paramString2);

    Integer updatePassword(@Param("newPass") String paramString, @Param("id") Integer paramInteger);
}

