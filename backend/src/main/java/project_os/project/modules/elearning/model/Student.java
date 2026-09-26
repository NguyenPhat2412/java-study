package project_os.project.modules.elearning.model;

import jakarta.persistence.*;

/**
 * Entity miền nghiệp vụ của Student. Name và email được thay đổi qua method
 * để entity tự bảo vệ invariant thay vì mở public setter.
 */
@Entity
@Table(name = "students")
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s ORDER BY s.id DESC"),
    @NamedQuery(name = "Student.findByKeyword", query = "SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY s.id DESC"),
    @NamedQuery(name = "Student.countById", query = "SELECT COUNT(s) FROM Student s WHERE s.id = :id")
})
public class Student extends BaseEntity {
    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true) private String email;

    /** JPA bắt buộc constructor này; code nghiệp vụ dùng constructor bên dưới. */
    protected Student() {}

    public Student(String name, String email) {
        updateContactInformation(name, email);
    }

    /** Cập nhật toàn bộ thông tin Student mà người dùng được phép sửa. */
    public void updateContactInformation(String name, String email) {
        this.name = requireText(name, "Tên sinh viên");
        this.email = requireText(email, "Email sinh viên");
    }

    public void changeName(String name) {
        this.name = requireText(name, "Tên sinh viên");
    }

    public void changeEmail(String email) {
        this.email = requireText(email, "Email sinh viên");
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " không được để trống");
        }
        return value.trim();
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
}
