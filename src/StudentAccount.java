package src;
import java.util.*;
import java.sql.*;

public class StudentAccount {
    private Student student;
    private LearningPlan learningPlan;

    public StudentAccount(Student student) {
        this.student = student;
        this.learningPlan = new LearningPlan(student.get_id());
    }

    public void show(){
        System.out.println("學號: " + student.get_id());
        System.out.println("姓名: " + student.get_name());
        System.out.println("學系: " + student.get_course());
        System.out.println("班別: " + student.get_department());
        System.out.println("年級: " + student.get_grade());
    }
    public Student get_Student(){
        return student;
    }
    public LearningPlan get_LearningPlan(){
        return learningPlan;
    }
}
