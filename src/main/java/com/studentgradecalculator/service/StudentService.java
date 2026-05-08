package com.studentgradecalculator.service;

import com.studentgradecalculator.dao.StudentDao;
import com.studentgradecalculator.model.ClassSummary;
import com.studentgradecalculator.model.Student;

import java.util.*;
import java.util.function.ToIntFunction;

public class StudentService {
    private final StudentDao studentDao;
    private final GradeService gradeService;

    public StudentService(StudentDao studentDao, GradeService gradeService) {
        this.studentDao = studentDao;
        this.gradeService = gradeService;
        this.studentDao.initialize();
    }

    public Student addStudent(Student student) {
        gradeService.validateStudent(student);
        gradeService.calculateStudentResult(student);
        int id = studentDao.save(student);
        student.setId(id);
        return student;
    }

    public void updateStudent(Student student) {
        gradeService.validateStudent(student);
        gradeService.calculateStudentResult(student);
        studentDao.update(student);
    }

    public void deleteStudent(int id) {
        studentDao.delete(id);
    }

    public List<Student> getRankedStudents() {
        List<Student> students = studentDao.findAll();
        students.forEach(gradeService::calculateStudentResult);
        gradeService.assignRanks(students);
        return students;
    }

    public ClassSummary getClassSummary() {
        List<Student> students = getRankedStudents();
        int total = students.size();
        if (total == 0) {
            return new ClassSummary(0, 0, 0, "N/A", 0, Map.of(), Map.of("A", 0L, "B", 0L, "C", 0L, "F", 0L));
        }

        long passCount = students.stream().filter(Student::isPass).count();
        double avg = students.stream().mapToDouble(Student::getPercentage).average().orElse(0);
        Student topper = students.get(0);

        Map<String, String> subjectToppers = new LinkedHashMap<>();
        subjectToppers.put("Math", topperFor(students, Student::getMath));
        subjectToppers.put("Science", topperFor(students, Student::getScience));
        subjectToppers.put("English", topperFor(students, Student::getEnglish));
        subjectToppers.put("History", topperFor(students, Student::getHistory));
        subjectToppers.put("Computer", topperFor(students, Student::getComputerScience));

        Map<String, Long> gradeDistribution = new LinkedHashMap<>();
        gradeDistribution.put("A", students.stream().filter(s -> "A".equals(s.getGrade())).count());
        gradeDistribution.put("B", students.stream().filter(s -> "B".equals(s.getGrade())).count());
        gradeDistribution.put("C", students.stream().filter(s -> "C".equals(s.getGrade())).count());
        gradeDistribution.put("F", students.stream().filter(s -> "F".equals(s.getGrade())).count());

        return new ClassSummary(
                total,
                round(passCount * 100.0 / total),
                round(avg),
                topper.getName(),
                topper.getPercentage(),
                subjectToppers,
                gradeDistribution
        );
    }

    private String topperFor(List<Student> students, ToIntFunction<Student> marksGetter) {
        Student topper = students.stream().max(Comparator.comparingInt(marksGetter)).orElse(null);
        return topper == null ? "N/A" : topper.getName() + " (" + marksGetter.applyAsInt(topper) + ")";
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
