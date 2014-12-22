package com.xsw.utils;

import java.util.UUID;

public class Util {

    public static String trim(String val) {
        if (val == null) {
            return "";
        }
        return val.trim();
    }

    public static boolean isEmpty(String val) {
        if ("".equals(Util.trim(val))) {
            return true;
        }
        return false;
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid2() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
