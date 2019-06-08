package src;
import java.util.*;
import java.sql.*;

public interface Course {
  String mandatory = "";
  String subjectname = "";
  String time = "";
  String location = "";
  String academicscore = "";
  String teacher = "";

  String get_mandatory();
  String get_subjectname();
  String get_time();
  String get_location();
  String get_academicscore();
  String get_teacher();

}
