package com.dm.model;

/**
 * Created by Dataman on 6/10/2017.
 */
public class DeleteLogData
{
    String entityName,entityId;

    public String getEntityId() {
        return entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
