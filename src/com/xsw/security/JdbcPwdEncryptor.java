package com.xsw.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.xsw.utils.Encrypt;
import com.xsw.utils.Util;

public class JdbcPwdEncryptor extends PropertyPlaceholderConfigurer {
    private static final String LIKE_JDBC_PASSWORD_KEY = ".password";
    private static final String JDBC_ENCRYPT_KEY = "encrypt";

    private static Logger log = Logger.getLogger(JdbcPwdEncryptor.class);

    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) {
        String encryptPwd = "";
        String deEncrypt = "";
        Map<String, String> pwdMap = new HashMap<String, String>();
        try {
            for (Object key : props.keySet()) {
                if (key.toString().endsWith(LIKE_JDBC_PASSWORD_KEY)) {
                    encryptPwd = props.getProperty(key.toString());
                    pwdMap.put((String) key, encryptPwd);
                }
            }
            String encrypt = props.getProperty(JDBC_ENCRYPT_KEY);
            if (!Util.isEmpty(encrypt) && "true".equals(encrypt)) {
                log.info("The current jdbc password is encrypted.");
                for (String key : pwdMap.keySet()) {
                    encryptPwd = pwdMap.get(key);
                    if (!Util.isEmpty(encryptPwd)) {
                        deEncrypt = Encrypt.decodeString(encryptPwd);
                        log.info("Decrypted jdbc password [" + key + ":" + encryptPwd + "] successful.");
                        props.setProperty(key, deEncrypt);
                    }
                }
            } else {
                log.info("The current jdbc password is not encrypted password: [" + pwdMap.values() + "]");
            }
        } catch (Exception e) {
            log.warn("Decryptied jdbc password failure： [" + encryptPwd + "]", e);
        }
        super.processProperties(beanFactory, props);
    }
}
