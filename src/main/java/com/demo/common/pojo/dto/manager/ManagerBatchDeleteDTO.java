package com.demo.common.pojo.dto.manager;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ManagerBatchDeleteDTO {

    @NotEmpty(message = "ids不能为空")
    private List<Long> ids;

}
