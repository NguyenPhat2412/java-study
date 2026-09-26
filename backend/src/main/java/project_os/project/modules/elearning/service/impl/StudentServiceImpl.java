package project_os.project.modules.elearning.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project_os.project.modules.elearning.common.CoreContants;
import project_os.project.modules.elearning.common.SystemException;
import project_os.project.modules.elearning.dao.student.StudentDAO;
import project_os.project.modules.elearning.service.StudentService;
import project_os.project.modules.elearning.model.Student;
import project_os.project.modules.elearning.wrapper.StudentWrapper;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;

    public StudentServiceImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentWrapper> getStudents(String keyword) {
        List<Student> students;
        if (keyword != null && !keyword.trim().isEmpty()) {
            students = studentDAO.findByKeyword(keyword.trim());
        } else {
            students = studentDAO.findAll();
        }
        return students.stream().map(StudentWrapper::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentWrapper getStudentById(Long id) {
        Student student = findStudentOrThrow(id);
        return StudentWrapper.fromEntity(student);
    }

    @Override
    public StudentWrapper createStudent(StudentWrapper wrapper) {
        validate(wrapper);

        Student student = new Student();
        mapWrapperToEntity(wrapper, student);

        Student saved = studentDAO.save(student);
        return StudentWrapper.fromEntity(saved);
    }

    @Override
    public StudentWrapper updateStudent(Long id, StudentWrapper wrapper) {
        Student student = findStudentOrThrow(id);
        validate(wrapper);

        mapWrapperToEntity(wrapper, student);

        Student updated = studentDAO.save(student);
        return StudentWrapper.fromEntity(updated);
    }

    @Override
    public StudentWrapper patchStudent(Long id, StudentWrapper wrapper) {
        Student student = findStudentOrThrow(id);
        if (wrapper == null) {
            throw SystemException.badRequest("Dữ liệu không được để trống");
        }

        if (wrapper.getName() != null && !wrapper.getName().trim().isEmpty()) {
            student.setName(wrapper.getName().trim());
        }
        if (wrapper.getEmail() != null && !wrapper.getEmail().trim().isEmpty()) {
            student.setEmail(wrapper.getEmail().trim());
        }

        Student updated = studentDAO.save(student);
        return StudentWrapper.fromEntity(updated);
    }

    @Override
    public void deleteStudent(Long id) {
        findStudentOrThrow(id);
        studentDAO.deleteById(id);
    }

    private void validate(StudentWrapper wrapper) {
        if (wrapper == null) {
            throw SystemException.badRequest("Dữ liệu không được để trống");
        }
        if (wrapper.getName() == null || wrapper.getName().trim().isEmpty()) {
            throw SystemException.badRequest("Tên sinh viên không được để trống");
        }
        if (wrapper.getEmail() == null || wrapper.getEmail().trim().isEmpty()) {
            throw SystemException.badRequest("Email sinh viên không được để trống");
        }
    }

    private void mapWrapperToEntity(StudentWrapper wrapper, Student student) {
        student.setName(wrapper.getName().trim());
        student.setEmail(wrapper.getEmail().trim());
    }

    private Student findStudentOrThrow(Long id) {
        return studentDAO.findById(id)
                .orElseThrow(() -> SystemException.notFound(CoreContants.STUDENT_NOT_FOUND + " với ID: " + id));
    }
}