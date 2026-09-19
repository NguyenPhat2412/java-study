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
import project_os.project.modules.elearning.dto.CourseRequest;
import project_os.project.modules.elearning.dto.CourseResponse;
import project_os.project.modules.elearning.service.CourseService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void testGetCourses_Returns200AndJsonArray() throws Exception {
        CourseResponse res = new CourseResponse(1L, "CNTT", "Nguyễn Văn Phát", "Drone AI", true, LocalDateTime.now(), LocalDateTime.now());
        when(courseService.getCourses(null)).thenReturn(List.of(res));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].department").value("CNTT"))
                .andExpect(jsonPath("$[0].student").value("Nguyễn Văn Phát"));

        verify(courseService, times(1)).getCourses(null);
    }

    @Test
    void testGetCourseById_Returns200() throws Exception {
        CourseResponse res = new CourseResponse(1L, "CNTT", "Nguyễn Văn Phát", "Drone AI", true, LocalDateTime.now(), LocalDateTime.now());
        when(courseService.getCourseById(1L)).thenReturn(res);

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.student").value("Nguyễn Văn Phát"));

        verify(courseService, times(1)).getCourseById(1L);
    }

    @Test
    void testCreateCourse_Returns201Created() throws Exception {
        CourseResponse created = new CourseResponse(5L, "Điện Tử", "Lê Thị B", "IoT", true, LocalDateTime.now(), LocalDateTime.now());
        when(courseService.createCourse(any(CourseRequest.class))).thenReturn(created);

        String jsonPayload = """
                {
                    "department": "Điện Tử",
                    "student": "Lê Thị B",
                    "favourite": "IoT",
                    "isStatus": true
                }
                """;

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.department").value("Điện Tử"));

        verify(courseService, times(1)).createCourse(any(CourseRequest.class));
    }

    @Test
    void testUpdateCourse_Returns200() throws Exception {
        CourseResponse updated = new CourseResponse(1L, "Cơ Khí", "Nguyễn Văn Phát", "Vỏ UAV", true, LocalDateTime.now(), LocalDateTime.now());
        when(courseService.updateCourse(eq(1L), any(CourseRequest.class))).thenReturn(updated);

        String jsonPayload = """
                {
                    "department": "Cơ Khí",
                    "student": "Nguyễn Văn Phát",
                    "favourite": "Vỏ UAV",
                    "isStatus": true
                }
                """;

        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("Cơ Khí"));

        verify(courseService, times(1)).updateCourse(eq(1L), any(CourseRequest.class));
    }

    @Test
    void testPatchCourse_Returns200() throws Exception {
        CourseResponse patched = new CourseResponse(1L, "CNTT", "Nguyễn Văn Phát", "Drone AI", false, LocalDateTime.now(), LocalDateTime.now());
        when(courseService.patchCourse(eq(1L), any(CourseRequest.class))).thenReturn(patched);

        String jsonPayload = """
                {
                    "isStatus": false
                }
                """;

        mockMvc.perform(patch("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isStatus").value(false));

        verify(courseService, times(1)).patchCourse(eq(1L), any(CourseRequest.class));
    }

    @Test
    void testDeleteCourse_Returns204NoContent() throws Exception {
        doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).deleteCourse(1L);
    }
}
