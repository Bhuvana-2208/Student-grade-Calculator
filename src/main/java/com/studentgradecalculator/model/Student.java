package com.studentgradecalculator.model;

public class Student {
    private Integer id;
    private String name;
    private String rollNumber;
    private int math;
    private int science;
    private int english;
    private int history;
    private int computerScience;
    private double percentage;
    private String grade;
    private int rank;

    public Student() {
    }

    public Student(Integer id, String name, String rollNumber, int math, int science, int english, int history, int computerScience) {
        this.id = id;
        this.name = name;
        this.rollNumber = rollNumber;
        this.math = math;
        this.science = science;
        this.english = english;
        this.history = history;
        this.computerScience = computerScience;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public int getMath() { return math; }
    public void setMath(int math) { this.math = math; }
    public int getScience() { return science; }
    public void setScience(int science) { this.science = science; }
    public int getEnglish() { return english; }
    public void setEnglish(int english) { this.english = english; }
    public int getHistory() { return history; }
    public void setHistory(int history) { this.history = history; }
    public int getComputerScience() { return computerScience; }
    public void setComputerScience(int computerScience) { this.computerScience = computerScience; }
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public int getTotalMarks() {
        return math + science + english + history + computerScience;
    }

    public boolean isPass() {
        return grade != null && !"F".equals(grade);
    }
}
