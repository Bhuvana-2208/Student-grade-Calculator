package com.studentgradecalculator.model;

import java.util.Map;

public record ClassSummary(int totalStudents,
                           double passRate,
                           double classAverage,
                           String topperName,
                           double topperPercentage,
                           Map<String, String> subjectToppers,
                           Map<String, Long> gradeDistribution) {
}
