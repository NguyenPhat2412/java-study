package project_os.project.modules.elearning.dao.student;

import project_os.project.modules.elearning.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    List<Student> findAll();
    Optional<Student> findById(Long id);
    List<Student> findByKeyword(String keyword);
    Student save(Student student);
    void deleteById(Long id);
    boolean existsById(Long id);
}

