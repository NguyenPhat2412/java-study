package project_os.project.modules.elearning.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project_os.project.modules.elearning.service.StudentService;
import project_os.project.modules.elearning.wrapper.StudentWrapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentRestController studentRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentRestController).build();
    }

    @Test
    void testGetStudents() throws Exception {
        StudentWrapper res = new StudentWrapper(1L, "Nguyen Van A", "a@example.com");
        when(studentService.getStudents(null)).thenReturn(List.of(res));

        mockMvc.perform(get(RestEndpoint.STUDENTS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Nguyen Van A"));

        verify(studentService, times(1)).getStudents(null);
    }

    @Test
    void testGetStudentById() throws Exception {
        StudentWrapper res = new StudentWrapper(1L, "Nguyen Van A", "a@example.com");
        when(studentService.getStudentById(1L)).thenReturn(res);

        mockMvc.perform(get(RestEndpoint.STUDENTS + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nguyen Van A"));
    }

    @Test
    void testCreateStudent() throws Exception {
        StudentWrapper res = new StudentWrapper(1L, "Nguyen Van A", "a@example.com");

        when(studentService.createStudent(any(StudentWrapper.class))).thenReturn(res);

        String jsonPayload = """
                {
                    "name": "Nguyen Van A",
                    "email": "a@example.com"
                }
                """;

        mockMvc.perform(post(RestEndpoint.STUDENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nguyen Van A"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        StudentWrapper res = new StudentWrapper(1L, "Nguyen Van B", "b@example.com");

        when(studentService.updateStudent(eq(1L), any(StudentWrapper.class))).thenReturn(res);

        String jsonPayload = """
                {
                    "name": "Nguyen Van B",
                    "email": "b@example.com"
                }
                """;

        mockMvc.perform(put(RestEndpoint.STUDENTS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nguyen Van B"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(delete(RestEndpoint.STUDENTS + "/1"))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
