package com.demo.controller;

import com.demo.common.pojo.dto.manager.ManagerAddDTO;
import com.demo.common.pojo.dto.manager.ManagerBatchDeleteDTO;
import com.demo.common.pojo.dto.manager.ManagerEditDTO;
import com.demo.common.pojo.dto.manager.ManagerPageDTO;
import com.demo.common.pojo.vo.manager.ManagerVO;
import com.demo.service.ManagerService;
import com.dev.common.pojo.vo.PageVO;
import com.dev.common.pojo.vo.RespVO;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("manager")
public class ManagerController {

    @Resource
    private ManagerService managerService;

    @PostMapping("add")
    public RespVO<Void> add(@RequestBody @Validated ManagerAddDTO dto) {
        managerService.add(dto);
        return RespVO.success();
    }

    @PutMapping("update")
    public RespVO<Void> update(@RequestBody @Validated ManagerEditDTO dto) {
        managerService.update(dto);
        return RespVO.success();
    }

    @GetMapping("page")
    public RespVO<PageVO<ManagerVO>> page(@Validated ManagerPageDTO dto) {
        PageVO<ManagerVO> page = managerService.page(dto);
        return RespVO.success(page);
    }

    @DeleteMapping("{id}")
    public RespVO<Void> delete(@PathVariable String id) {
        managerService.deleteById(Long.valueOf(id));
        return RespVO.success();
    }

    @PostMapping("batch-delete")
    public RespVO<Void> delete(@RequestBody @Validated ManagerBatchDeleteDTO dto) {
        managerService.deleteByIds(dto.getIds());
        return RespVO.success();
    }
}
