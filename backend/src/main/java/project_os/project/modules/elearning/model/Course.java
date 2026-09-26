package project_os.project.modules.elearning.model;

import jakarta.persistence.*;

/**
 * Entity miền nghiệp vụ của khóa học.
 *
 * Entity tự giữ các quy tắc để một Course luôn hợp lệ. Đây là ý tưởng OOP
 * chính trong lần refactor này: bên ngoài yêu cầu object thực hiện hành vi
 * thay vì sửa từng field từ bên ngoài.
 */
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
public class Course extends BaseEntity {

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String student;

    private String favourite;

    @Column(name = "is_status", nullable = false)
    private Boolean isStatus = true;

    /**
     * JPA cần constructor không tham số. Để protected giúp code nghiệp vụ
     * phải tạo Course bằng constructor có ý nghĩa nghiệp vụ.
     */
    protected Course() {}

    public Course(String department, String student, String favourite, Boolean isStatus) {
        updateInformation(department, student, favourite);
        this.isStatus = isStatus != null ? isStatus : true;
    }

    /**
     * Thay đổi toàn bộ thông tin người dùng được phép sửa trong một thao tác.
     * Đặt hành vi ở entity giúp các use case mới không tạo ra state sai.
     */
    public void updateInformation(String department, String student, String favourite) {
        this.department = requireText(department, "Khoa (Department)");
        this.student = requireText(student, "Tên sinh viên (Student)");
        // favourite null nghĩa là giữ nguyên khi update.
        // Entity mới vẫn nhận giá trị mặc định là chuỗi rỗng.
        if (favourite != null) {
            this.favourite = normalizeOptional(favourite);
        } else if (this.favourite == null) {
            this.favourite = "";
        }
    }

    public void changeDepartment(String department) {
        this.department = requireText(department, "Khoa (Department)");
    }

    public void changeStudent(String student) {
        this.student = requireText(student, "Tên sinh viên (Student)");
    }

    public void changeFavourite(String favourite) {
        this.favourite = normalizeOptional(favourite);
    }

    /** Boolean null từ HTTP request được chuyển thành giá trị mặc định an toàn. */
    public void changeStatus(Boolean status) {
        this.isStatus = status != null ? status : true;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " không được để trống");
        }
        return value.trim();
    }

    private static String normalizeOptional(String value) {
        return value == null ? "" : value.trim();
    }

    public String getDepartment() {
        return department;
    }

    public String getStudent() {
        return student;
    }

    public String getFavourite() {
        return favourite;
    }

    public Boolean getIsStatus() {
        return isStatus;
    }

}
