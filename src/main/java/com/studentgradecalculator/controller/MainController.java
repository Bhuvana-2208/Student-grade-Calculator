package com.studentgradecalculator.controller;

import com.studentgradecalculator.model.ClassSummary;
import com.studentgradecalculator.model.Student;
import com.studentgradecalculator.service.GradeService;
import com.studentgradecalculator.service.StudentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainController {
    private StudentService studentService;
    private final GradeService gradeService = new GradeService();

    @FXML private Label totalStudentsLabel;
    @FXML private Label statusLabel;

    @FXML private TextField nameField;
    @FXML private TextField rollField;
    @FXML private TextField mathField;
    @FXML private TextField scienceField;
    @FXML private TextField englishField;
    @FXML private TextField historyField;
    @FXML private TextField computerField;

    @FXML private TextField searchField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> rankCol;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Student, String> rollCol;
    @FXML private TableColumn<Student, String> mathCol;
    @FXML private TableColumn<Student, String> scienceCol;
    @FXML private TableColumn<Student, String> englishCol;
    @FXML private TableColumn<Student, String> historyCol;
    @FXML private TableColumn<Student, String> computerCol;
    @FXML private TableColumn<Student, String> percentCol;
    @FXML private TableColumn<Student, String> gradeCol;

    @FXML private TextArea reportArea;
    @FXML private Label summaryTotal;
    @FXML private Label summaryPassRate;
    @FXML private Label summaryAverage;
    @FXML private Label summaryTopper;
    @FXML private TextArea subjectTopperArea;
    @FXML private PieChart gradeChart;

    private List<Student> cachedStudents = List.of();
    private int reportIndex = 0;

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
        refreshAll();
    }

    @FXML
    public void initialize() {
        setupTable();
    }

    @FXML
    private void addStudent() {
        try {
            Student student = buildStudentFromForm();
            studentService.addStudent(student);
            clearForm();
            statusLabel.setText("Student added successfully");
            refreshAll();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void clearForm() {
        nameField.clear();
        rollField.clear();
        mathField.clear();
        scienceField.clear();
        englishField.clear();
        historyField.clear();
        computerField.clear();
    }

    @FXML
    private void deleteSelectedStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a student to delete");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            studentService.deleteStudent(selected.getId());
            statusLabel.setText("Student deleted");
            refreshAll();
        }
    }

    @FXML
    private void updateSelectedStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a student to update");
            return;
        }
        try {
            Student updated = buildStudentFromForm();
            updated.setId(selected.getId());
            studentService.updateStudent(updated);
            statusLabel.setText("Student updated");
            refreshAll();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void loadSelectedIntoForm() {
        Student s = studentTable.getSelectionModel().getSelectedItem();
        if (s == null) return;
        nameField.setText(s.getName());
        rollField.setText(s.getRollNumber());
        mathField.setText(String.valueOf(s.getMath()));
        scienceField.setText(String.valueOf(s.getScience()));
        englishField.setText(String.valueOf(s.getEnglish()));
        historyField.setText(String.valueOf(s.getHistory()));
        computerField.setText(String.valueOf(s.getComputerScience()));
    }

    @FXML
    private void applyFilter() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        List<Student> filtered = cachedStudents.stream()
                .filter(s -> s.getName().toLowerCase().contains(query) || s.getRollNumber().toLowerCase().contains(query))
                .collect(Collectors.toList());
        studentTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void previousReport() {
        if (cachedStudents.isEmpty()) return;
        reportIndex = Math.max(0, reportIndex - 1);
        renderReport();
    }

    @FXML
    private void nextReport() {
        if (cachedStudents.isEmpty()) return;
        reportIndex = Math.min(cachedStudents.size() - 1, reportIndex + 1);
        renderReport();
    }

    @FXML
    private void exportReportsCsv() {
        try (FileWriter writer = new FileWriter("student_reports.csv")) {
            writer.write("Rank,Name,Roll,Math,Science,English,History,Computer,Percentage,Grade\n");
            for (Student s : cachedStudents) {
                writer.write(String.format("%d,%s,%s,%d,%d,%d,%d,%d,%.2f,%s%n",
                        s.getRank(), s.getName(), s.getRollNumber(), s.getMath(), s.getScience(), s.getEnglish(), s.getHistory(), s.getComputerScience(), s.getPercentage(), s.getGrade()));
            }
            statusLabel.setText("Exported to student_reports.csv");
        } catch (IOException e) {
            statusLabel.setText("Export failed: " + e.getMessage());
        }
    }

    private void setupTable() {
        rankCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getRank())));
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        rollCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRollNumber()));
        mathCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getMath())));
        scienceCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getScience())));
        englishCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getEnglish())));
        historyCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getHistory())));
        computerCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getComputerScience())));
        percentCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getPercentage())));
        gradeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGrade()));
    }

    private Student buildStudentFromForm() {
        Student student = new Student(
                null,
                nameField.getText(),
                rollField.getText(),
                parseMark(mathField, "Math"),
                parseMark(scienceField, "Science"),
                parseMark(englishField, "English"),
                parseMark(historyField, "History"),
                parseMark(computerField, "Computer Science")
        );
        gradeService.validateStudent(student);
        return student;
    }

    private int parseMark(TextField field, String subject) {
        try {
            int value = Integer.parseInt(field.getText().trim());
            if (value < 0 || value > 100) {
                throw new IllegalArgumentException(subject + " mark must be 0-100");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + subject + " mark");
        }
    }

    private void refreshAll() {
        cachedStudents = studentService.getRankedStudents();
        studentTable.setItems(FXCollections.observableArrayList(cachedStudents));
        totalStudentsLabel.setText(String.valueOf(cachedStudents.size()));
        reportIndex = Math.min(reportIndex, Math.max(0, cachedStudents.size() - 1));
        renderReport();
        renderSummary();
    }

    private void renderReport() {
        if (cachedStudents.isEmpty()) {
            reportArea.setText("No students available.");
            return;
        }
        Student s = cachedStudents.get(reportIndex);
        reportArea.setText(String.format(
                "Student: %s\nRoll No: %s\nRank: %d\n\n" +
                        "Math: %d (%s)\nScience: %d (%s)\nEnglish: %d (%s)\nHistory: %d (%s)\nComputer: %d (%s)\n\n" +
                        "Total: %d/500\nPercentage: %.2f%%\nGrade: %s\nResult: %s",
                s.getName(), s.getRollNumber(), s.getRank(),
                s.getMath(), gradeService.isSubjectPass(s.getMath()) ? "Pass" : "Fail",
                s.getScience(), gradeService.isSubjectPass(s.getScience()) ? "Pass" : "Fail",
                s.getEnglish(), gradeService.isSubjectPass(s.getEnglish()) ? "Pass" : "Fail",
                s.getHistory(), gradeService.isSubjectPass(s.getHistory()) ? "Pass" : "Fail",
                s.getComputerScience(), gradeService.isSubjectPass(s.getComputerScience()) ? "Pass" : "Fail",
                s.getTotalMarks(), s.getPercentage(), s.getGrade(), s.isPass() ? "Pass" : "Fail"
        ));
    }

    private void renderSummary() {
        ClassSummary summary = studentService.getClassSummary();
        summaryTotal.setText(String.valueOf(summary.totalStudents()));
        summaryPassRate.setText(summary.passRate() + "%");
        summaryAverage.setText(summary.classAverage() + "%");
        summaryTopper.setText(summary.topperName() + " (" + summary.topperPercentage() + "%)");

        String subjectText = summary.subjectToppers().entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\n"));
        subjectTopperArea.setText(subjectText);

        gradeChart.getData().setAll(
                new PieChart.Data("A", summary.gradeDistribution().getOrDefault("A", 0L)),
                new PieChart.Data("B", summary.gradeDistribution().getOrDefault("B", 0L)),
                new PieChart.Data("C", summary.gradeDistribution().getOrDefault("C", 0L)),
                new PieChart.Data("F", summary.gradeDistribution().getOrDefault("F", 0L))
        );
    }
}
