package com.crm.model;

import java.time.LocalDateTime;

public class ContactHistory {
    private Long id;
    private Long customerId;
    private LocalDateTime contactDate;
    private String contactType;
    private String notes;
    private Long createdBy;
    private LocalDateTime createdAt;

    public ContactHistory(Long customerId, String contactType, String notes, Long createdBy) {
        this.customerId = customerId;
        this.contactType = contactType;
        this.notes = notes;
        this.createdBy = createdBy;
        this.contactDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public LocalDateTime getContactDate() { return contactDate; }
    public void setContactDate(LocalDateTime contactDate) { this.contactDate = contactDate; }
    public String getContactType() { return contactType; }
    public void setContactType(String contactType) { this.contactType = contactType; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}