package project_os.project.modules.elearning.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project_os.project.modules.elearning.dao.student.StudentDAOImpl;
import project_os.project.modules.elearning.model.Student;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Student> studentQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private StudentDAOImpl studentDAO;

    @Test
    void testFindAll() {
        Student s1 = new Student("Nguyen Van A", "a@example.com");
        s1.setId(1L);
        Student s2 = new Student("Tran Thi B", "b@example.com");
        s2.setId(2L);

        when(entityManager.createNamedQuery("Student.findAll", Student.class))
                .thenReturn(studentQuery);
        when(studentQuery.getResultList()).thenReturn(List.of(s2, s1));

        List<Student> result = studentDAO.findAll();

        assertEquals(2, result.size());
        assertEquals("Tran Thi B", result.get(0).getName());
        verify(entityManager, times(1)).createNamedQuery("Student.findAll", Student.class);
    }

    @Test
    void testFindById_Found() {
        Student s1 = new Student("Nguyen Van A", "a@example.com");
        s1.setId(1L);

        when(entityManager.find(Student.class, 1L)).thenReturn(s1);

        Optional<Student> result = studentDAO.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Nguyen Van A", result.get().getName());
        verify(entityManager, times(1)).find(Student.class, 1L);
    }

    @Test
    void testFindById_NotFound() {
        when(entityManager.find(Student.class, 99L)).thenReturn(null);

        Optional<Student> result = studentDAO.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindById_NullId() {
        Optional<Student> result = studentDAO.findById(null);
        assertFalse(result.isPresent());
        verify(entityManager, never()).find(any(), any());
    }

    @Test
    void testFindByKeyword() {
        Student s = new Student("Nguyen Van A", "a@example.com");
        when(entityManager.createNamedQuery("Student.findByKeyword", Student.class)).thenReturn(studentQuery);
        when(studentQuery.setParameter("keyword", "Van")).thenReturn(studentQuery);
        when(studentQuery.getResultList()).thenReturn(List.of(s));

        List<Student> result = studentDAO.findByKeyword(" Van ");

        assertEquals(1, result.size());
        assertEquals("Nguyen Van A", result.get(0).getName());
        verify(studentQuery, times(1)).setParameter("keyword", "Van");
    }

    @Test
    void testSave_NewEntity_CallsPersist() {
        Student s = new Student("Nguyen Van A", "a@example.com");

        Student result = studentDAO.save(s);

        verify(entityManager, times(1)).persist(s);
        assertEquals(s, result);
    }

    @Test
    void testSave_ExistingEntity_CallsMerge() {
        Student s = new Student("Nguyen Van A", "a@example.com");
        s.setId(1L);
        Student merged = new Student("Nguyen Van A Updated", "a@example.com");
        merged.setId(1L);

        when(entityManager.merge(s)).thenReturn(merged);

        Student result = studentDAO.save(s);

        verify(entityManager, times(1)).merge(s);
        assertEquals(merged, result);
    }

    @Test
    void testDeleteById_CallsRemoveWhenFound() {
        Student s = new Student("Nguyen Van A", "a@example.com");
        s.setId(1L);
        when(entityManager.find(Student.class, 1L)).thenReturn(s);

        studentDAO.deleteById(1L);

        verify(entityManager, times(1)).remove(s);
    }

    @Test
    void testExistsById_True() {
        when(entityManager.createNamedQuery("Student.countById", Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter("id", 1L)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        assertTrue(studentDAO.existsById(1L));
    }

    @Test
    void testExistsById_FalseWhenNull() {
        assertFalse(studentDAO.existsById(null));
    }
}
