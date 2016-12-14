package com.cleverm.smartpen.net;



/**
 * Created by Enva on 2016/1/7.
 */
public class InfoSendSMSVo {
    private long          clientID;
    private long          orgID;
    private long          tableID;
    private int           templateID;//接收参数：模板ID

    private String        tableName;//接收参数：桌名

    private boolean isSuccess;
    private boolean success;//输出参数：是否成功

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }
    private String msg;//输出参数：是否成功

    public InfoSendSMSVo(){}

    public InfoSendSMSVo(boolean isSuccess, String msg){
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public InfoSendSMSVo(long clientID, long orgID, long tableID, int templateID) {
        this.clientID = clientID;
        this.orgID = orgID;
        this.tableID = tableID;
        this.templateID = templateID;
    }

    public InfoSendSMSVo(long clientID, long orgID, long tableID){
        this.clientID = clientID;
        this.orgID = orgID;
        this.tableID = tableID;
    }

    public long getClientID() {
        return clientID;
    }

    public void setClientID(long clientID) {
        this.clientID = clientID;
    }

    public long getOrgID() {
        return orgID;
    }

    public void setOrgID(long orgID) {
        this.orgID = orgID;
    }

    public long getTableID() {
        return tableID;
    }

    public void setTableID(long tableID) {
        this.tableID = tableID;
    }

    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "InfoSendSMSVo{" +
                "clientID=" + clientID +
                ", orgID=" + orgID +
                ", tableID=" + tableID +
                ", templateID=" + templateID +
                ", tableName='" + tableName + '\'' +
                ", isSuccess=" + isSuccess +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
