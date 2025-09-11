package eu.pickride.db.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "pdf_document")
@SQLDelete(sql = "UPDATE pdf_document SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class PdfDocumentDataModel {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "declaration_type", nullable = false)
    private String declarationType;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "file_location")
    private String fileLocation;

    @Column(name = "validation_error")
    private String validationError;

    @Column(name = "rectified")
    private boolean rectified;

    // --- Getters and Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getDeclarationType() {
        return declarationType;
    }

    public void setDeclarationType(String declarationType) {
        this.declarationType = declarationType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getValidationError() {
        return validationError;
    }

    public void setValidationError(String validationError) {
        this.validationError = validationError;
    }

    public void setRectified(boolean rectified) {
        this.rectified = rectified;
    }

    public boolean getRectified(boolean rectified) {
        return rectified;
    }


}

