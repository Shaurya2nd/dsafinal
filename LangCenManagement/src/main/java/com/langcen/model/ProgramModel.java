package com.langcen.model;

import java.time.LocalDateTime;

public class ProgramModel {
    private int programId;
    private String programCode;
    private String name;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public ProgramModel() {
        this.status = "active";
    }

    public ProgramModel(String name) {
        this();
        this.name = name;
    }

    // Getters and Setters
    public int getProgramId() { return programId; }
    public void setProgramId(int programId) { this.programId = programId; }

    public String getProgramCode() { return programCode; }
    public void setProgramCode(String programCode) { this.programCode = programCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}