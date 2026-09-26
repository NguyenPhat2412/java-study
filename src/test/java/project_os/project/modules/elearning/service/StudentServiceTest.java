package project_os.project.modules.elearning.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import project_os.project.modules.elearning.dao.student.StudentDAO;
import project_os.project.modules.elearning.service.impl.StudentServiceImpl;
import project_os.project.modules.elearning.model.Student;
import project_os.project.modules.elearning.wrapper.StudentWrapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDAO studentDAO;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void testGetStudents_All() {
        Student s1 = new Student("Nguyen Van A", "a@example.com");
        s1.setId(1L);
        Student s2 = new Student("Tran Thi B", "b@example.com");
        s2.setId(2L);

        when(studentDAO.findAll()).thenReturn(List.of(s1, s2));

        List<StudentWrapper> result = studentService.getStudents(null);

        assertEquals(2, result.size());
        assertEquals("Nguyen Van A", result.get(0).getName());
        verify(studentDAO, times(1)).findAll();
    }

    @Test
    void testGetStudents_WithKeyword() {
        Student s1 = new Student("Nguyen Van A", "a@example.com");
        s1.setId(1L);

        when(studentDAO.findByKeyword("Nguyen")).thenReturn(List.of(s1));

        List<StudentWrapper> result = studentService.getStudents("Nguyen");

        assertEquals(1, result.size());
        assertEquals("Nguyen Van A", result.get(0).getName());
        verify(studentDAO, times(1)).findByKeyword("Nguyen");
    }

    @Test
    void testGetStudentById_Success() {
        Student s1 = new Student("Nguyen Van A", "a@example.com");
        s1.setId(1L);

        when(studentDAO.findById(1L)).thenReturn(Optional.of(s1));

        StudentWrapper result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nguyen Van A", result.getName());
    }

    @Test
    void testGetStudentById_NotFound_ThrowsException() {
        when(studentDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> studentService.getStudentById(99L));
    }

    @Test
    void testCreateStudent_Success() {
        StudentWrapper req = new StudentWrapper("Nguyen Van A", "a@example.com");
        Student saved = new Student("Nguyen Van A", "a@example.com");
        saved.setId(1L);

        when(studentDAO.save(any(Student.class))).thenReturn(saved);

        StudentWrapper res = studentService.createStudent(req);

        assertEquals(1L, res.getId());
        assertEquals("Nguyen Van A", res.getName());
        verify(studentDAO, times(1)).save(any(Student.class));
    }

    @Test
    void testCreateStudent_InvalidInput_ThrowsException() {
        StudentWrapper emptyName = new StudentWrapper("", "a@example.com");
        assertThrows(ResponseStatusException.class, () -> studentService.createStudent(emptyName));

        StudentWrapper emptyEmail = new StudentWrapper("Nguyen", "");
        assertThrows(ResponseStatusException.class, () -> studentService.createStudent(emptyEmail));
    }

    @Test
    void testUpdateStudent_Success() {
        Student existing = new Student("Nguyen Van A", "a@example.com");
        existing.setId(1L);

        StudentWrapper updateReq = new StudentWrapper("Nguyen Van B", "b@example.com");
        Student updated = new Student("Nguyen Van B", "b@example.com");
        updated.setId(1L);

        when(studentDAO.findById(1L)).thenReturn(Optional.of(existing));
        when(studentDAO.save(existing)).thenReturn(updated);

        StudentWrapper res = studentService.updateStudent(1L, updateReq);

        assertEquals("Nguyen Van B", res.getName());
        assertEquals("b@example.com", res.getEmail());
    }

    @Test
    void testDeleteStudent_Success() {
        Student existing = new Student("Nguyen Van A", "a@example.com");
        existing.setId(1L);

        when(studentDAO.findById(1L)).thenReturn(Optional.of(existing));

        studentService.deleteStudent(1L);

        verify(studentDAO, times(1)).deleteById(1L);
    }
}
