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
import project_os.project.modules.elearning.service.CourseService;
import project_os.project.modules.elearning.wrapper.CourseWrapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseRestController courseRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseRestController).build();
    }

    @Test
    void testGetCourses_Returns200AndJsonArray() throws Exception {
        CourseWrapper res = new CourseWrapper(1L, "CNTT", "Nguyễn Văn Phát", "Drone AI", true);
        when(courseService.getCourses(null)).thenReturn(List.of(res));

        mockMvc.perform(get(RestEndpoint.COURSES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].department").value("CNTT"))
                .andExpect(jsonPath("$[0].student").value("Nguyễn Văn Phát"));

        verify(courseService, times(1)).getCourses(null);
    }

    @Test
    void testGetCourseById_Returns200() throws Exception {
        CourseWrapper res = new CourseWrapper(1L, "CNTT", "Nguyễn Văn Phát", "Drone AI", true);
        when(courseService.getCourseById(1L)).thenReturn(res);

        mockMvc.perform(get(RestEndpoint.COURSES + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.student").value("Nguyễn Văn Phát"));

        verify(courseService, times(1)).getCourseById(1L);
    }

    @Test
    void testCreateCourse_Returns201Created() throws Exception {
        CourseWrapper created = new CourseWrapper(5L, "Điện Tử", "Lê Thị B", "IoT", true);
        when(courseService.createCourse(any(CourseWrapper.class))).thenReturn(created);

        String jsonPayload = """
                {
                    "department": "Điện Tử",
                    "student": "Lê Thị B",
                    "favourite": "IoT",
                    "isStatus": true
                }
                """;

        mockMvc.perform(post(RestEndpoint.COURSES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.department").value("Điện Tử"));

        verify(courseService, times(1)).createCourse(any(CourseWrapper.class));
    }

    @Test
    void testUpdateCourse_Returns200() throws Exception {
        CourseWrapper updated = new CourseWrapper(1L, "Cơ Khí", "Nguyễn Văn Phát", "Vỏ UAV", true);
        when(courseService.updateCourse(eq(1L), any(CourseWrapper.class))).thenReturn(updated);

        String jsonPayload = """
                {
                    "department": "Cơ Khí",
                    "student": "Nguyễn Văn Phát",
                    "favourite": "Vỏ UAV",
                    "isStatus": true
                }
                """;

        mockMvc.perform(put(RestEndpoint.COURSES + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("Cơ Khí"));

        verify(courseService, times(1)).updateCourse(eq(1L), any(CourseWrapper.class));
    }

    @Test
    void testPatchCourse_Returns200() throws Exception {
        CourseWrapper patched = new CourseWrapper(1L, "CNTT", "Nguyễn Văn Phát", "Drone AI", false);
        when(courseService.patchCourse(eq(1L), any(CourseWrapper.class))).thenReturn(patched);

        String jsonPayload = """
                {
                    "isStatus": false
                }
                """;

        mockMvc.perform(patch(RestEndpoint.COURSES + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isStatus").value(false));

        verify(courseService, times(1)).patchCourse(eq(1L), any(CourseWrapper.class));
    }

    @Test
    void testDeleteCourse_Returns204NoContent() throws Exception {
        doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(delete(RestEndpoint.COURSES + "/1"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).deleteCourse(1L);
    }
}
