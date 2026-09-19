package project_os.project.modules.elearning.dto.student;

import project_os.project.modules.elearning.model.Student;

public record StudentResponse(
    Long id,
    String name,
    String email
) {
    public static StudentResponse fromEntity(Student student) {
        return new StudentResponse(
            student.getId(),
            student.getName(),
            student.getEmail()
        );
    }
}

