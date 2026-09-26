package project_os.project.modules.elearning.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_os.project.modules.elearning.service.StudentService;
import project_os.project.modules.elearning.wrapper.StudentWrapper;

import java.util.List;

@RestController
@RequestMapping(RestEndpoint.STUDENTS)
@Tag(name = "Students", description = "Danh sách API quản lý sinh viên (Student Management)")
public class StudentRestController {

    private final StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Lấy danh sách sinh viên", description = "Tìm kiếm theo từ khóa hoặc lấy toàn bộ sinh viên.")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping
    public ResponseEntity<List<StudentWrapper>> getStudents(
            @Parameter(description = "Từ khóa tìm kiếm theo tên hoặc email")
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(studentService.getStudents(keyword));
    }

    @Operation(summary = "Lấy chi tiết sinh viên", description = "Tìm kiếm sinh viên theo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy sinh viên"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy sinh viên với ID đã cho")
    })
    @GetMapping(RestEndpoint.STUDENT_ID)
    public ResponseEntity<StudentWrapper> getStudentById(
            @Parameter(description = "ID của sinh viên", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @Operation(summary = "Tạo mới sinh viên", description = "Thêm mới một sinh viên vào hệ thống.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo mới sinh viên thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu sinh viên không hợp lệ")
    })
    @PostMapping
    public ResponseEntity<StudentWrapper> createStudent(
            @Parameter(description = "Thông tin sinh viên mới", required = true)
            @RequestBody StudentWrapper request) {
        StudentWrapper created = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Cập nhật sinh viên", description = "Cập nhật toàn bộ thông tin của sinh viên.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy sinh viên")
    })
    @PutMapping(RestEndpoint.STUDENT_ID)
    public ResponseEntity<StudentWrapper> updateStudent(
            @Parameter(description = "ID của sinh viên cần cập nhật", required = true)
            @PathVariable Long id,
            @RequestBody StudentWrapper request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @Operation(summary = "Cập nhật một phần sinh viên (Patch)", description = "Chỉ cập nhật những trường được truyền vào.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy sinh viên")
    })
    @PatchMapping(RestEndpoint.STUDENT_ID)
    public ResponseEntity<StudentWrapper> patchStudent(
            @Parameter(description = "ID của sinh viên", required = true)
            @PathVariable Long id,
            @RequestBody StudentWrapper request) {
        return ResponseEntity.ok(studentService.patchStudent(id, request));
    }

    @Operation(summary = "Xóa sinh viên", description = "Xóa sinh viên khỏi hệ thống theo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công (No Content)"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy sinh viên")
    })
    @DeleteMapping(RestEndpoint.STUDENT_ID)
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "ID sinh viên cần xóa", required = true)
            @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
