package project_os.project.modules.elearning.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project_os.project.modules.elearning.dto.student.StudentRequest;
import project_os.project.modules.elearning.dto.student.StudentResponse;
import project_os.project.modules.elearning.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void testGetStudents() throws Exception {
        StudentResponse res = new StudentResponse(1L, "Nguyen Van A", "a@example.com");
        when(studentService.getStudents(null)).thenReturn(List.of(res));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Nguyen Van A"));

        verify(studentService, times(1)).getStudents(null);
    }

    @Test
    void testGetStudentById() throws Exception {
        StudentResponse res = new StudentResponse(1L, "Nguyen Van A", "a@example.com");
        when(studentService.getStudentById(1L)).thenReturn(res);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nguyen Van A"));
    }

    @Test
    void testCreateStudent() throws Exception {
        StudentResponse res = new StudentResponse(1L, "Nguyen Van A", "a@example.com");

        when(studentService.createStudent(any(StudentRequest.class))).thenReturn(res);

        String jsonPayload = """
                {
                    "name": "Nguyen Van A",
                    "email": "a@example.com"
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nguyen Van A"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        StudentResponse res = new StudentResponse(1L, "Nguyen Van B", "b@example.com");

        when(studentService.updateStudent(eq(1L), any(StudentRequest.class))).thenReturn(res);

        String jsonPayload = """
                {
                    "name": "Nguyen Van B",
                    "email": "b@example.com"
                }
                """;

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nguyen Van B"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
