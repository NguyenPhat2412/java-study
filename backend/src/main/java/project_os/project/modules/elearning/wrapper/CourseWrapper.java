package project_os.project.modules.elearning.wrapper;

import project_os.project.modules.elearning.model.Course;

/** Dữ liệu trung gian giữa request/API và entity Course. */
public class CourseWrapper {
    private Long id;
    private String department;
    private String student;
    private String favourite;
    private Boolean isStatus;

    public CourseWrapper() {}

    public CourseWrapper(String department, String student, String favourite, Boolean isStatus) {
        this(null, department, student, favourite, isStatus);
    }

    public CourseWrapper(Long id, String department, String student, String favourite, Boolean isStatus) {
        this.id = id;
        this.department = department;
        this.student = student;
        this.favourite = favourite;
        this.isStatus = isStatus;
    }

    public static CourseWrapper fromEntity(Course course) {
        if (course == null) return null;
        return new CourseWrapper(
                course.getId(),
                course.getDepartment(),
                course.getStudent(),
                course.getFavourite(),
                course.getIsStatus()
        );
    }

    public Course toEntity() {
        Course course = new Course();
        course.setId(this.id);
        course.setDepartment(this.department != null ? this.department.trim() : null);
        course.setStudent(this.student != null ? this.student.trim() : null);
        course.setFavourite(this.favourite != null ? this.favourite.trim() : null);
        course.setIsStatus(this.isStatus != null ? this.isStatus : true);
        return course;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getStudent() { return student; }
    public void setStudent(String student) { this.student = student; }
    public String getFavourite() { return favourite; }
    public void setFavourite(String favourite) { this.favourite = favourite; }
    public Boolean getIsStatus() { return isStatus; }
    public void setIsStatus(Boolean isStatus) { this.isStatus = isStatus; }
}