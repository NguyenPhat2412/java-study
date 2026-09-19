package project_os.project.modules.elearning.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import project_os.project.modules.elearning.dao.student.StudentDAO;
import project_os.project.modules.elearning.dto.student.StudentRequest;
import project_os.project.modules.elearning.dto.student.StudentResponse;
import project_os.project.modules.elearning.model.Student;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudents(String keyword) {
        List<Student> students;
        if (keyword != null && !keyword.trim().isEmpty()) {
            students = studentDAO.findByKeyword(keyword.trim());
        } else {
            students = studentDAO.findAll();
        }
        return students.stream().map(StudentResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        Student student = findStudentOrThrow(id);
        return StudentResponse.fromEntity(student);
    }

    public StudentResponse createStudent(StudentRequest request) {
        validateRequest(request);
        Student student = new Student(
                request.name().trim(),
                request.email().trim()
        );
        Student saved = studentDAO.save(student);
        return StudentResponse.fromEntity(saved);
    }

    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = findStudentOrThrow(id);
        validateRequest(request);

        student.setName(request.name().trim());
        student.setEmail(request.email().trim());

        Student updated = studentDAO.save(student);
        return StudentResponse.fromEntity(updated);
    }

    public StudentResponse patchStudent(Long id, StudentRequest request) {
        Student student = findStudentOrThrow(id);
        request.applyTo(student);
        Student updated = studentDAO.save(student);
        return StudentResponse.fromEntity(updated);
    }

    public void deleteStudent(Long id) {
        findStudentOrThrow(id);
        studentDAO.deleteById(id);
    }

    private Student findStudentOrThrow(Long id) {
        return studentDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sinh viên với ID: " + id));
    }

    private void validateRequest(StudentRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dữ liệu sinh viên không được để trống");
        }
        if (request.name() == null || request.name().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên sinh viên không được để trống");
        }
        if (request.email() == null || request.email().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email sinh viên không được để trống");
        }
    }
}

