package project_os.project.modules.elearning.rest;

/**
 * Quản lý tập trung toàn bộ danh sách URL API Endpoints trong hệ thống.
 * Giúp tránh hardcode chuỗi URL và dễ dàng cấu hình Security, Versioning.
 */
public final class RestEndpoint {

    private RestEndpoint() {
        // Private constructor để ngăn việc tạo instance
    }

    // Prefix chung cho toàn bộ REST API
    public static final String API_PREFIX = "/api";

    // Endpoints quản lý Khóa học (Courses)
    public static final String COURSES = API_PREFIX + "/courses";
    public static final String COURSE_ID = "/{id}";

    // Endpoints quản lý Sinh viên (Students)
    public static final String STUDENTS = API_PREFIX + "/students";
    public static final String STUDENT_ID = "/{id}";
}
