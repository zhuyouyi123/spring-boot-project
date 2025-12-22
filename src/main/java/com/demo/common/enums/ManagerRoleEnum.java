package com.demo.common.enums;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public enum ManagerRoleEnum {

    ADMIN(1, "管理员"),

    USER(2, "普通用户");

    private final int code;
    private final String desc;

    public static Optional<ManagerRoleEnum> getByCode(Integer role) {
        for (ManagerRoleEnum value : values()) {
            if (value.code == role) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
