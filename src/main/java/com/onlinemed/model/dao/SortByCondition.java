package com.onlinemed.model.dao;

import java.util.UUID;

/**
 * Class providing implementation of the filtering process for the fields of a given entity
 */
public class SortByCondition {

    private String sortByField;
    private String sortByConnectedObjectField;
    private UUID attributeTypeId;

    public SortByCondition() {
    }

    /**
     * Gets sortByField
     *
     * @return value of sortByField
     */
    public String getSortByField() {
        return sortByField;
    }

    /**
     * Sets <code>SortByCondition</code> sortByField value
     *
     * @param sortByField - set new value of sortByField
     */
    public void setSortByField(String sortByField) {
        this.sortByField = sortByField;
    }

    /**
     * Gets sortByConnectedObjectField
     *
     * @return value of sortByConnectedObjectField
     */
    public String getSortByConnectedObjectField() {
        return sortByConnectedObjectField;
    }

    /**
     * Sets <code>SortByCondition</code> sortByConnectedObjectField value
     *
     * @param sortByConnectedObjectField - set new value of sortByConnectedObjectField
     */
    public void setSortByConnectedObjectField(String sortByConnectedObjectField) {
        this.sortByConnectedObjectField = sortByConnectedObjectField;
    }

    /**
     * Gets attributeTypeId
     *
     * @return value of attributeTypeId
     */
    public UUID getAttributeTypeId() {
        return attributeTypeId;
    }

    /**
     * Sets <code>SortByCondition</code> attributeTypeId value
     *
     * @param attributeTypeId - set new value of attributeTypeId
     */
    public void setAttributeTypeId(UUID attributeTypeId) {
        this.attributeTypeId = attributeTypeId;
    }
}
