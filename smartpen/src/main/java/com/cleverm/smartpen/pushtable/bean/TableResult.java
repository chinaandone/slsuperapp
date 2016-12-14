package com.cleverm.smartpen.pushtable.bean;

import java.util.List;

/**
 * Created by 95 on 2016/2/19.
 */
public class TableResult {
    private String orgID;
    private String clientID;
    private String orgName;
    private List<TableInfo> tableList;
    private List<TableTypeInfo> tableTypeList;

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<TableInfo> getTableList() {
        return tableList;
    }

    public void setTableList(List<TableInfo> tableList) {
        this.tableList = tableList;
    }

    public List<TableTypeInfo> getTableTypeList() {
        return tableTypeList;
    }

    public void setTableTypeList(List<TableTypeInfo> tableTypeList) {
        this.tableTypeList = tableTypeList;
    }

    @Override
    public String toString() {
        return "TableResult{" +
                "orgID='" + orgID + '\'' +
                ", clientID='" + clientID + '\'' +
                ", orgName='" + orgName + '\'' +
                ", tableList=" + tableList +
                ", tableTypeList=" + tableTypeList +
                '}';
    }
}
