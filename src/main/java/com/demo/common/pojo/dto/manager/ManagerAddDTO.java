package com.demo.common.pojo.dto.manager;

import com.demo.common.enums.ManagerRoleEnum;
import com.demo.common.enums.errors.ManagerError;
import com.dev.common.pojo.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ManagerAddDTO extends BaseDTO {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    private String phone;

    @NotNull(message = "角色不能为空")
    private Integer role;

    @Override
    public void validate() {
        if (ManagerRoleEnum.getByCode(role).isEmpty()) {
            throw ManagerError.ROLE_DOES_NOT_EXIST.build();
        }
    }

}
