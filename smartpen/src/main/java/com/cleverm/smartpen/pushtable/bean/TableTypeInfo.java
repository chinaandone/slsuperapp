package com.cleverm.smartpen.pushtable.bean;

/**
 * Created by 95 on 2016/2/19.
 */
public class TableTypeInfo {
    private String capacity;
    private String description;
    private String minimum;
    private String typeId;
    private String typeName;

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "TableTypeInfo{" +
                "capacity='" + capacity + '\'' +
                ", description='" + description + '\'' +
                ", minimum='" + minimum + '\'' +
                ", typeId='" + typeId + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
