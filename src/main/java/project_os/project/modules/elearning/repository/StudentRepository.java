package project_os.project.modules.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_os.project.modules.elearning.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByKeyword(@Param("keyword") String keyword);

    List<Student> findAllByOrderByIdDesc();

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);
}

