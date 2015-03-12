package com.xsw.views;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.xsw.ctx.AppCtx;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2015-03-11
 * @description JXLS 插件导出XLS数据的封装类，并同时继承了Spring MVC的抽象视图类用于文件流的统一输出。
 *
 */

public class JxlsExcelView extends AbstractView {
    private static String contentType = "application/vnd.ms-excel; charset=UTF-8";
    private static String extension = ".xls";

    public JxlsExcelView() {
        setContentType(contentType);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Workbook workbook;
        AppCtx appctx = (AppCtx) getApplicationContext().getBean("appctx");

        LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
        Locale userLocale = RequestContextUtils.getLocale(request);
        String jxlTemp = appctx.getParam("jxlTemplates");
        String fileName = (String) map.get("templateName");
        Resource inputFile = helper.findLocalizedResource(jxlTemp + File.separator + fileName, extension, userLocale);

        XLSTransformer transformer = new XLSTransformer();
        workbook = transformer.transformXLS(inputFile.getInputStream(), map);

        response.reset();
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=0");
        response.setContentType(getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + extension + "\"");

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
}
