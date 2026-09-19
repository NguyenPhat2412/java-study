package project_os.project.modules.elearning.dto;

import project_os.project.modules.elearning.model.Course;

public record CourseRequest(
    String department,
    String student,
    String favourite,
    Boolean isStatus
) {
    public void applyTo(Course course) {
        if (department != null) course.setDepartment(department.trim());
        if (student != null) course.setStudent(student.trim());
        if (favourite != null) course.setFavourite(favourite.trim());
        if (isStatus != null) course.setIsStatus(isStatus);
    }
}
