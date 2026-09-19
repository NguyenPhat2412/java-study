package project_os.project.modules.elearning.dto.student;

import project_os.project.modules.elearning.model.Student;

public record StudentRequest(
    String name,
    String email
) {
    public void applyTo(Student student) {
        if (name != null) student.setName(name.trim());
        if (email != null) student.setEmail(email.trim());
    }
}

