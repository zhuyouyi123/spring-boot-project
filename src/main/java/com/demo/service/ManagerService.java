package com.demo.service;

import com.demo.common.pojo.dataobject.ManagerDO;
import com.demo.common.pojo.dto.manager.ManagerAddDTO;
import com.demo.common.pojo.dto.manager.ManagerEditDTO;
import com.demo.common.pojo.dto.manager.ManagerPageDTO;
import com.demo.common.pojo.vo.manager.ManagerVO;
import com.dev.common.pojo.vo.PageVO;

import java.util.List;

public interface ManagerService {

    /**
     * 添加管理用户信息
     *
     * @param dto 用户信息
     */
    void add(ManagerAddDTO dto);

    /**
     * 修改管理用户信息
     *
     * @param dto 用户信息
     */
    void update(ManagerEditDTO dto);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    ManagerDO getByUsernameOrPhone(String username);

    /**
     * 查询管理用户信息
     * @param dto 分页参数和 关键字
     * @return 管理用户信息
     */
    PageVO<ManagerVO> page(ManagerPageDTO dto);

    /**
     * 删除管理用户信息
     * @param id 管理用户id
     */
    void deleteById(Long id);

    /**
     * 批量删除管理用户信息
     * @param ids 管理用户id
     */
    void deleteByIds(List<Long> ids);

}
