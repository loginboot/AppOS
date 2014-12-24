package com.xsw.model;

import com.xsw.mapper.JsonMapper;

/**
 * 
 * @author lyodssoft.com
 * 
 * @creator Kevin
 * @version 1.0.0
 * @date 2013-09-21
 * @description Json Format - Create
 *
 */

public abstract class AbstractEntity {
    @Override
    public String toString() {
        JsonMapper mapper = new JsonMapper();
        return mapper.toJson(this);
    }
}
