package project_os.project.modules.elearning.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for domain behavior. These tests do not start Spring or a
 * database; they exercise the Course object directly.
 */
class CourseTest {

    @Test
    void constructorNormalizesValuesAndUsesSafeStatusDefault() {
        Course course = new Course(" CNTT ", " Nguyen Van A ", " Drone ", null);

        assertEquals("CNTT", course.getDepartment());
        assertEquals("Nguyen Van A", course.getStudent());
        assertEquals("Drone", course.getFavourite());
        assertTrue(course.getIsStatus());
    }

    @Test
    void updateInformationKeepsCourseValid() {
        Course course = new Course("CNTT", "A", "Drone", true);

        course.updateInformation(" Co Khi ", " B ", null);

        assertEquals("Co Khi", course.getDepartment());
        assertEquals("B", course.getStudent());
        assertEquals("Drone", course.getFavourite());
    }

    @Test
    void emptyRequiredValueIsRejectedByTheEntity() {
        assertThrows(IllegalArgumentException.class,
                () -> new Course("", "Student", "", true));
    }
}
