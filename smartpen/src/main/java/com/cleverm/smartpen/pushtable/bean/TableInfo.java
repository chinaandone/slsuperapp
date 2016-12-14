package com.cleverm.smartpen.pushtable.bean;

/**
 * Created by 95 on 2016/2/19.
 */
public class TableInfo {
    private String beeperID;
    private String tableId;
    private String tableName;
    private String typeId;
    private String zoneId;

    public String getBeeperID() {
        return beeperID;
    }

    public void setBeeperID(String beeperID) {
        this.beeperID = beeperID;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "beeperID='" + beeperID + '\'' +
                ", tableId='" + tableId + '\'' +
                ", tableName='" + tableName + '\'' +
                ", typeId='" + typeId + '\'' +
                ", zoneId='" + zoneId + '\'' +
                '}';
    }
}
