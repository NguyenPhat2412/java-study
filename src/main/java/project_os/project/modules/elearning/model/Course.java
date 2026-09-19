package project_os.project.modules.elearning.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@NamedQueries({
    @NamedQuery(
        name = "Course.findAll",
        query = "SELECT c FROM Course c ORDER BY c.id DESC"
    ),
    @NamedQuery(
        name = "Course.searchCourses",
        query = "SELECT c FROM Course c WHERE " +
                "LOWER(c.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(c.student) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(c.favourite) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                "ORDER BY c.id DESC"
    ),
    @NamedQuery(
        name = "Course.countById",
        query = "SELECT COUNT(c) FROM Course c WHERE c.id = :id"
    ),
    @NamedQuery(
        name = "Course.findById",
        query = "SELECT c FROM Course c WHERE c.id = :id"
    )
})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String student;

    private String favourite;

    @Column(name = "is_status", nullable = false)
    private Boolean isStatus = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Course() {}

    public Course(String department, String student, String favourite, Boolean isStatus) {
        this.department = department;
        this.student = student;
        this.favourite = favourite;
        this.isStatus = isStatus != null ? isStatus : true;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public Boolean getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(Boolean isStatus) {
        this.isStatus = isStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
