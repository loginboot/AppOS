package com.xsw.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xsw.constant.ExpStatusEnum;
import com.xsw.constant.Type;
import com.xsw.model.AtLog;
import com.xsw.service.AtLogService;
import com.xsw.utils.Servlets;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2015-01-02
 * @description 系统日志 - 统一日志管理
 *
 */

@Controller
@RequestMapping("/system")
@RequiresAuthentication
public class AtLogController extends BaseController {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(AtLogController.class);

    @Resource
    private AtLogService atLogService;

    /**
     * 跳转用户列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/atlog.do", method = RequestMethod.GET)
    @RequiresPermissions("system-atlog")
    public String list(HttpServletRequest request, Model model) {
        log.debug("get atlog list for url:[atlog.do]...");
        // 初始化分页信息
        Util.initPage(request);
        model.addAttribute("types", ExpStatusEnum.actionTypes);
        model.addAttribute("permissions", atLogService.getMenuByType(Type.MENU_MODULE));
        return "system/atlog";
    }

    /**
     * 返回用户JSON分页数据
     * @param request
     * @param response
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/atlog.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-atlog")
    public Object list(HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {
        // 排序
        Sort sort = null;
        // 查询条件
        Map<String, Object> search = Servlets.getParametersStartingWith(request, "search_");
        // 执行
        Page<AtLog> ls = atLogService.findByPage(page, pageSize, search, sort);
        // 持久化查询条件
        search.put("page", page);
        search.put("pageSize", pageSize);
        Util.storeSearchKeyValue(request, search, "search_");
        // 返回JSON数据
        return Util.writePagableJson(ls);
    }

    /**
     * 导出审计日志
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/atlog/export.do")
    @RequiresPermissions("system-atlog")
    public ModelAndView exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> search = Servlets.getParametersStartingWith(request, "search_");
        Sort sort = null;
        search.put("NOTNULL_permission", "not null");
        log.debug("export atlog xls for search:" + search);
        Page<AtLog> ls = atLogService.findByPage(1, 2000, search, sort);
        // menu and user idToName
        Map<String, String> mlst = new HashMap<String, String>();
        mlst.put("exception", Util.getMessageByCode(messageSource, request, "PUB.exception"));
        for (AtLog al : ls) {
            if (mlst.get(al.getPermission()) == null) {
                String str = atLogService.getMenuNameByPermission(al.getPermission());
                mlst.put(al.getPermission(), Util.getMessageByCode(messageSource, request, str));
            }
        }
        Map<Integer, String> types = new HashMap<Integer, String>();
        for (Integer key : ExpStatusEnum.actionTypes.keySet()) {
            types.put(key, Util.getMessageByCode(messageSource, request, ExpStatusEnum.actionTypes.get(key)));
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("templateName", "atlog"); // 模板名称
        map.put("atlogs", ls.getContent()); // bean对象
        map.put("types", types);
        map.put("mlst", mlst);
        return new ModelAndView("JxlsExcelView", map);
    }

}
