package project_os.project.modules.elearning.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_os.project.modules.elearning.service.StudentService;
import project_os.project.modules.elearning.wrapper.StudentWrapper;

import java.util.List;

@RestController
@RequestMapping(RestEndpoint.STUDENTS)
@Tag(name = "Students")
public class StudentRestController {

    private final StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Lấy danh sách sinh viên")
    @GetMapping
    public ResponseEntity<List<StudentWrapper>> getStudents(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(studentService.getStudents(keyword));
    }

    @Operation(summary = "Lấy chi tiết sinh viên theo ID")
    @GetMapping(RestEndpoint.STUDENT_ID)
    public ResponseEntity<StudentWrapper> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @Operation(summary = "Tạo mới sinh viên")
    @PostMapping
    public ResponseEntity<StudentWrapper> createStudent(@RequestBody StudentWrapper request) {
        StudentWrapper created = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Cập nhật sinh viên")
    @PutMapping(RestEndpoint.STUDENT_ID)
    public ResponseEntity<StudentWrapper> updateStudent(@PathVariable Long id, @RequestBody StudentWrapper request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @Operation(summary = "Cập nhật một phần sinh viên")
    @PatchMapping(RestEndpoint.STUDENT_ID)
    public ResponseEntity<StudentWrapper> patchStudent(@PathVariable Long id, @RequestBody StudentWrapper request) {
        return ResponseEntity.ok(studentService.patchStudent(id, request));
    }

    @Operation(summary = "Xóa sinh viên")
    @DeleteMapping(RestEndpoint.STUDENT_ID)
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
