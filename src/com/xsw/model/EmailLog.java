package com.xsw.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Table T_EMAIL_LOG
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2013-09-12
 * @description 邮件发送日志表
 * 
 */
@Entity
@Table(name = "T_EMAIL_LOG")
public class EmailLog extends AbstractEntity implements Serializable {
    /**
     * EID
     */
    private static final long serialVersionUID = 1L;
    private int eid;
    private AppList appList;
    private User user;
    private String subject;
    private String mailTo;
    private String ccTo;
    private String bccTo;
    private String attach;
    private String content;
    private String createTime;
    private String sendTime;
    private int retryTimes; // 失败次数
    private int status; // 0.未发送，1.已发送,2.sended failed,3.邮件准备中..,4.生成附件失败

    @Id
    @SequenceGenerator(name = "SEQ_T_EMAIL_LOG", sequenceName = "SEQ_T_EMAIL_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_T_EMAIL_LOG")
    @Column(name = "EID", nullable = false)
    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    @ManyToOne
    @JoinColumn(name = "APP_ID")
    public AppList getAppList() {
        return appList;
    }

    public void setAppList(AppList appList) {
        this.appList = appList;
    }

    @ManyToOne
    @JoinColumn(name = "USERID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NotBlank
    @Length(max = 1000)
    @Column(name = "SUBJECT")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @NotBlank
    @Column(name = "MAIL_TO")
    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    @Column(name = "CC_TO")
    public String getCcTo() {
        return ccTo;
    }

    public void setCcTo(String ccTo) {
        this.ccTo = ccTo;
    }

    @Column(name = "BCC_TO")
    public String getBccTo() {
        return bccTo;
    }

    public void setBccTo(String bccTo) {
        this.bccTo = bccTo;
    }

    @Column(name = "ATTACH")
    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NotNull
    @Column(name = "CREATE_TIME")
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Column(name = "SEND_TIME")
    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @NotNull
    @Column(name = "RETRY_TIMES")
    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    @NotNull
    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
