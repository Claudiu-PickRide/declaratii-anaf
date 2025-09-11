package eu.pickride.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_user_declarations")
@SQLDelete(sql = "UPDATE monthly_user_declarations SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class MonthlyUserDeclarationDataModel {

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

    @Column(name = "file_location_d100", nullable = true)
    private String d100FileLocation;

    @Column(name = "file_location_d300", nullable = true)
    private String d300FileLocation;

    @Column(name = "file_location_d390", nullable = true)
    private String d390FileLocation;

    @Column(name = "file_location_d710", nullable = true)
    private String d710FileLocation;

    @Column(name = "user_id", nullable = false)
    private String userId;

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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getD100FileLocation() {
        return d100FileLocation;
    }

    public void setD100FileLocation(String d100FileLocation) {
        this.d100FileLocation = d100FileLocation;
    }

    public String getD300FileLocation() {
        return d300FileLocation;
    }

    public void setD300FileLocation(String d300FileLocation) {
        this.d300FileLocation = d300FileLocation;
    }

    public String getD390FileLocation() {
        return d390FileLocation;
    }

    public void setD390FileLocation(String d390FileLocation) {
        this.d390FileLocation = d390FileLocation;
    }

    public String getD710FileLocation() {
        return d710FileLocation;
    }

    public void setD710FileLocation(String d710FileLocation) {
        this.d710FileLocation = d710FileLocation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}