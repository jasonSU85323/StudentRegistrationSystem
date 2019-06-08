package src;
import java.util.*;
import java.sql.*;

public class AcademicAffairsOffice {
  private static String now_schoolyear = "107";
  private static String now_semester = "1";
  private static String next_schoolyear = "107";
  private static String next_semester = "2";
  public AcademicAffairsOffice(){

  }

  public String get_now_schoolyear(){
    return now_schoolyear;
  }
  public String get_now_semester(){
    return now_semester;
  }
  public String get_next_schoolyear(){
    return next_schoolyear;
  }
  public String get_next_semester(){
    return next_semester;
  }

}
