# Student-grade-Calculator

JavaFX desktop application for managing and analyzing student grades across 5 subjects.

## Features
- Add/edit/delete students with validation (name + marks in 0-100 range)
- Auto-calculated percentage, grade (A/B/C/F), and class ranking
- Report card view with pass/fail per subject
- Class summary metrics + subject toppers + grade distribution chart
- SQLite persistence (`student_grades.db`)
- CSV export (`student_reports.csv`)

## Tech Stack
- Java 17
- JavaFX (FXML + CSS)
- SQLite (JDBC)
- Maven

## Run
```bash
mvn clean test
mvn javafx:run
```
