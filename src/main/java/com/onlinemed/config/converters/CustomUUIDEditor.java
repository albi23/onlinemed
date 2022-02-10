package com.onlinemed.config.converters;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.util.UUID;

/**
 * Implementation of PropertyEditorSupport
 * that allows for setting property values onto a target UUID object
 */
public class CustomUUIDEditor extends PropertyEditorSupport {

    /**
     * Sets the property value by parsing a given String.  May raise
     * java.lang.IllegalArgumentException if either the String is
     * badly formatted or if this kind of property can't be expressed
     * as text.
     *
     * @param text - text form of UUID object
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text) && !text.equals("null")) {
            setValue(UUID.fromString(text));
        } else {
            setValue(null);
        }
    }

    /**
     * Gets the property value as a string suitable for presentation
     * to a human to edit.
     *
     * @return The property value as a string suitable for presentation
     * to a human to edit.
     * <p>   Returns null if the value can't be expressed as a string.
     * <p>   If a non-null value is returned, then the PropertyEditor should
     * be prepared to parse that string back in setAsText().
     */
    @Override
    public String getAsText() {
        UUID value = (UUID) getValue();
        return (value != null ? value.toString() : "");
    }

}
