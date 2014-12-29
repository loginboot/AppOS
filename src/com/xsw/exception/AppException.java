package com.xsw.exception;

import com.xsw.utils.Util;

public class AppException extends Exception{
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    public AppException(String errcode) {
        super(errcode);
    }

    public AppException(int ierrcode) {
        super("ERRCODE." + Util.leftFillZero("" + ierrcode, 4));
    }
}
