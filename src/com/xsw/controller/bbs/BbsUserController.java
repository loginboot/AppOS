package com.xsw.controller.bbs;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xsw.controller.BaseController;

@Controller
@RequestMapping("/bbs")
@RequiresAuthentication
public class BbsUserController extends BaseController {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(BbsUserController.class);

    public String list(HttpServletRequest request) {

        return "bbs/bbsUser";
    }
}
