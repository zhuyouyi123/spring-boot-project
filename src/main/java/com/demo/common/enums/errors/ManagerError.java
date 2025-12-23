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

    USER_ALREADY_EXISTS {
        @Override
        public BusinessException build(Object... formatArgs) {
            return BusinessException.of(ErrorConstants.MANAGER_ERROR + 2, "用户已存在", formatArgs);
        }
    },

    ROLE_DOES_NOT_EXIST {
        @Override
        public BusinessException build(Object... formatArgs) {
            return BusinessException.of(ErrorConstants.MANAGER_ERROR + 3, "角色不存在", formatArgs);
        }
    },

    DELETE_ADMIN_NOT_ALLOWED {
        @Override
        public BusinessException build(Object... formatArgs) {
            return BusinessException.of(ErrorConstants.MANAGER_ERROR + 4, "不允许删除管理员", formatArgs);
        }
    }

}
