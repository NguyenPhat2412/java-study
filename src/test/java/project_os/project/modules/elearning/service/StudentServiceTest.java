package project_os.project.modules.elearning.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import project_os.project.modules.elearning.dao.student.StudentDAO;
import project_os.project.modules.elearning.dto.student.StudentRequest;
import project_os.project.modules.elearning.dto.student.StudentResponse;
import project_os.project.modules.elearning.model.Student;

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
    private StudentService studentService;

    @Test
    void testGetStudents_All() {
        Student s1 = new Student("Nguyen Van A", "a@example.com");
        s1.setId(1L);
        Student s2 = new Student("Tran Thi B", "b@example.com");
        s2.setId(2L);

        when(studentDAO.findAll()).thenReturn(List.of(s1, s2));

        List<StudentResponse> result = studentService.getStudents(null);

        assertEquals(2, result.size());
        assertEquals("Nguyen Van A", result.get(0).name());
        verify(studentDAO, times(1)).findAll();
    }

    @Test
    void testGetStudents_WithKeyword() {
        Student s1 = new Student("Nguyen Van A", "a@example.com");
        s1.setId(1L);

        when(studentDAO.findByKeyword("Nguyen")).thenReturn(List.of(s1));

        List<StudentResponse> result = studentService.getStudents("Nguyen");

        assertEquals(1, result.size());
        assertEquals("Nguyen Van A", result.get(0).name());
        verify(studentDAO, times(1)).findByKeyword("Nguyen");
    }

    @Test
    void testGetStudentById_Success() {
        Student s1 = new Student("Nguyen Van A", "a@example.com");
        s1.setId(1L);

        when(studentDAO.findById(1L)).thenReturn(Optional.of(s1));

        StudentResponse result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Nguyen Van A", result.name());
    }

    @Test
    void testGetStudentById_NotFound_ThrowsException() {
        when(studentDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> studentService.getStudentById(99L));
    }

    @Test
    void testCreateStudent_Success() {
        StudentRequest req = new StudentRequest("Nguyen Van A", "a@example.com");
        Student saved = new Student("Nguyen Van A", "a@example.com");
        saved.setId(1L);

        when(studentDAO.save(any(Student.class))).thenReturn(saved);

        StudentResponse res = studentService.createStudent(req);

        assertEquals(1L, res.id());
        assertEquals("Nguyen Van A", res.name());
        verify(studentDAO, times(1)).save(any(Student.class));
    }

    @Test
    void testCreateStudent_InvalidInput_ThrowsException() {
        StudentRequest emptyName = new StudentRequest("", "a@example.com");
        assertThrows(ResponseStatusException.class, () -> studentService.createStudent(emptyName));

        StudentRequest emptyEmail = new StudentRequest("Nguyen", "");
        assertThrows(ResponseStatusException.class, () -> studentService.createStudent(emptyEmail));
    }

    @Test
    void testUpdateStudent_Success() {
        Student existing = new Student("Nguyen Van A", "a@example.com");
        existing.setId(1L);

        StudentRequest updateReq = new StudentRequest("Nguyen Van B", "b@example.com");
        Student updated = new Student("Nguyen Van B", "b@example.com");
        updated.setId(1L);

        when(studentDAO.findById(1L)).thenReturn(Optional.of(existing));
        when(studentDAO.save(existing)).thenReturn(updated);

        StudentResponse res = studentService.updateStudent(1L, updateReq);

        assertEquals("Nguyen Van B", res.name());
        assertEquals("b@example.com", res.email());
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

