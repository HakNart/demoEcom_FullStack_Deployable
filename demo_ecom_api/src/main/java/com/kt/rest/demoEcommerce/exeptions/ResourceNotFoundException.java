package com.kt.rest.demoEcommerce.exeptions;

import java.util.NoSuchElementException;

public class ResourceNotFoundException extends NoSuchElementException {
    private String resourceName;
    private String resourceField;
    private String resourceFieldValue;
    private int resourceFieldId;
    public ResourceNotFoundException() {}
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String resourceField, String resourceFieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, resourceField, resourceFieldValue));
        this.resourceName = resourceName;
        this.resourceField = resourceField;
        this.resourceFieldValue = resourceFieldValue;
    }

    public ResourceNotFoundException (String resourceName, String resourceField, int resourceFieldId) {
        super(String.format("%s not found with %s: %s", resourceName, resourceField, resourceFieldId));
        this.resourceName = resourceName;
        this.resourceField = resourceField;
        this.resourceFieldId = resourceFieldId;
    }
}
