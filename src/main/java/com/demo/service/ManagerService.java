package com.demo.service;

import com.demo.common.pojo.dataobject.ManagerDO;
import com.demo.common.pojo.dto.manager.ManagerAddDTO;

public interface ManagerService {

    /**
     * 添加管理用户信息
     *
     * @param dto 用户信息
     */
    void add(ManagerAddDTO dto);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    ManagerDO getByUsername(String username);

}
