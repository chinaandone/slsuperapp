package com.cleverm.smartpen.pushtable;

import java.util.List;

public class RestaurantVo {
    private List<RTableTypeVo> tableTypeList;
    private List<RTableZoneVo> tableZoneList;
    private List<RTableVo> tableList;
    private List<RDeviceVo> deviceList;
    private Long clientID;
    private Long orgID;
    private String orgName;

    public RestaurantVo() {
    }

    public RestaurantVo(List<RTableTypeVo> typeList, List<RTableZoneVo> zoneList, List<RDeviceVo> deviceList,
                        List<RTableVo> tableList) {
        this.tableTypeList = typeList;
        this.tableZoneList = zoneList;
        this.deviceList = deviceList;
        this.tableList = tableList;
    }
    public List<RDeviceVo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<RDeviceVo> deviceList) {
        this.deviceList = deviceList;
    }

    public List<RTableZoneVo> getTableZoneList() {
        return tableZoneList;
    }

    public void setTableZoneList(List<RTableZoneVo> tableZoneList) {
        this.tableZoneList = tableZoneList;
    }

    public List<RTableTypeVo> getTableTypeList() {
        return tableTypeList;
    }

    public void setTableTypeList(List<RTableTypeVo> tableTypeList) {
        this.tableTypeList = tableTypeList;
    }

    public List<RTableVo> getTableList() {
        return tableList;
    }

    public void setTableList(List<RTableVo> tableList) {
        this.tableList = tableList;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public Long getOrgID() {
        return orgID;
    }

    public void setOrgID(Long orgID) {
        this.orgID = orgID;
    }
}
