package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.constants.login.LoginConstants;
import com.demo.common.enums.ManagerRoleEnum;
import com.demo.common.enums.errors.ManagerError;
import com.demo.common.pojo.dataobject.ManagerDO;
import com.demo.common.pojo.dto.manager.ManagerAddDTO;
import com.demo.common.pojo.dto.manager.ManagerEditDTO;
import com.demo.common.pojo.dto.manager.ManagerPageDTO;
import com.demo.common.pojo.vo.manager.ManagerVO;
import com.demo.common.utils.SmCryptoUtil;
import com.demo.mapper.ManagerMapper;
import com.demo.service.ManagerService;
import com.dev.common.pojo.vo.PageVO;
import com.dev.common.utils.RandomNameGenerator;
import com.dev.common.utils.RandomPhoneGenerator;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, ManagerDO> implements ManagerService{

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
        ManagerDO managerDO = getByUsernameOrPhone(username);
        if (managerDO != null) {
            throw ManagerError.USER_ALREADY_EXISTS.build();
        }
        String password = SmCryptoUtil.sm2Encrypt(LoginConstants.LOGIN_DEFAULT_PASSWORD);
        managerMapper.insert(ManagerDO.builder()
                .username(username)
                .password(password)
                .phone(dto.getPhone())
                .role(ManagerRoleEnum.getByCode(dto.getRole()).orElse(ManagerRoleEnum.USER).getCode())
                .locked(false)
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build());
    }

    /**
     * 修改管理用户信息
     *
     * @param dto 用户信息
     */
    @Override
    public void update(ManagerEditDTO dto) {
        ManagerDO managerDO = managerMapper.selectById(dto.getId());
        if (Objects.isNull(managerDO)){
            throw ManagerError.USER_NOT_EXIST.build();
        }
        ManagerDO usernameManagerDO = getByUsernameOrPhone(dto.getUsername());
        if (Objects.nonNull(usernameManagerDO) && !Objects.equals(managerDO.getId(), usernameManagerDO.getId())){
            throw ManagerError.USER_ALREADY_EXISTS.build();
        }
        managerMapper.updateById(ManagerDO.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .phone(dto.getPhone())
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
    public ManagerDO getByUsernameOrPhone(String username) {
        return managerMapper.selectOne(new LambdaQueryWrapper<ManagerDO>().eq(ManagerDO::getUsername, username));
    }

    /**
     * 查询管理用户信息
     *
     * @param dto 分页参数
     * @return 管理用户信息
     */
    @Override
    public PageVO<ManagerVO> page(ManagerPageDTO dto) {
        Page<ManagerDO> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        LambdaQueryWrapper<ManagerDO> wrapper = new LambdaQueryWrapper<ManagerDO>()
                .like(ManagerDO::getUsername, dto.getKeyword())
                .or()
                .like(ManagerDO::getPhone, dto.getKeyword());
        Page<ManagerDO> managerDOPage = managerMapper.selectPage(page, wrapper);
        return PageVO.of((int) managerDOPage.getCurrent(), (int) managerDOPage.getSize(), managerDOPage.getTotal(),
                Optional.ofNullable(managerDOPage.getRecords())
                        .orElse(Collections.emptyList())
                        .stream().map(managerDO -> ManagerVO.builder()
                                .id(managerDO.getId())
                                .username(managerDO.getUsername())
                                .phone(managerDO.getPhone())
                                .role(ManagerRoleEnum.getByCode(managerDO.getRole()).orElse(ManagerRoleEnum.USER).getCode() + "")
                                .roleDesc(ManagerRoleEnum.getByCode(managerDO.getRole()).orElse(ManagerRoleEnum.USER).getDesc())
                                .createTime(managerDO.getCreatedTime())
                                .build())
                        .collect(Collectors.toList()));
    }

    @Override
    public void deleteById(Long id) {
//        ManagerDO managerDO = managerMapper.selectById(id);
//        if (Objects.isNull(managerDO)) {
//            return;
//        }
//        if (managerDO.getRole() == ManagerRoleEnum.ADMIN.getCode()) {
//            throw ManagerError.DELETE_ADMIN_NOT_ALLOWED.build();
//        }
//        managerMapper.deleteById(id);

       generateTestPerson();
    }



    /**
     * 批量删除管理用户信息
     *
     * @param ids 管理用户id
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        List<ManagerDO> doList = managerMapper.selectBatchIds(ids);
        boolean existAdmin = doList.stream().anyMatch(e -> e.getRole() == ManagerRoleEnum.ADMIN.getCode());
        if (existAdmin) {
            throw ManagerError.DELETE_ADMIN_NOT_ALLOWED.build();
        }
        managerMapper.deleteByIds(ids);
    }

    private void generateTestPerson() {
        List<String> strings = new ArrayList<>(RandomNameGenerator.generateUniqueNames(10000));
        List<String> phoneList = new ArrayList<>(RandomPhoneGenerator.generateUniquePhones(10000));
        List<ManagerDO> managerDOList = new ArrayList<>();

        for (int i = 0; i < strings.size(); i++) {
            managerDOList.add(ManagerDO.builder()
                    .username( strings.get( i))
                    .password(SmCryptoUtil.sm2Encrypt(LoginConstants.LOGIN_DEFAULT_PASSWORD))
                    .phone(phoneList.get( i))
                    .role(ManagerRoleEnum.USER.getCode())
                    .locked(false)
                    .createdTime(LocalDateTime.now())
                    .updatedTime(LocalDateTime.now())
                    .build());
        }

        saveBatch(managerDOList);
    }

}
