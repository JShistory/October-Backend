package com.october.back.global.exception;

import com.october.back.global.common.BusinessException;
import com.october.back.global.common.ErrorCode;

public class CrawlException extends BusinessException {
    public CrawlException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CrawlException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
