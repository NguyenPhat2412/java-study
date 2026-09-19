package project_os.project.modules.elearning.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import project_os.project.modules.elearning.dao.course.CourseDAO;
import project_os.project.modules.elearning.dto.course.CourseRequest;
import project_os.project.modules.elearning.dto.course.CourseResponse;
import project_os.project.modules.elearning.model.Course;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseDAO courseDAO;

    @InjectMocks
    private CourseService courseService;

    // lấy toàn bộ khoá học
    @Test
    void testGetCourses_All() {
        Course course1 = new Course("CNTT", "Nguyễn Văn Phát", "Drone AI", true);
        course1.setId(1L);
        Course course2 = new Course("Điện Tử", "Trần Thị Mai", "IoT UAV", true);
        course2.setId(2L);

        when(courseDAO.findAll()).thenReturn(List.of(course1, course2));

        List<CourseResponse> result = courseService.getCourses(null);

        assertEquals(2, result.size());
        assertEquals("CNTT", result.get(0).department());
        assertEquals("Điện Tử", result.get(1).department());
        verify(courseDAO, times(1)).findAll();
    }

    @Test
    void testGetCourses_WithKeyword() {
        Course course = new Course("CNTT", "Nguyễn Văn Phát", "Drone AI", true);
        course.setId(1L);

        when(courseDAO.searchCourses("Phát")).thenReturn(List.of(course));

        List<CourseResponse> result = courseService.getCourses("Phát");

        assertEquals(1, result.size());
        assertEquals("Nguyễn Văn Phát", result.get(0).student());
        verify(courseDAO, times(1)).searchCourses("Phát");
    }

    @Test
    void testGetCourseById_Success() {
        Course course = new Course("CNTT", "Nguyễn Văn Phát", "Drone AI", true);
        course.setId(1L);

        when(courseDAO.findById(1L)).thenReturn(Optional.of(course));

        CourseResponse result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Nguyễn Văn Phát", result.student());
        verify(courseDAO, times(1)).findById(1L);
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> courseService.getCourseById(99L));
        verify(courseDAO, times(1)).findById(99L);
    }

    @Test
    void testCreateCourse_Success() {
        CourseRequest request = new CourseRequest("CNTT", "Lê Hoàng", "Khung UAV", true);
        Course saved = new Course("CNTT", "Lê Hoàng", "Khung UAV", true);
        saved.setId(10L);

        when(courseDAO.save(any(Course.class))).thenReturn(saved);

        CourseResponse result = courseService.createCourse(request);

        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals("CNTT", result.department());
        assertEquals("Lê Hoàng", result.student());
        assertTrue(result.isStatus());
        verify(courseDAO, times(1)).save(any(Course.class));
    }

    @Test
    void testCreateCourse_EmptyDepartment_ThrowsException() {
        CourseRequest request = new CourseRequest("", "Lê Hoàng", "Khung UAV", true);

        assertThrows(ResponseStatusException.class, () -> courseService.createCourse(request));
        verify(courseDAO, never()).save(any());
    }

    @Test
    void testUpdateCourse_Success() {
        Course existing = new Course("CNTT", "Nguyễn Văn Phát", "Drone", true);
        existing.setId(1L);

        CourseRequest updateRequest = new CourseRequest("Cơ Khí", "Nguyễn Văn Phát", "Vỏ UAV", false);

        when(courseDAO.findById(1L)).thenReturn(Optional.of(existing));
        when(courseDAO.save(any(Course.class))).thenReturn(existing);

        CourseResponse result = courseService.updateCourse(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Cơ Khí", result.department());
        assertEquals("Vỏ UAV", result.favourite());
        assertFalse(result.isStatus());
        verify(courseDAO, times(1)).save(existing);
    }

    @Test
    void testDeleteCourse_Success() {
        Course existing = new Course("CNTT", "Nguyễn Văn Phát", "Drone", true);
        existing.setId(1L);

        when(courseDAO.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(courseDAO).delete(existing);

        assertDoesNotThrow(() -> courseService.deleteCourse(1L));
        verify(courseDAO, times(1)).delete(existing);
    }
}
