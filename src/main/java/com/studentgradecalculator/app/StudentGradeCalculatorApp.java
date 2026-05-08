package com.studentgradecalculator.app;

import com.studentgradecalculator.controller.MainController;
import com.studentgradecalculator.dao.SQLiteStudentDao;
import com.studentgradecalculator.service.GradeService;
import com.studentgradecalculator.service.StudentService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;

public class StudentGradeCalculatorApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/studentgradecalculator/main-view.fxml"));
        BorderPane root = loader.load();

        MainController controller = loader.getController();
        String dbUrl = "jdbc:sqlite:student_grades.db";
        controller.setStudentService(new StudentService(new SQLiteStudentDao(dbUrl), new GradeService()));

        Scene scene = new Scene(root, 1220, 760);
        scene.getStylesheets().add(getClass().getResource("/com/studentgradecalculator/styles.css").toExternalForm());

        stage.setTitle("Student Grade Calculator - Desktop");
        stage.setScene(scene);
        stage.show();

        String screenshotPath = System.getProperty("app.screenshot");
        if (screenshotPath == null || screenshotPath.isBlank()) {
            screenshotPath = System.getenv("APP_SCREENSHOT");
        }
        if (screenshotPath != null && !screenshotPath.isBlank()) {
            final String outputPath = screenshotPath;
            Platform.runLater(() -> {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(scene.snapshot(null), null), "png", new File(outputPath));
                } catch (Exception ignored) {
                } finally {
                    Platform.exit();
                }
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
