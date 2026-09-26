package project_os.project.modules.elearning.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

/**
 * Lớp cha dùng chung cho các entity trong module.
 *
 * @MappedSuperclass nghĩa là lớp này không tạo một bảng riêng. Các field của
 * nó được JPA đưa vào bảng của lớp con, ví dụ bảng courses và students.
 * Đây là kế thừa để dùng lại phần persistence chung, không phải kế thừa để
 * gom các hành vi nghiệp vụ không liên quan.
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Constructor protected để chỉ entity con hoặc JPA khởi tạo trực tiếp. */
    protected BaseEntity() {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    /** Chỉ phục vụ test fixture và mapping persistence. */
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
