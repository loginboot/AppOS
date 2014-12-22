package com.xsw.ctx;

import java.util.HashMap;
import java.util.Map;

public class AppCtx {

    private Map<String, String> params = new HashMap<String, String>();

    private Map<String, Object> objparams = new HashMap<String, Object>();

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getParam(String key) {
        return this.params.get(key);
    }

    public Map<String, Object> getObjparams() {
        return objparams;
    }

    public void setObjparams(Map<String, Object> objparams) {
        this.objparams = objparams;
    }

    public Object getObjParam(String key) {
        return objparams.get(key);
    }
}
