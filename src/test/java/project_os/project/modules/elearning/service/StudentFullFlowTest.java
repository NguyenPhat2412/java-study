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
class StudentFullFlowTest {
    @Mock StudentDAO studentDAO;
    @InjectMocks StudentServiceImpl studentService;

    @Test void getAllAndSearchReturnMappedResponses() {
        Student student = new Student("Nguyen Van A", "a@example.com");
        student.setId(1L);
        when(studentDAO.findAll()).thenReturn(List.of(student));
        when(studentDAO.findByKeyword("nguyen")).thenReturn(List.of(student));

        assertEquals(1, studentService.getStudents(null).size());
        assertEquals(1, studentService.getStudents("nguyen").size());
        verify(studentDAO).findAll();
        verify(studentDAO).findByKeyword("nguyen");
    }

    @Test void createAndUpdateTrimValues() {
        Student saved = new Student("Nguyen Van A", "a@example.com");
        saved.setId(3L);
        when(studentDAO.save(any(Student.class))).thenReturn(saved);

        StudentWrapper response = studentService.createStudent(
                new StudentWrapper(" Nguyen Van A ", " a@example.com "));
        assertEquals(3L, response.getId());
        verify(studentDAO).save(argThat(s -> s.getName().equals("Nguyen Van A")
                && s.getEmail().equals("a@example.com")));

        when(studentDAO.findById(3L)).thenReturn(Optional.of(saved));
        when(studentDAO.save(saved)).thenReturn(saved);
        studentService.updateStudent(3L, new StudentWrapper(" B ", " b@example.com "));
        assertEquals("B", saved.getName());
        assertEquals("b@example.com", saved.getEmail());
    }

    @Test void patchPreservesMissingFieldsAndDeleteUsesId() {
        Student student = new Student("A", "a@example.com");
        student.setId(1L);
        when(studentDAO.findById(1L)).thenReturn(Optional.of(student));
        when(studentDAO.save(student)).thenReturn(student);

        StudentWrapper response = studentService.patchStudent(1L,
                new StudentWrapper(" B ", null));
        assertEquals("B", response.getName());
        assertEquals("a@example.com", response.getEmail());

        studentService.deleteStudent(1L);
        verify(studentDAO).deleteById(1L);
    }

    @Test void invalidRequestsAndMissingStudentAreRejected() {
        assertThrows(ResponseStatusException.class, () -> studentService.createStudent(
                new StudentWrapper("", "a@example.com")));
        assertThrows(ResponseStatusException.class, () -> studentService.createStudent(
                new StudentWrapper("A", "")));
        when(studentDAO.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> studentService.getStudentById(99L));
        verify(studentDAO, never()).save(any());
    }
}
