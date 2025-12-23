package com.demo.common.pojo.dto.manager;

import com.dev.common.pojo.dto.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ManagerPageDTO extends BasePageDTO {

    private String keyword;
}
