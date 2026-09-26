package project_os.project.modules.elearning.wrapper;

import project_os.project.modules.elearning.model.Student;

/** Dữ liệu trung gian giữa request/API và entity Student. */
public class StudentWrapper {
    private Long id;
    private String name;
    private String email;
    private String password;

    public StudentWrapper() {}

    public StudentWrapper(String name, String email) {
        this(null, name, email, null);
    }

    public StudentWrapper(Long id, String name, String email) {
        this(id, name, email, null);
    }

    public StudentWrapper(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static StudentWrapper fromEntity(Student student) {
        if (student == null) return null;
        return new StudentWrapper(
                student.getId(),
                student.getName(),
                student.getEmail()
        );
    }

    public Student toEntity() {
        // Constructor domain chịu trách nhiệm validation và chuẩn hóa dữ liệu.
        Student student = new Student(this.name, this.email);
        student.setId(this.id);
        return student;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
