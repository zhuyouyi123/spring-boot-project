package com.demo.common.handle;

import com.dev.web.exception.BaseExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@ControllerAdvice
@Slf4j
public class WebExceptionHandler extends BaseExceptionHandler {

}
