package com.demo.common.enums.errors;

import com.dev.common.exception.BusinessException;
import com.dev.common.exception.IBusinessExceptionBuilder;
import com.dev.constants.ErrorConstants;

public enum LoginError implements IBusinessExceptionBuilder {
    CAPTCHA_ERROR {
        @Override
        public BusinessException build(Object... formatArgs) {
            return BusinessException.of(ErrorConstants.LOGIN_ERROR + 1, "验证码错误", formatArgs);
        }
    },

    USER_NOT_EXIST {
        @Override
        public BusinessException build(Object... formatArgs) {
            return BusinessException.of(ErrorConstants.LOGIN_ERROR + 2, "用户不存在", formatArgs);
        }
    },

    LOGIN_FAILED {
        @Override
        public BusinessException build(Object... formatArgs) {
            return BusinessException.of(ErrorConstants.LOGIN_ERROR + 3, "登录失败", formatArgs);
        }
    },

}
