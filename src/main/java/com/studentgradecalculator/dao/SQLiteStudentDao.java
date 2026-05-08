package com.studentgradecalculator.dao;

import com.studentgradecalculator.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteStudentDao implements StudentDao {
    private final String dbUrl;

    public SQLiteStudentDao(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    @Override
    public void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    roll_number TEXT NOT NULL UNIQUE,
                    math INTEGER NOT NULL,
                    science INTEGER NOT NULL,
                    english INTEGER NOT NULL,
                    history INTEGER NOT NULL,
                    computer_science INTEGER NOT NULL
                )
                """;
        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to initialize database at " + dbUrl, exception);
        }
    }

    @Override
    public int save(Student student) {
        String sql = """
                INSERT INTO students (name, roll_number, math, science, english, history, computer_science)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindStudent(statement, student);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to save student", exception);
        }
    }

    @Override
    public void update(Student student) {
        String sql = """
                UPDATE students
                SET name = ?, roll_number = ?, math = ?, science = ?, english = ?, history = ?, computer_science = ?
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindStudent(statement, student);
            statement.setInt(8, student.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update student", exception);
        }
    }

    @Override
    public void delete(int studentId) {
        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE id = ?")) {
            statement.setInt(1, studentId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to delete student", exception);
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, name, roll_number, math, science, english, history, computer_science FROM students";
        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("roll_number"),
                        rs.getInt("math"),
                        rs.getInt("science"),
                        rs.getInt("english"),
                        rs.getInt("history"),
                        rs.getInt("computer_science")
                ));
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to fetch students", exception);
        }
        return students;
    }

    private void bindStudent(PreparedStatement statement, Student student) throws SQLException {
        statement.setString(1, student.getName());
        statement.setString(2, student.getRollNumber());
        statement.setInt(3, student.getMath());
        statement.setInt(4, student.getScience());
        statement.setInt(5, student.getEnglish());
        statement.setInt(6, student.getHistory());
        statement.setInt(7, student.getComputerScience());
    }
}
