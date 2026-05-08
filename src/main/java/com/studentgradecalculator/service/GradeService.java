package com.studentgradecalculator.service;

import com.studentgradecalculator.model.Student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class GradeService {
    public static final int SUBJECT_COUNT = 5;

    public void validateStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student is required");
        }
        if (student.getName() == null || !student.getName().trim().matches("[A-Za-z ]{2,50}")) {
            throw new IllegalArgumentException("Name must be 2-50 letters/spaces");
        }
        if (student.getRollNumber() == null || student.getRollNumber().trim().isBlank()) {
            throw new IllegalArgumentException("Roll number is required");
        }
        validateMark(student.getMath(), "Math");
        validateMark(student.getScience(), "Science");
        validateMark(student.getEnglish(), "English");
        validateMark(student.getHistory(), "History");
        validateMark(student.getComputerScience(), "Computer Science");
    }

    public void calculateStudentResult(Student student) {
        Objects.requireNonNull(student, "student");
        double percentage = round(student.getTotalMarks() * 100.0 / (SUBJECT_COUNT * 100));
        student.setPercentage(percentage);
        student.setGrade(calculateGrade(percentage));
    }

    public void assignRanks(List<Student> students) {
        students.sort(Comparator.comparingDouble(Student::getPercentage).reversed());
        int rank = 1;
        for (Student student : students) {
            student.setRank(rank++);
        }
    }

    public String calculateGrade(double percentage) {
        if (percentage >= 80) return "A";
        if (percentage >= 60) return "B";
        if (percentage >= 40) return "C";
        return "F";
    }

    public boolean isSubjectPass(int mark) {
        return mark >= 40;
    }

    private static void validateMark(int mark, String subject) {
        if (mark < 0 || mark > 100) {
            throw new IllegalArgumentException(subject + " mark must be between 0 and 100");
        }
    }

    private static double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
