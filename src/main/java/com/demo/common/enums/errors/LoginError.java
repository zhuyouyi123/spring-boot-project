package com.demo.common.enums.errors;

import com.dev.constants.ErrorConstants;
import com.dev.holder.exception.BusinessException;
import com.dev.holder.exception.IBusinessExceptionBuilder;

public enum LoginError implements IBusinessExceptionBuilder {
    CAPTCHA_ERROR {
        @Override
        public BusinessException build(Object... formatArgs) {
            return BusinessException.of(ErrorConstants.LOGIN_ERROR + 1, "验证码错误", formatArgs);
        }
    },

}
