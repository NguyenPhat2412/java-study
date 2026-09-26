package project_os.project.modules.elearning.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import project_os.project.modules.elearning.dao.course.CourseDAO;
import project_os.project.modules.elearning.service.impl.CourseServiceImpl;
import project_os.project.modules.elearning.model.Course;
import project_os.project.modules.elearning.wrapper.CourseWrapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseFullFlowTest {
    @Mock CourseDAO courseDAO;
    @InjectMocks CourseServiceImpl courseService;

    @Test void getAllAndSearchReturnMappedResponses() {
        Course course = new Course(" CNTT ", " Nguyen Van A ", " AI ", true);
        course.setId(1L);
        when(courseDAO.findAll()).thenReturn(List.of(course));
        when(courseDAO.searchCourses("java")).thenReturn(List.of(course));

        assertEquals(1, courseService.getCourses(null).size());
        assertEquals(1L, courseService.getCourses(null).get(0).getId());
        assertEquals(1, courseService.getCourses("java").size());
        verify(courseDAO, times(2)).findAll();
        verify(courseDAO).searchCourses("java");
    }

    @Test void createTrimsValuesAndUsesDefaults() {
        Course saved = new Course("CNTT", "Nguyen Van A", "", true);
        saved.setId(5L);
        when(courseDAO.save(any(Course.class))).thenReturn(saved);

        CourseWrapper response = courseService.createCourse(
                new CourseWrapper(null, " CNTT ", " Nguyen Van A ", null, null));

        assertEquals(5L, response.getId());
        verify(courseDAO).save(argThat(c -> c.getDepartment().equals("CNTT")
                && c.getStudent().equals("Nguyen Van A")
                && c.getFavourite().equals("") && Boolean.TRUE.equals(c.getIsStatus())));
    }

    @Test void updateAndPatchChangeOnlyExpectedFields() {
        Course course = new Course("CNTT", "A", "Drone", true);
        course.setId(1L);
        when(courseDAO.findById(1L)).thenReturn(Optional.of(course));
        when(courseDAO.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CourseWrapper updated = courseService.updateCourse(1L,
                new CourseWrapper(null, " Co Khi ", " B ", " UAV ", false));
        assertEquals("Co Khi", updated.getDepartment());
        assertFalse(updated.getIsStatus());

        CourseWrapper patched = courseService.patchCourse(1L,
                new CourseWrapper(null, null, null, null, true));
        assertEquals("Co Khi", patched.getDepartment());
        assertEquals("B", patched.getStudent());
        assertTrue(patched.getIsStatus());
    }

    @Test void invalidCreateAndMissingCourseAreRejected() {
        assertThrows(ResponseStatusException.class, () -> courseService.createCourse(
                new CourseWrapper(null, "", "A", "", true)));
        when(courseDAO.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> courseService.getCourseById(99L));
        assertThrows(ResponseStatusException.class, () -> courseService.deleteCourse(99L));
        verify(courseDAO, never()).save(any());
    }
}
