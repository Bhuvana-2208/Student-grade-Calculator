package com.studentgradecalculator.service;

import com.studentgradecalculator.model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeServiceTest {
    private final GradeService gradeService = new GradeService();

    @Test
    void shouldCalculatePercentageAndGradeBoundaries() {
        Student student = new Student(null, "Alice Johnson", "R01", 80, 80, 80, 80, 80);
        gradeService.calculateStudentResult(student);
        assertEquals(80.0, student.getPercentage());
        assertEquals("A", student.getGrade());

        student = new Student(null, "Bob Smith", "R02", 60, 60, 60, 60, 60);
        gradeService.calculateStudentResult(student);
        assertEquals("B", student.getGrade());

        student = new Student(null, "Cara Jones", "R03", 40, 40, 40, 40, 40);
        gradeService.calculateStudentResult(student);
        assertEquals("C", student.getGrade());

        student = new Student(null, "Dan Brown", "R04", 39, 39, 39, 39, 39);
        gradeService.calculateStudentResult(student);
        assertEquals("F", student.getGrade());
    }

    @Test
    void shouldValidateMarksRange() {
        Student invalid = new Student(null, "Alice Johnson", "R09", 101, 20, 30, 40, 50);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> gradeService.validateStudent(invalid));
        assertTrue(ex.getMessage().contains("Math"));
    }
}
