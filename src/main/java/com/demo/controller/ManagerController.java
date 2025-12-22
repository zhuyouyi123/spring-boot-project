package com.demo.controller;

import com.demo.common.pojo.dto.manager.ManagerAddDTO;
import com.demo.service.ManagerService;
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

    @GetMapping("list")
    public RespVO<Void> list() {
        return RespVO.success();
    }
}
