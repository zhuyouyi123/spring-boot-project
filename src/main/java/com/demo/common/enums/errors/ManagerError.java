package com.demo.common.enums.errors;

import com.dev.common.exception.BusinessException;
import com.dev.common.exception.IBusinessExceptionBuilder;
import com.dev.constants.ErrorConstants;

public enum ManagerError implements IBusinessExceptionBuilder {

    USER_NOT_EXIST {
        @Override
        public BusinessException build(Object... formatArgs) {
            return BusinessException.of(ErrorConstants.MANAGER_ERROR + 1, "用户不存在", formatArgs);
        }
    },

    ROLE_DOES_NOT_EXIST {
        @Override
        public BusinessException build(Object... formatArgs) {
            return BusinessException.of(ErrorConstants.MANAGER_ERROR + 2, "角色不存在", formatArgs);
        }
    },

}
