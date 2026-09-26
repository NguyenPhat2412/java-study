package project_os.project.modules.elearning.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s ORDER BY s.id DESC"),
    @NamedQuery(name = "Student.findByKeyword", query = "SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY s.id DESC"),
    @NamedQuery(name = "Student.countById", query = "SELECT COUNT(s) FROM Student s WHERE s.id = :id")
})
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true) private String email;

    public Student() {}
    public Student(String name, String email) { this.name = name; this.email = email; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
