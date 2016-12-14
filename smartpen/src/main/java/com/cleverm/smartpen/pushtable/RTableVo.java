package com.cleverm.smartpen.pushtable;

public class RTableVo {
    private long typeId;
    private long tableId;
    private String tableName;
    private Integer seatAdded;
    private Long beeperID;
    private Long zoneId;

    public RTableVo() {
    }

    public RTableVo(long typeId, long tableId, String tableName, Long beeperID, Long zoneId) {
        super();
        this.typeId = typeId;
        this.tableId = tableId;
        this.tableName = tableName;
        this.beeperID = beeperID;
        this.zoneId = zoneId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getSeatAdded() {
        return seatAdded;
    }

    public void setSeatAdded(Integer seatAdded) {
        this.seatAdded = seatAdded;
    }

    public Long getBeeperID() {
        return beeperID;
    }

    public void setBeeperID(Long beeperID) {
        this.beeperID = beeperID;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + "BeerNo" + ":" + getBeeperID());
        sb.append("|" + "TableId" + ":" + getTableId());
        sb.append("|" + "TableName" + ":" + getTableName());
        sb.append("|" + "TypeId" + ":" + getTypeId() + "]");
        return sb.toString();
    }
}