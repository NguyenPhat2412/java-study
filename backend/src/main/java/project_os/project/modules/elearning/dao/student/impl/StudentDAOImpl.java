package project_os.project.modules.elearning.dao.student.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import project_os.project.modules.elearning.dao.student.StudentDAO;
import project_os.project.modules.elearning.model.Student;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentDAOImpl implements StudentDAO {
    @PersistenceContext private EntityManager entityManager;

    @Override public List<Student> findAll() {
        return entityManager.createNamedQuery("Student.findAll", Student.class).getResultList();
    }
    @Override public Optional<Student> findById(Long id) {
        return id == null ? Optional.empty() : Optional.ofNullable(entityManager.find(Student.class, id));
    }
    @Override public List<Student> findByKeyword(String keyword) {
        String value = keyword == null ? "" : keyword.trim();
        TypedQuery<Student> query = entityManager.createNamedQuery("Student.findByKeyword", Student.class);
        return query.setParameter("keyword", value).getResultList();
    }
    @Override public Student save(Student student) {
        if (student.getId() == null) { entityManager.persist(student); return student; }
        return entityManager.merge(student);
    }
    @Override public void delete(Student student) {
        if (student == null) return;
        Student managed = entityManager.contains(student) ? student : entityManager.find(Student.class, student.getId());
        if (managed != null) entityManager.remove(managed);
    }
    @Override public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    @Override public boolean existsById(Long id) {
        if (id == null) return false;
        Long count = entityManager.createNamedQuery("Student.countById", Long.class)
                .setParameter("id", id).getSingleResult();
        return count > 0;
    }
}
