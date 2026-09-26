package project_os.project.modules.elearning.dao.course.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import project_os.project.modules.elearning.dao.course.CourseDAO;
import project_os.project.modules.elearning.model.Course;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseDAOImpl implements CourseDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public CourseDAOImpl() {
    }

    public CourseDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Course> findAll() {
        TypedQuery<Course> query = entityManager.createNamedQuery("Course.findAll", Course.class);
        return query.getResultList();
    }

    @Override
    public Optional<Course> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Course course = entityManager.find(Course.class, id);
        return Optional.ofNullable(course);
    }

    @Override
    public List<Course> searchCourses(String keyword) {
        TypedQuery<Course> query = entityManager.createNamedQuery("Course.searchCourses", Course.class);
        query.setParameter("keyword", keyword != null ? keyword.trim() : "");
        return query.getResultList();
    }

    @Override
    public Course save(Course course) {
        if (course.getId() == null) {
            entityManager.persist(course);
            return course;
        } else {
            return entityManager.merge(course);
        }
    }

    @Override
    public void delete(Course course) {
        if (course != null) {
            if (entityManager.contains(course)) {
                entityManager.remove(course);
            } else {
                Course managed = entityManager.find(Course.class, course.getId());
                if (managed != null) {
                    entityManager.remove(managed);
                }
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(course -> entityManager.remove(course));
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        Long count = entityManager.createNamedQuery("Course.countById", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count != null && count > 0;
    }
}

