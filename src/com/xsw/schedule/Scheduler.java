package com.xsw.schedule;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsw.ctx.AppCtx;
import com.xsw.utils.Util;

/**
 * 
 * @author lyodssoft.com
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-10-10
 * @description 系统后台任务调度定时器处理类 - 创建
 *
 */

@Component
public class Scheduler {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(Scheduler.class);

    @Autowired
    private AppCtx appctx;

    /**
     * 定时清理临时目录文件
     */
    public void cleanTempDirectory() {
        String tempdir = appctx.getParam("UploadTemp");
        int keepdays = Integer.parseInt(appctx.getParam("TmpKeepDays"));
        log.debug("Begin to cleanTempDirectory:" + tempdir + ", it will clean " + keepdays + " days old files...");
        Util.deleteFileByDays(tempdir, keepdays);
        log.debug("Finish to cleanTempDirectory:" + tempdir + ".");
    }

    /**
     * 定时发送邮件信息
     */
    public void sendEmailLog() {
        log.debug("begin to doing batch email sending...");
        //emailSender.batchSendEmailLog();
        log.debug("Finish send email.");
    }

}
