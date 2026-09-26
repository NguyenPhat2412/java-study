package project_os.project.modules.elearning.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project_os.project.modules.elearning.dao.course.impl.CourseDAOImpl;
import project_os.project.modules.elearning.model.Course;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Course> courseQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private CourseDAOImpl courseDAO;

    @Test
    void testFindAll() {
        Course c1 = new Course("CNTT", "Nguyen Van A", "Drone", true);
        c1.setId(1L);
        Course c2 = new Course("Dien Tu", "Tran Thi B", "IoT", true);
        c2.setId(2L);

        when(entityManager.createNamedQuery("Course.findAll", Course.class)).thenReturn(courseQuery);
        when(courseQuery.getResultList()).thenReturn(List.of(c2, c1));

        List<Course> result = courseDAO.findAll();

        assertEquals(2, result.size());
        assertEquals("Dien Tu", result.get(0).getDepartment());
        verify(entityManager, times(1)).createNamedQuery("Course.findAll", Course.class);
    }

    @Test
    void testFindById_Found() {
        Course c = new Course("CNTT", "Nguyen Van A", "Drone", true);
        c.setId(1L);

        when(entityManager.find(Course.class, 1L)).thenReturn(c);

        Optional<Course> result = courseDAO.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("CNTT", result.get().getDepartment());
        verify(entityManager, times(1)).find(Course.class, 1L);
    }

    @Test
    void testFindById_NotFound() {
        when(entityManager.find(Course.class, 99L)).thenReturn(null);

        Optional<Course> result = courseDAO.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindById_NullId() {
        Optional<Course> result = courseDAO.findById(null);
        assertFalse(result.isPresent());
        verify(entityManager, never()).find(any(), any());
    }

    @Test
    void testSearchCourses() {
        Course c = new Course("CNTT", "Nguyen Van A", "Drone", true);
        c.setId(1L);

        when(entityManager.createNamedQuery("Course.searchCourses", Course.class)).thenReturn(courseQuery);
        when(courseQuery.setParameter("keyword", "CNTT")).thenReturn(courseQuery);
        when(courseQuery.getResultList()).thenReturn(List.of(c));

        List<Course> result = courseDAO.searchCourses(" CNTT ");

        assertEquals(1, result.size());
        assertEquals("CNTT", result.get(0).getDepartment());
        verify(courseQuery, times(1)).setParameter("keyword", "CNTT");
    }

    @Test
    void testSave_NewEntity() {
        Course c = new Course("CNTT", "Nguyen Van A", "Drone", true);

        Course saved = courseDAO.save(c);

        verify(entityManager, times(1)).persist(c);
        assertEquals(c, saved);
    }

    @Test
    void testSave_ExistingEntity() {
        Course c = new Course("CNTT", "Nguyen Van A", "Drone", true);
        c.setId(1L);
        Course merged = new Course("CNTT Updated", "Nguyen Van A", "Drone", true);
        merged.setId(1L);

        when(entityManager.merge(c)).thenReturn(merged);

        Course result = courseDAO.save(c);

        verify(entityManager, times(1)).merge(c);
        assertEquals(merged, result);
    }

    @Test
    void testDelete_Contained() {
        Course c = new Course("CNTT", "Nguyen Van A", "Drone", true);
        c.setId(1L);
        when(entityManager.contains(c)).thenReturn(true);

        courseDAO.delete(c);

        verify(entityManager, times(1)).remove(c);
    }

    @Test
    void testDelete_NotContained_Found() {
        Course c = new Course("CNTT", "Nguyen Van A", "Drone", true);
        c.setId(1L);
        when(entityManager.contains(c)).thenReturn(false);
        when(entityManager.find(Course.class, 1L)).thenReturn(c);

        courseDAO.delete(c);

        verify(entityManager, times(1)).remove(c);
    }

    @Test
    void testDeleteById() {
        Course c = new Course("CNTT", "Nguyen Van A", "Drone", true);
        c.setId(1L);
        when(entityManager.find(Course.class, 1L)).thenReturn(c);

        courseDAO.deleteById(1L);

        verify(entityManager, times(1)).remove(c);
    }

    @Test
    void testExistsById_True() {
        when(entityManager.createNamedQuery("Course.countById", Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter("id", 1L)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        assertTrue(courseDAO.existsById(1L));
    }

    @Test
    void testExistsById_FalseWhenNull() {
        assertFalse(courseDAO.existsById(null));
    }
}

