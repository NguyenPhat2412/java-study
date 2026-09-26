package project_os.project.modules.elearning.service;

import project_os.project.modules.elearning.wrapper.StudentWrapper;

import java.util.List;

public interface StudentService {
    List<StudentWrapper> getStudents(String keyword);
    StudentWrapper getStudentById(Long id);
    StudentWrapper createStudent(StudentWrapper studentWrapper);
    StudentWrapper updateStudent(Long id, StudentWrapper studentWrapper);
    StudentWrapper patchStudent(Long id, StudentWrapper studentWrapper);
    void deleteStudent(Long id);
}