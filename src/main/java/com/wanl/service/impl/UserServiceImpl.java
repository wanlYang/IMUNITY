/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.config.MerchantConfig;
import com.wanl.entity.OpenUser;
import com.wanl.entity.Role;
import com.wanl.entity.RoleUser;
import com.wanl.entity.User;
import com.wanl.entity.UserQueryParam;
import com.wanl.mapper.RoleMapper;
import com.wanl.mapper.RoleUserMapper;
import com.wanl.mapper.UserMapper;
import com.wanl.service.AccountService;
import com.wanl.service.UserService;
import com.wanl.utils.Base64Util;
import com.wanl.utils.MD5Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SwitchingDataSource
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleUserMapper roleUserMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MerchantConfig merchantConfig;

    @Transactional(rollbackFor = { Exception.class })
    public Integer addUser(User user, String stringRoles) {
        Integer row = Integer.valueOf(0);
        if (user != null && stringRoles != null && stringRoles.trim().length() > 0) {
            Integer userAccountByName = CheckUsername(user.getUsername());
            Integer userAccountByEmail = CheckEmail(user.getBindemail());
            if (userAccountByName.intValue() != 0 || userAccountByEmail.intValue() != 0) {
                return Integer.valueOf(-2);
            }
            String[] roles = stringRoles.split(",");
            String decoderPass = Base64Util.decoder(MD5Util.MD5Encode(user.getPassword()));
            user.setPassword(decoderPass);
            user.setHeadImg(merchantConfig.getDefaultUserImg());
            row = Integer.valueOf(row.intValue() + this.userMapper.createUser(user).intValue());
            row = Integer.valueOf(row.intValue() + this.accountService.addAccount(user).intValue());
            RoleUser roleUser = new RoleUser();
            roleUser.setUser(user);
            List<Role> roleList = new ArrayList<Role>();
            for (String roleId : roles) {
                Role role = this.roleMapper.getRoleById(Integer.valueOf(Integer.parseInt(roleId)));
                roleList.add(role);
            }
            for (Role role : roleList) {
                roleUser.setRole(role);
                row = Integer.valueOf(row.intValue() + this.roleUserMapper.insertRoleUser(roleUser).intValue());
            }
        }
        return Integer.valueOf((row.intValue() != 0) ? row.intValue() : 0);
    }

    public User getUserByUserName(String username) {
        User user = null;
        if (username != null && username.trim() != "") {
            user = this.userMapper.findByUserName(username);
        }
        return (user != null) ? user : null;
    }

    public List<User> getAllUserList() {
        List<User> userList = this.userMapper.findAllUserList();
        return (userList != null) ? userList : null;
    }

    public List<User> getAllUserList(Integer page, Integer limit) {
        List<User> userListByPage = null;
        if (page != null && limit != null) {
            userListByPage = this.userMapper
                    .findAllUserListByPage(Integer.valueOf((page.intValue() - 1) * limit.intValue()), limit);
        }
        return (userListByPage != null) ? userListByPage : null;
    }

    public Integer getUserCount() {
        Integer userCount = this.userMapper.getUserCount();
        return Integer.valueOf((userCount != null) ? userCount.intValue() : 0);
    }

    public Integer usableUser(Integer id, Integer status) {
        Integer row = Integer.valueOf(0);
        boolean condition = (id != null && status != null && (status.intValue() == 0 || status.intValue() == 1));
        if (condition) {
            row = this.userMapper.usableUser(id, Integer.valueOf((status.intValue() == 0) ? 1 : 0));
        }
        if (row.intValue() != 0) {
            return Integer.valueOf((status.intValue() == 0) ? 1 : 0);
        }
        return Integer.valueOf(0);
    }

    public Integer deleteUser(String[] delId, String roleSuperAdmin) {
        int row = 0;
        for (String id : delId) {
            row += deleteUser(Integer.valueOf(Integer.parseInt(id)), roleSuperAdmin).intValue();
        }
        return Integer.valueOf(row);
    }

    @Transactional(rollbackFor = { Exception.class })
    public Integer deleteUser(Integer id, String roleSuperAdmin) {
        User user = this.userMapper.getUserById(id);
        Integer row = Integer.valueOf(0);
        if (id != null && roleSuperAdmin != null) {

            this.roleUserMapper.deleteRoleUserByUserId(user.getId());

            this.accountService.delAccount(user.getId());

            this.userMapper.cancelOpenUser(user.getId(), "QQ");

            row = this.userMapper.deleteUser(user.getId());
        } else if (user != null) {
            if (user.getRoles() != null) {
                Iterator<?> iterator = user.getRoles().iterator();
                if (iterator.hasNext()) {
                    Role role = (Role) iterator.next();
                    if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_SUPER_ADMIN")) {
                        return Integer.valueOf(0);
                    }
                }

            }

            this.roleUserMapper.deleteRoleUserByUserId(user.getId());

            this.accountService.delAccount(user.getId());

            this.userMapper.cancelOpenUser(user.getId(), "QQ");

            row = this.userMapper.deleteUser(user.getId());
        }

        return Integer.valueOf((row.intValue() != 0) ? row.intValue() : 0);
    }

    @Transactional(rollbackFor = { Exception.class })
    public User editUser(User user, String stringRoles) {
        if (user != null && stringRoles != null && stringRoles.trim().length() > 0) {
            this.userMapper.updateUser(user);
            String[] roles = stringRoles.split(",");
            List<Role> roleList = new ArrayList<Role>();
            for (String roleId : roles) {
                Role role = this.roleMapper.getRoleById(Integer.valueOf(Integer.parseInt(roleId)));
                roleList.add(role);
            }
            this.roleUserMapper.deleteRoleUserByUserId(user.getId());
            RoleUser roleUser = new RoleUser();
            roleUser.setUser(user);
            for (Role role : roleList) {
                roleUser.setRole(role);
                this.roleUserMapper.insertRoleUser(roleUser);
            }
            user.setRoles(roleList);
        }
        return user;
    }

    public Integer CheckUsername(String username) {
        Integer count = Integer.valueOf(0);
        if (username != null && username.trim().length() > 0) {
            count = this.userMapper.findCountByUsername(username);
        }
        return Integer.valueOf((count.intValue() != 0) ? count.intValue() : 0);
    }

    public Integer CheckEmail(String email) {
        Integer count = Integer.valueOf(0);
        if (email != null && email.trim().length() > 0) {
            count = this.userMapper.findCountByEmail(email);
        }
        return Integer.valueOf((count.intValue() != 0) ? count.intValue() : 0);
    }

    public User editUserRole(Integer id, String stringRoles) {
        User user = null;
        if (id != null && stringRoles != null && stringRoles.trim().length() > 0) {
            String[] roles = stringRoles.split(",");
            user = this.userMapper.getUserById(id);
            List<Role> roleList = new ArrayList<Role>();
            for (String roleId : roles) {
                Role role = this.roleMapper.getRoleById(Integer.valueOf(Integer.parseInt(roleId)));
                roleList.add(role);
            }
            this.roleUserMapper.deleteRoleUserByUserId(id);
            RoleUser roleUser = new RoleUser();
            roleUser.setUser(user);
            for (Role role : roleList) {
                roleUser.setRole(role);
                this.roleUserMapper.insertRoleUser(roleUser);
            }
            user.setRoles(roleList);
        }
        return user;
    }

    public List<User> getAllUserList(Integer page, Integer limit, UserQueryParam userQueryParam) {
        List<User> userListByPage = null;
        if (page != null && limit != null) {
            userListByPage = this.userMapper.findAllUserListByPageAndKey(
                    Integer.valueOf((page.intValue() - 1) * limit.intValue()), limit, userQueryParam);
        }
        return (userListByPage != null) ? userListByPage : null;
    }

    public Integer getUserCount(UserQueryParam userQueryParam) {
        Integer userCount = this.userMapper.getUserCountByParam(userQueryParam);
        return Integer.valueOf((userCount != null) ? userCount.intValue() : 0);
    }

    public User getUser(Integer id) {
        User user = null;
        if (id != null) {
            user = this.userMapper.getUserById(id);
        }
        return user;
    }

    public User updateAdmin(User user) {
        Integer row = null;
        if (user != null) {
            row = this.userMapper.updateUser(user);
        }
        return (row != null) ? user : null;
    }

    public Integer updateAdminHeadImg(String fileUrl, Integer id) {
        boolean condition = (fileUrl != null && id != null);
        Integer row = null;
        if (condition) {
            row = this.userMapper.updateHeadImg(fileUrl, id);
        }
        return Integer.valueOf((row != null) ? row.intValue() : 0);
    }

    public User getUserByOpenId(String openId, String openType) {
        User userByOpenId = this.userMapper.findUserByOpenId(openId, openType);
        return (userByOpenId != null) ? userByOpenId : null;
    }

    public Integer authorizationOpenInfo(OpenUser openUser) {
        Integer row = null;
        if (openUser != null) {
            row = this.userMapper.createOpenUser(openUser);
        }
        return Integer.valueOf((row != null) ? row.intValue() : 0);
    }

    public boolean checkAuthorizationOpenInfo(String userId, String openType) {
        if (userId != null) {
            OpenUser openUser = this.userMapper.getOpenUserById(userId, openType);
            if (openUser != null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean cancelOpenOauth(Integer id, String openType) {
        if (id != null) {
            Integer row = this.userMapper.cancelOpenUser(id, openType);
            if (row.intValue() != 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public OpenUser getOpenUserByOpenIdAndUserId(String openId, Integer id, String openType) {
        OpenUser openUser = null;
        boolean condition = (openId != null && id != null && openType != null);
        if (condition) {
            openUser = this.userMapper.findOpenUserByOpenIdAndUserId(openId, id, openType);
        }
        return openUser;
    }

    public OpenUser getOpenUserByUserId(Integer id, String openType) {
        OpenUser openUser = null;
        boolean condition = (id != null && openType != null);
        if (condition) {
            openUser = this.userMapper.findOpenUserByUserId(id, openType);
        }
        return openUser;
    }

    public Integer updateOpenUser(OpenUser openUser) {
        Integer row = null;
        if (openUser != null) {
            row = this.userMapper.updateOpenUserByUidAndOpenId(openUser);
        }
        return Integer.valueOf((row != null) ? row.intValue() : 0);
    }

    public OpenUser getOpenUserByOpenId(String openId, String openType) {
        OpenUser openUser = null;
        if (openId != null) {
            openUser = this.userMapper.findOpenUserByOpenId(openId, openType);
        }
        return openUser;
    }

    public boolean checkOldPassword(User user, String oldPass) {
        if (user != null && oldPass != null) {
            User findUser = this.userMapper.getUserById(user.getId());
            if (findUser != null) {
                String oldPassEncode = Base64Util.decoder(MD5Util.MD5Encode(oldPass));
                if (findUser.getPassword().equals(oldPassEncode)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean changePassword(String newPass, User user) {
        boolean condition = (newPass != null && newPass.trim().length() > 0 && user != null);
        if (condition) {
            Integer row = this.userMapper.updatePassword(Base64Util.decoder(MD5Util.MD5Encode(newPass)), user.getId());
            if (row.intValue() > 0) {
                return true;
            }
        }
        return false;
    }
}
