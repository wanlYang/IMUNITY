/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.entity.BackStageNav;
import com.wanl.entity.Permission;
import com.wanl.entity.Role;
import com.wanl.entity.RolePermission;
import com.wanl.mapper.BackStageNavMapper;
import com.wanl.service.BackStageNavService;
import com.wanl.service.RoleService;
import com.wanl.utils.SpringContextUtil;
import com.wanl.utils.SpringSecurityUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SwitchingDataSource("mysqlConfigDataSource")
@Service
public class BackStageNavServiceImpl implements BackStageNavService {
    @Autowired
    private BackStageNavMapper adminNavMapper;
    @Autowired
    private RoleService roleService;

    public List<BackStageNav> getAllTopLevel() {
        String authorities = SpringSecurityUtil.getUserAuthorities().toString();
        List<BackStageNav> list = this.adminNavMapper.getTopLevel(0);
        if (authorities.contains("ROLE_SUPER_ADMIN")) {
            navigationPrivilegeProcessing(list, "ROLE_SUPER_ADMIN");
        } else if (authorities.contains("ROLE_ADMIN")) {
            navigationPrivilegeProcessing(list, "ROLE_ADMIN");
        }
        return list;
    }

    private void navigationPrivilegeProcessing(List<BackStageNav> list, String switchRole) {
        if (list != null) {
            Iterator<BackStageNav> iterator = list.iterator();
            while (iterator.hasNext()) {
                BackStageNav backStageNav = (BackStageNav) iterator.next();
                if (backStageNav.getRolesString() != null && backStageNav.getRolesString().length() > 0) {
                    String rolesString = backStageNav.getRolesString();
                    String[] roles = rolesString.split(",");
                    List<Role> listRole = new ArrayList<Role>();
                    for (String roleString : roles) {
                        Role role = this.roleService.getRoleById(Integer.valueOf(Integer.parseInt(roleString)));
                        listRole.add(role);
                    }
                    List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
                    for (Role role : listRole) {
                        grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
                    }
                    String string = grantedAuthorities.toString();
                    if (switchRole.equals("ROLE_SUPER_ADMIN")) {
                        if (!string.contains("ROLE_SUPER_ADMIN"))
                            iterator.remove();
                        continue;
                    }
                    if (switchRole.equals("ROLE_ADMIN") && !string.contains("ROLE_ADMIN")) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    public List<BackStageNav> getSecondLevel(Integer menuId) {
        if (menuId != null) {
            List<BackStageNav> allNav = getAllNav();
            return treeBackStageNavs(allNav, menuId, false);
        }

        return null;
    }

    public List<BackStageNav> getNav(Integer id) {
        if (id != null) {
            return this.adminNavMapper.getNav(id);
        }
        return null;
    }

    public List<BackStageNav> getAllNav(Integer page, Integer limit) {
        List<BackStageNav> backStageNavs = null;
        if (page != null && limit != null) {
            backStageNavs = this.adminNavMapper.findAllNav(Integer.valueOf((page.intValue() - 1) * limit.intValue()),
                    limit);
        }
        return backStageNavs;
    }

    public Integer getNavCount() {
        return this.adminNavMapper.findNavCount();
    }

    public List<BackStageNav> getAllTopLevel(Integer flag) {
        List<BackStageNav> topNav = new ArrayList<BackStageNav>();
        if (flag != null && flag.intValue() == 2) {
            List<BackStageNav> topLevel = this.adminNavMapper.getTopLevel(0);
            topNav.addAll(topLevel);
            if (topLevel != null && !topLevel.isEmpty()) {
                Iterator<BackStageNav> iterator = topLevel.iterator();
                while (iterator.hasNext()) {
                    BackStageNav backStageNav = (BackStageNav) iterator.next();
                    List<BackStageNav> secondLevel = this.adminNavMapper.getSecondLevel(backStageNav.getNavId());
                    topNav.addAll(secondLevel);
                }
            }
        }
        return topNav;
    }

    public Integer addNav(BackStageNav backStageNav, boolean isAutoAddPerm) {
        Integer row = null;
        if (backStageNav != null) {
            if (backStageNav.getParentId().intValue() == 0) {
                List<BackStageNav> list = this.adminNavMapper.getTopLevel(0);
                if (list.size() >= 8) {
                    return Integer.valueOf(-2);
                }
            }
            if (isAutoAddPerm) {
                Permission permission = new Permission();
                permission.setName("DEFAULT_ROLE_NAME");
                permission.setDescritpion("默认描述");
                if (StringUtils.isNotBlank(backStageNav.getHref())) {
                    if (backStageNav.getHref().startsWith("/")) {
                        permission.setUrl(backStageNav.getHref());
                    } else {
                        permission.setUrl("admin/manager/" + backStageNav.getHref());
                    }
                }
                permission.setPid(0);
                Integer addNav = ((BackStageNavServiceImpl) SpringContextUtil.getBean(getClass())).addNav(backStageNav);
                ((BackStageNavServiceImpl) SpringContextUtil.getBean(getClass())).addNavPermission(permission,
                        backStageNav.getRolesString());
                return addNav;
            }
            row = this.adminNavMapper.createNav(backStageNav);
        }
        return row;
    }

    @SwitchingDataSource("mysqlConfigDataSource")
    protected Integer addNav(BackStageNav backStageNav) {
        return this.adminNavMapper.createNav(backStageNav);
    }

    public BackStageNav getBackStageNav(Integer id) {
        BackStageNav backStageNav = null;
        if (id != null) {
            backStageNav = this.adminNavMapper.findNavById(id);
        }
        return backStageNav;
    }

    public Integer delBackStageNav(Integer id) {
        Integer row = null;
        if (id != null) {
            List<BackStageNav> findAllNav = this.adminNavMapper.findAll();
            List<BackStageNav> child = treeBackStageNavs(findAllNav, id, true);
            if (child != null) {
                return Integer.valueOf(-2);
            }
            row = this.adminNavMapper.delete(id);
        }
        return row;
    }

    public List<BackStageNav> treeBackStageNavs(List<BackStageNav> backStageNavs, Integer parentId, boolean isSuper) {
        List<BackStageNav> childNavList = new ArrayList<BackStageNav>();
        Iterator<BackStageNav> iterator = backStageNavs.iterator();
        while (iterator.hasNext()) {
            BackStageNav backStageNav = (BackStageNav) iterator.next();
            if (backStageNav.getParentId().intValue() != 0
                    && backStageNav.getParentId().intValue() == parentId.intValue()) {
                childNavList.add(backStageNav);
            }
        }

        if (!isSuper) {
            String authorities = SpringSecurityUtil.getUserAuthorities().toString();
            if (authorities.contains("ROLE_SUPER_ADMIN")) {
                navigationPrivilegeProcessing(childNavList, "ROLE_SUPER_ADMIN");
            } else if (authorities.contains("ROLE_ADMIN")) {
                navigationPrivilegeProcessing(childNavList, "ROLE_ADMIN");
            }
        }
        for (BackStageNav backStageNav : childNavList) {
            if (backStageNav.getHref().equals("0")) {
                backStageNav.setChildren(treeBackStageNavs(backStageNavs, backStageNav.getNavId(), isSuper));
            }
        }
        if (childNavList.size() == 0) {
            return null;
        }
        return childNavList;
    }

    public List<BackStageNav> getAllNav() {
        return this.adminNavMapper.findAll();
    }

    public Integer editNav(BackStageNav backStageNav) {
        Integer row = null;
        if (backStageNav != null) {
            row = this.adminNavMapper.updateNav(backStageNav);
        }
        return row;
    }

    @SwitchingDataSource("mysqlDeveDataSource")
    public List<Permission> getNavRoleList(Integer page, Integer limit) {
        List<Permission> permissions = null;
        if (page != null && limit != null) {
            permissions = this.adminNavMapper
                    .findAllNavPermission(Integer.valueOf((page.intValue() - 1) * limit.intValue()), limit);
        }
        return permissions;
    }

    @SwitchingDataSource("mysqlDeveDataSource")
    public Integer getNavPermissionCount() {
        return this.adminNavMapper.findNavPermissionCount();
    }

    @Transactional(rollbackFor = { Exception.class })
    @SwitchingDataSource("mysqlDeveDataSource")
    public boolean addNavPermission(Permission permission, String rolesString) {
        if (permission != null && StringUtils.isNotBlank(rolesString)) {
            Integer row = this.adminNavMapper.createNavPermission(permission);
            if (row.intValue() > 0) {
                String[] split = rolesString.split(",");
                if (split.length != 0) {
                    List<Role> roleList = new ArrayList<Role>();
                    for (String roleId : split) {
                        Role role = this.roleService.getRoleById(Integer.valueOf(Integer.parseInt(roleId)));
                        roleList.add(role);
                    }
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setPermission(permission);
                    if (roleList != null) {
                        Integer rowRolePerm = Integer.valueOf(0);
                        for (Role role : roleList) {
                            rolePermission.setRole(role);
                            rowRolePerm = Integer.valueOf(rowRolePerm.intValue()
                                    + this.adminNavMapper.insertRolePermission(rolePermission).intValue());
                        }
                        if (rowRolePerm.intValue() > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @SwitchingDataSource("mysqlDeveDataSource")
    @Transactional(rollbackFor = { Exception.class })
    public boolean delNavPermission(Integer id) {
        if (id != null) {
            Permission permission = this.adminNavMapper.getNavPermission(id);
            if (permission != null && permission.getId() != 2) {
                this.adminNavMapper.delNavRolePermission(Integer.valueOf(permission.getId()));
                Integer row = this.adminNavMapper.delNavPermission(Integer.valueOf(permission.getId()));
                if (row.intValue() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @SwitchingDataSource("mysqlDeveDataSource")
    public boolean editNavPermission(Permission permission, String rolesString) {
        if (permission != null && StringUtils.isNotBlank(rolesString)) {
            Permission findPermission = this.adminNavMapper.getNavPermission(Integer.valueOf(permission.getId()));
            int num = 2;
            if (findPermission != null && findPermission.getId() != num) {
                Integer row = this.adminNavMapper.updateNavPermission(permission);
                if (row.intValue() > 0) {
                    String[] split = rolesString.split(",");
                    if (split.length != 0) {
                        List<Role> roleList = new ArrayList<Role>();
                        for (String roleId : split) {
                            Role role = this.roleService.getRoleById(Integer.valueOf(Integer.parseInt(roleId)));
                            roleList.add(role);
                        }
                        this.adminNavMapper.delNavRolePermission(Integer.valueOf(permission.getId()));
                        RolePermission rolePermission = new RolePermission();
                        rolePermission.setPermission(permission);
                        if (roleList != null) {
                            Integer rowRolePerm = Integer.valueOf(0);
                            for (Role role : roleList) {
                                rolePermission.setRole(role);
                                rowRolePerm = Integer.valueOf(rowRolePerm.intValue()
                                        + this.adminNavMapper.insertRolePermission(rolePermission).intValue());
                            }
                            if (rowRolePerm.intValue() > 0) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
