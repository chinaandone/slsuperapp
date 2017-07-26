package com.cleverm.smartpen.bean.event;

/**
 * Created by Randy on 2017/7/26.
 */

public class TableInfo {
    private Long tableId;

    private Long tableTypeId;
    private String tableTypeName;

    private Long tableZoneId;
    private String tableZoneName;

    private String name;

    private String description;

    private Integer seatAdded;

    private Long beeperDeviceId;

    private String scanCode;//桌子码，用于点点笔扫码

    private String erpId;//erpId，小超人用

    private int runflag; //for设备心跳
    private String versioncode="";//
    private String versionname="";//

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getTableTypeId() {
        return tableTypeId;
    }

    public void setTableTypeId(Long tableTypeId) {
        this.tableTypeId = tableTypeId;
    }

    public String getTableTypeName() {
        return tableTypeName;
    }

    public void setTableTypeName(String tableTypeName) {
        this.tableTypeName = tableTypeName;
    }

    public Long getTableZoneId() {
        return tableZoneId;
    }

    public void setTableZoneId(Long tableZoneId) {
        this.tableZoneId = tableZoneId;
    }

    public String getTableZoneName() {
        return tableZoneName;
    }

    public void setTableZoneName(String tableZoneName) {
        this.tableZoneName = tableZoneName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSeatAdded() {
        return seatAdded;
    }

    public void setSeatAdded(Integer seatAdded) {
        this.seatAdded = seatAdded;
    }

    public Long getBeeperDeviceId() {
        return beeperDeviceId;
    }

    public void setBeeperDeviceId(Long beeperDeviceId) {
        this.beeperDeviceId = beeperDeviceId;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public String getErpId() {
        return erpId;
    }

    public void setErpId(String erpId) {
        this.erpId = erpId;
    }

    public int getRunflag() {
        return runflag;
    }

    public void setRunflag(int runflag) {
        this.runflag = runflag;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }
}
