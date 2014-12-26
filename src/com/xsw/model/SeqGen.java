package com.xsw.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-06-10
 * @description 系统序号生成器表
 *
 */

@Entity
@Table(name = "T_SEQ_GEN")
public class SeqGen extends AbstractEntity implements Serializable {
    /**
     * SEQ ID
     */
    private static final long serialVersionUID = 1L;
    private int appId;
    private String name;
    private int seqno;

    @Id
    @Column(name = "APP_ID", nullable = false)
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    @Id
    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "SEQ_NO", nullable = false)
    public int getSeqno() {
        return seqno;
    }

    public void setSeqno(int seqno) {
        this.seqno = seqno;
    }

}
