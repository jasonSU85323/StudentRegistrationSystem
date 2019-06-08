package src;
import java.util.*;
import java.sql.*;

public class ElectiveStage implements Course {
  private String mandatory;
  private String subjectname;
  private String time;
  private String location;
  private String academicscore;
  private String teacher;
  private String upperlimit;
  private String lowerlimit;
  private String currentlimit;

  private CourseConditions courseconditions;
  private WaitingList waitinglist;

  public ElectiveStage(String mandatory, String subjectname, String time, String location, String academicscore, String teacher, String upperlimit, String lowerlimit, String currentlimit){
    this.mandatory = mandatory;
    this.subjectname = subjectname;
    this.time = time;
    this.location = location;
    this.academicscore = academicscore;
    this.teacher = teacher;
    this.upperlimit = upperlimit;
    this.lowerlimit = lowerlimit;
    this.currentlimit = currentlimit;

    courseconditions = new CourseConditions(subjectname);
    waitinglist = new WaitingList(subjectname);

  }

  public String get_mandatory(){
    return mandatory;
  }
  public String get_subjectname(){
    return subjectname;
  }
  public String get_time(){
    return time;
  }
  public String get_location(){
    return location;
  }
  public String get_academicscore(){
    return academicscore;
  }
  public String get_teacher(){
    return teacher;
  }
  public String get_upperlimit(){
    return upperlimit;
  }
  public String get_lowerlimit(){
    return lowerlimit;
  }
  public String get_currentlimit(){
    return currentlimit;
  }

  public CourseConditions get_courseconditions(){
    return courseconditions;
  }
  public WaitingList get_waitinglist(){
    return waitinglist;
  }
}
