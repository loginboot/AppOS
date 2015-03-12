package com.xsw.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.xsw.exception.AppException;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2015-03-11
 * @description 系统文件上传控制类 - 创建
 *
 */

@Controller
@RequestMapping("/")
public class FileUploadController extends BaseController {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(FileUploadController.class);

    /**
     * 文件上传
     * 
     * @param request
     * @param resonpse
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload.do", method = RequestMethod.POST)
    public String uploadFileList(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 准备上传
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(4096); // 设置缓冲区大小

        String isTemp = request.getParameter("isTemp"); // 是否上到临时目录 默认为临时目录
        String folder = request.getParameter("folder"); // 上传到的目录
        String uri = "UploadTemp";
        if ("false".equals(Util.trim(isTemp))) {
            uri = "UploadDir";
        }

        WebApplicationContext ctx = RequestContextUtils.getWebApplicationContext(request);
        boolean isOk = true;

        Resource respath = ctx.getResource(appctx.getParam(uri) + File.separator); // upload目录
        File pathfile = respath.getFile();
        if (!Util.isEmpty(folder)) {
            String newPath = pathfile.getAbsolutePath() + File.separator + folder;
            pathfile = new File(newPath);
        }

        // 看目录是否创建成功.....
        if (!pathfile.exists()) {
            if (!pathfile.mkdirs()) {
                isOk = false;
            }
        }

        // 看是否成功
        if (!isOk) {
            throw new AppException("MSGCODE.1012");
        }

        // 处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        String fileSize = request.getParameter("fileSize");
        if (Util.isEmpty(fileSize)) {
            upload.setSizeMax(2048000000); // 2G 最大上传
        } else {
            upload.setSizeMax(Integer.parseInt(fileSize)); // 2G 最大上传
        }

        // Parse the request
        List<FileItem> items = upload.parseRequest(request);
        Iterator<FileItem> iter = items.iterator();
        List<Map<String, String>> fileList = new ArrayList<Map<String, String>>(); // 返回上传的文件路径信息
        // 循环读取上传文件
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if (item.isFormField()) {
                log.error("no file upload error...");
            } else {
                String fileName = item.getName();
                if (Util.isEmpty(fileName)) {
                    continue;
                }
                long curTime = System.currentTimeMillis();
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                String ext = fileName.substring(fileName.lastIndexOf("."));
                ext = ext.toLowerCase();

                String newFileName = curTime + ext; // 新的文件名称

                File uploadedFile = new File(pathfile, newFileName); // 存储文件

                item.write(uploadedFile); // 写入

                Map<String, String> mapfile = new HashMap<String, String>();

                mapfile.put("fileName", pathfile + File.separator + newFileName);
                mapfile.put("name", newFileName);
                mapfile.put("type", "f");
                mapfile.put("size", String.valueOf(uploadedFile.length()));
                fileList.add(mapfile); // 加入 文件信息
                log.debug("upload success info uri:[ " + pathfile + newFileName + " ]");
            }
        }
        request.setAttribute("realName", fileList.size() != 0 ? fileList.get(0).get("realName") : "");
        if (fileList.size() == 1) {
            request.setAttribute("fileCtx", mapper.toJson(fileList.get(0)));
        } else {
            request.setAttribute("fileCtx", mapper.toJson(fileList));
        }
        return "uploadok"; // 返回上传成功的文件信息
    }
}
