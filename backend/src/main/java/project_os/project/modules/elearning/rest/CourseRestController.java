package project_os.project.modules.elearning.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_os.project.modules.elearning.service.CourseService;
import project_os.project.modules.elearning.wrapper.CourseWrapper;

import java.util.List;

@RestController
@RequestMapping(RestEndpoint.COURSES)
@Tag(name = "Courses")
public class CourseRestController {

    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Lấy danh sách khóa học")
    @GetMapping
    public ResponseEntity<List<CourseWrapper>> getCourses(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(courseService.getCourses(keyword));
    }

    @Operation(summary = "Lấy chi tiết khóa học theo ID")
    @GetMapping(RestEndpoint.COURSE_ID)
    public ResponseEntity<CourseWrapper> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @Operation(summary = "Tạo mới khóa học")
    @PostMapping
    public ResponseEntity<CourseWrapper> createCourse(@RequestBody CourseWrapper request) {
        CourseWrapper created = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Cập nhật khóa học")
    @PutMapping(RestEndpoint.COURSE_ID)
    public ResponseEntity<CourseWrapper> updateCourse(@PathVariable Long id, @RequestBody CourseWrapper request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @Operation(summary = "Cập nhật một phần khóa học")
    @PatchMapping(RestEndpoint.COURSE_ID)
    public ResponseEntity<CourseWrapper> patchCourse(@PathVariable Long id, @RequestBody CourseWrapper request) {
        return ResponseEntity.ok(courseService.patchCourse(id, request));
    }

    @Operation(summary = "Xóa khóa học")
    @DeleteMapping(RestEndpoint.COURSE_ID)
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
