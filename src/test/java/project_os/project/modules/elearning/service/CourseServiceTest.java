package project_os.project.modules.elearning.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import project_os.project.modules.elearning.dto.CourseRequest;
import project_os.project.modules.elearning.dto.CourseResponse;
import project_os.project.modules.elearning.model.Course;
import project_os.project.modules.elearning.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    // lấy toàn bộ khoá học
    @Test
    void testGetCourses_All() {
        Course course1 = new Course("CNTT", "Nguyễn Văn Phát", "Drone AI", true);
        course1.setId(1L);
        Course course2 = new Course("Điện Tử", "Trần Thị Mai", "IoT UAV", true);
        course2.setId(2L);

        when(courseRepository.findAllByOrderByIdDesc()).thenReturn(List.of(course1, course2));

        List<CourseResponse> result = courseService.getCourses(null);

        assertEquals(2, result.size());
        assertEquals("CNTT", result.get(0).department());
        assertEquals("Điện Tử", result.get(1).department());
        verify(courseRepository, times(1)).findAllByOrderByIdDesc();
    }

    @Test
    void testGetCourses_WithKeyword() {
        Course course = new Course("CNTT", "Nguyễn Văn Phát", "Drone AI", true);
        course.setId(1L);

        when(courseRepository.searchCourses("Phát")).thenReturn(List.of(course));

        List<CourseResponse> result = courseService.getCourses("Phát");

        assertEquals(1, result.size());
        assertEquals("Nguyễn Văn Phát", result.get(0).student());
        verify(courseRepository, times(1)).searchCourses("Phát");
    }

    @Test
    void testGetCourseById_Success() {
        Course course = new Course("CNTT", "Nguyễn Văn Phát", "Drone AI", true);
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseResponse result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Nguyễn Văn Phát", result.student());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> courseService.getCourseById(99L));
        verify(courseRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateCourse_Success() {
        CourseRequest request = new CourseRequest("CNTT", "Lê Hoàng", "Khung UAV", true);
        Course saved = new Course("CNTT", "Lê Hoàng", "Khung UAV", true);
        saved.setId(10L);

        when(courseRepository.save(any(Course.class))).thenReturn(saved);

        CourseResponse result = courseService.createCourse(request);

        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals("CNTT", result.department());
        assertEquals("Lê Hoàng", result.student());
        assertTrue(result.isStatus());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testCreateCourse_EmptyDepartment_ThrowsException() {
        CourseRequest request = new CourseRequest("", "Lê Hoàng", "Khung UAV", true);

        assertThrows(ResponseStatusException.class, () -> courseService.createCourse(request));
        verify(courseRepository, never()).save(any());
    }

    @Test
    void testUpdateCourse_Success() {
        Course existing = new Course("CNTT", "Nguyễn Văn Phát", "Drone", true);
        existing.setId(1L);

        CourseRequest updateRequest = new CourseRequest("Cơ Khí", "Nguyễn Văn Phát", "Vỏ UAV", false);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any(Course.class))).thenReturn(existing);

        CourseResponse result = courseService.updateCourse(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Cơ Khí", result.department());
        assertEquals("Vỏ UAV", result.favourite());
        assertFalse(result.isStatus());
        verify(courseRepository, times(1)).save(existing);
    }

    @Test
    void testDeleteCourse_Success() {
        Course existing = new Course("CNTT", "Nguyễn Văn Phát", "Drone", true);
        existing.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(courseRepository).delete(existing);

        assertDoesNotThrow(() -> courseService.deleteCourse(1L));
        verify(courseRepository, times(1)).delete(existing);
    }
}
