// javac -encoding UTF-8 Mymain.java
// java Mymain

import src.*;

import java.util.*;
import java.sql.*;

public class Mymain{
    public static void main(String[] args){

        System.out.println("※※※ Welcome to the Student Registration System. ※※※");
        InterfaceEntry LoginInterface = new InterfaceEntry();
        Student student = LoginInterface.Login();
        System.out.println("sign in suceesfully\n");

        StudentAccount studentUser = new StudentAccount( student );
        ElectiveCourse electivecourse = new ElectiveCourse( student );

        help();

        int number = 1;
        while(number!=0){

            System.out.print("\nPlease enter the service number: ");
            Scanner scanner = new Scanner(System.in);
            if (!scanner.hasNextInt()){
                System.out.println("Invalid input!!\n");
                continue;
            }
            number = scanner.nextInt();

            switch(number){
                case 1: // 修課紀錄
                    studentUser.get_LearningPlan().CourseListshow();
                    System.out.println();
                    break;
                case 2: // 歷年成績
                    studentUser.get_LearningPlan().AllGradeListshow();
                    System.out.println();
                    break;
                case 3: // 目前選課清單
                    studentUser.get_LearningPlan().ElectiveCourseListshow();
                    System.out.println();
                    break;
                case 4: // 目前各科選課情況
                    electivecourse.ElectiveStageshow();
                    break;
                case 5: // 加選課程
                    System.out.print("Please enter the course name: ");
                    scanner = new Scanner(System.in);
                    if (scanner.hasNextLine()){
                        String course1 = scanner.nextLine();
                        electivecourse.AddCourse(course1);
                        break;
                    }
                    System.out.println("Invalid input!!");
                    break;
                case 6: // 退選課程
                    System.out.print("Please enter the course name: ");
                    scanner = new Scanner(System.in);
                    if (scanner.hasNextLine()){
                        String course2 = scanner.nextLine();
                        electivecourse.DelCourse(course2);
                        break;
                    }
                    System.out.println("Invalid input!!");
                    break;
                case 7: // 退出候補名單
                    System.out.print("Please enter the course name: ");
                    scanner = new Scanner(System.in);
                    if (scanner.hasNextLine()){
                        String course3 = scanner.nextLine();
                        electivecourse.DelWaitinglist(course3);
                        break;
                    }
                    System.out.println("Invalid input!!");
                    break;
                case 8: // Help
                    help();
                    break;
                case 0: // 離開
                    break;
                default:
                    System.out.println("Invalid input!!");
            }
            System.out.println("");
        }
        System.out.print("By~ By~");
    }
    public static void help(){
        Formatter formatter = new Formatter(System.out);
        formatter.format("%s\t\t %s\t\t %s\t\t \n", "1:修課紀錄" ,"2:歷年成績" ,"3:目前選課清單");
        formatter.format("%s\t %s\t\t %s\t\t \n", "4:目前各科選課情況" ,"5:加選課程" ,"6:退選課程");
        formatter.format("%s\t\t %s\t\t\t %s \n", "7:退出候補名單", "8:Help", "0:離開");
    }
}
