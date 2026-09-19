package project_os.project.modules.elearning.dao.student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import project_os.project.modules.elearning.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentDAOImpl implements StudentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public StudentDAOImpl() {
    }

    public StudentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Student> findAll() {
        TypedQuery<Student> query = entityManager.createNamedQuery("Student.findAll", Student.class);
        return query.getResultList();
    }

    @Override
    public Optional<Student> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Student student = entityManager.find(Student.class, id);
        return Optional.ofNullable(student);
    }

    @Override
    public List<Student> findByKeyword(String keyword) {
        TypedQuery<Student> query = entityManager.createNamedQuery("Student.findByKeyword", Student.class);
        query.setParameter("keyword", keyword.trim());
        return query.getResultList();
    }

    @Override
    public Student save(Student student) {
        if (student.getId() == null) {
            entityManager.persist(student);
            return student;
        } else {
            return entityManager.merge(student);
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(student -> entityManager.remove(student));
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        Long count = entityManager.createNamedQuery("Student.countById", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count != null && count > 0;
    }
}

