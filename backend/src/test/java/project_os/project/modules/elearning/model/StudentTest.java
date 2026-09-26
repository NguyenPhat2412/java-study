package project_os.project.modules.elearning.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentTest {

    @Test
    void constructorAndUpdateNormalizeValues() {
        Student student = new Student(" Nguyen Van A ", " a@example.com ");

        student.updateContactInformation(" Tran Thi B ", " b@example.com ");

        assertEquals("Tran Thi B", student.getName());
        assertEquals("b@example.com", student.getEmail());
    }

    @Test
    void blankEmailIsRejectedByTheEntity() {
        assertThrows(IllegalArgumentException.class,
                () -> new Student("Nguyen Van A", " "));
    }
}
