package project_os.project.modules.elearning.common;

/**
 * Hằng số hệ thống dùng chung (System Constants):
 * - Thông báo lỗi mặc định (Messages)
 * - Trạng thái hệ thống (Status)
 */
public class CoreContants {

    private CoreContants() {
        // Ngăn việc tạo instance
    }

    // ==========================================
    // Messages
    // ==========================================
    public static final String COURSE_NOT_FOUND = "Course not found";
    public static final String STUDENT_NOT_FOUND = "Student not found";
    public static final String COURSE_ALREADY_EXISTS = "Course already exists";
    public static final String STUDENT_ALREADY_EXISTS = "Student already exists";
    public static final String INVALID_COURSE_ID = "Invalid course ID";
    public static final String INVALID_STUDENT_ID = "Invalid student ID";
    public static final String DEPARTMENT_NOT_FOUND = "Department not found";

    // ==========================================
    // Status
    // ==========================================
    public static final Boolean IS_ACTIVE = true;
    public static final Boolean IS_INACTIVE = false;
}
