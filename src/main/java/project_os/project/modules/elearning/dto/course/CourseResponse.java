package project_os.project.modules.elearning.dto.course;

import java.time.LocalDateTime;
import project_os.project.modules.elearning.model.Course;

public record CourseResponse(
    Long id,
    String department,
    String student,
    String favourite,
    Boolean isStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CourseResponse fromEntity(Course course) {
        return new CourseResponse(
            course.getId(),
            course.getDepartment(),
            course.getStudent(),
            course.getFavourite(),
            course.getIsStatus(),
            course.getCreatedAt(),
            course.getUpdatedAt()
        );
    }
}
