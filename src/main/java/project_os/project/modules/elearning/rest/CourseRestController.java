package project_os.project.modules.elearning.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_os.project.modules.elearning.service.CourseService;
import project_os.project.modules.elearning.wrapper.CourseWrapper;

import java.util.List;

@RestController
@RequestMapping(RestEndpoint.COURSES)
@Tag(name = "Courses", description = "Danh sách API quản lý các khóa học (Course Management)")
public class CourseRestController {

    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Lấy danh sách khóa học", description = "Tìm kiếm theo từ khóa hoặc lấy toàn bộ khóa học nếu không truyền keyword.")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping
    public ResponseEntity<List<CourseWrapper>> getCourses(
            @Parameter(description = "Từ khóa tìm kiếm theo tên sinh viên, khoa, sở thích")
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(courseService.getCourses(keyword));
    }

    @Operation(summary = "Lấy chi tiết khóa học", description = "Tìm kiếm khóa học theo ID định danh duy nhất.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy khóa học"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khóa học với ID đã cho")
    })
    @GetMapping(RestEndpoint.COURSE_ID)
    public ResponseEntity<CourseWrapper> getCourseById(
            @Parameter(description = "ID của khóa học", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @Operation(summary = "Tạo mới khóa học", description = "Tạo mới một khóa học trong hệ thống với thông tin từ CourseWrapper.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo mới khóa học thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ")
    })
    @PostMapping
    public ResponseEntity<CourseWrapper> createCourse(
            @Parameter(description = "Thông tin khóa học mới", required = true)
            @RequestBody CourseWrapper request) {
        CourseWrapper created = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Cập nhật khóa học", description = "Cập nhật toàn bộ thông tin khóa học theo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khóa học")
    })
    @PutMapping(RestEndpoint.COURSE_ID)
    public ResponseEntity<CourseWrapper> updateCourse(
            @Parameter(description = "ID của khóa học cần sửa", required = true)
            @PathVariable Long id,
            @RequestBody CourseWrapper request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @Operation(summary = "Cập nhật một phần khóa học (Patch)", description = "Chỉ cập nhật những trường được truyền vào.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật một phần thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khóa học")
    })
    @PatchMapping(RestEndpoint.COURSE_ID)
    public ResponseEntity<CourseWrapper> patchCourse(
            @Parameter(description = "ID của khóa học", required = true)
            @PathVariable Long id,
            @RequestBody CourseWrapper request) {
        return ResponseEntity.ok(courseService.patchCourse(id, request));
    }

    @Operation(summary = "Xóa khóa học", description = "Xóa vĩnh viễn khóa học theo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công (No Content)"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khóa học cần xóa")
    })
    @DeleteMapping(RestEndpoint.COURSE_ID)
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "ID khóa học cần xóa", required = true)
            @PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
