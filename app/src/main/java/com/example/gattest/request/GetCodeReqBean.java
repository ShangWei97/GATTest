package com.example.gattest.request;

public class GetCodeReqBean {
    private Integer status;

    private String telNum;

    private Integer tplId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public Integer getTplId() {
        return tplId;
    }

    public void setTplId(Integer tplId) {
        this.tplId = tplId;
    }
}
