package com.studentgradecalculator.dao;

import com.studentgradecalculator.model.Student;

import java.util.List;

public interface StudentDao {
    void initialize();

    int save(Student student);

    void update(Student student);

    void delete(int studentId);

    List<Student> findAll();
}
