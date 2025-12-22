package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.common.constants.login.LoginConstants;
import com.demo.common.enums.ManagerRoleEnum;
import com.demo.common.enums.errors.ManagerError;
import com.demo.common.pojo.dataobject.ManagerDO;
import com.demo.common.pojo.dto.manager.ManagerAddDTO;
import com.demo.common.utils.SmCryptoUtil;
import com.demo.mapper.ManagerMapper;
import com.demo.service.ManagerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Resource
    private ManagerMapper managerMapper;

    /**
     * 添加管理用户信息
     *
     * @param dto 用户信息
     */
    @Override
    public void add(ManagerAddDTO dto) {
        String username = dto.getUsername();
        ManagerDO managerDO = getByUsername(username);
        if (managerDO != null) {
            throw ManagerError.USER_NOT_EXIST.build();
        }
        String password = SmCryptoUtil.sm2Encrypt(LoginConstants.LOGIN_DEFAULT_PASSWORD);
        managerMapper.insert(ManagerDO.builder()
                .username(username)
                .password(password)
                .role(ManagerRoleEnum.USER.getCode())
                .locked(false)
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build());
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public ManagerDO getByUsername(String username) {
        return managerMapper.selectOne(new LambdaQueryWrapper<ManagerDO>().eq(ManagerDO::getUsername, username));
    }


}
