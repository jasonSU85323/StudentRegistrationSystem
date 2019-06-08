package src;
import java.util.*;
import java.sql.*;

public class ElectiveCourse {
  private Student student;
  private LearningPlan LP;
  private HashSet<ElectiveStage> courselist = new HashSet<ElectiveStage>();

  public ElectiveCourse(Student student){
    this.student = student;
    this.LP = new LearningPlan(student.get_id());
    updataCourselist();
  }

  public void updataCourselist(){
      courselist.clear();
      MysqlFunction mysql = new MysqlFunction();
      Connection conn;

      try{
          // 打開鏈結
          conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

          // 開始查詢
          String sql;
          sql = "SELECT * FROM startlist";
          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();

          // 展開結果
          int count = 0;
          while(rs.next()){
              String a = rs.getString("mandatory");
              String b = rs.getString("subjectname");
              String c = rs.getString("time");
              String d = rs.getString("location");
              String e = rs.getString("academicscore");
              String f = rs.getString("teacher");
              String g = rs.getString("upperlimit");
              String h = rs.getString("lowerlimit");
              String i = rs.getString("currentlimit");
              ElectiveStage temp = new ElectiveStage(a, b, c, d, e, f, g, h, i);
              courselist.add(temp);
          }
          // 完成后关闭
          rs.close();
          conn.close();

      } catch(Exception e){
          System.out.println(e);
      }
  }
  public void ElectiveStageshow(){
      updataCourselist();
      Formatter formatter = new Formatter(System.out);
      Iterator clist = courselist.iterator();
      while (clist.hasNext()) {
          ElectiveStage list = (ElectiveStage)clist.next();

          String staticstr;
          if (LP.CourseStatus(list.get_subjectname())){
            staticstr = "[*]";
          }else{
            staticstr = "[ ]";
          }

          formatter.format("%-5s %-2s %-10s \t %-6s %-4s %-4s %3s %-4s %-4s %-4s \n",
            staticstr,
            list.get_mandatory(),
            list.get_subjectname(),
            list.get_time(),
            list.get_location(),
            list.get_academicscore(),
            list.get_teacher(),
            list.get_upperlimit(),
            list.get_lowerlimit(),
            list.get_currentlimit());
      }
  }

  public void AddCourse(String course){
      updataCourselist();
      Formatter formatter = new Formatter(System.out);
      Iterator clist = courselist.iterator();
      while (clist.hasNext()) {
          ElectiveStage list = (ElectiveStage)clist.next();
          if (list.get_subjectname().equals(course)) {
              if (LP.CourseStatus(course)) {
                  System.out.println("已選此課程!!");
                  return;
              }else{
                  boolean part1 = list.get_courseconditions().PassAndFail(LP);
                  boolean part2 = list.get_waitinglist().IsVacancy();
                  if (part1 && part2) {
                      System.out.println("你可以選，準備加選...");
                      SqlAddLearningplan(list);
                      return;
                  }else if (!part1) {
                      System.out.println("你不符合選課條件!!");
                      return;
                  }else {
                      System.out.println("人數已滿，準備進入候補名單...");
                      SqlAddWaitinglist(list);
                      return;
                  }
              }
          }
      }
      System.out.println("無此課程!!");
  }
  public void DelCourse(String course){
      updataCourselist();
      Formatter formatter = new Formatter(System.out);
      Iterator clist = courselist.iterator();
      while (clist.hasNext()) {
          ElectiveStage list = (ElectiveStage)clist.next();
          // System.out.println(list.get_subjectname());
          if (list.get_subjectname().equals(course)) {
              if (LP.CourseStatus(course)) {
                System.out.println("你有選這課程，準備退課...");
                SqlDelLearningplan(list);
                SQLWaitingListMakeUp(list);
                return;
              }
          }
      }
      System.out.println("你本來就沒選這個課!!");
  }
  public void DelWaitinglist(String course){
    updataCourselist();
    Formatter formatter = new Formatter(System.out);
    Iterator clist = courselist.iterator();
    while (clist.hasNext()) {
        ElectiveStage list = (ElectiveStage)clist.next();
        // System.out.println(list.get_subjectname());
        if (list.get_subjectname().equals(course)) {
            if (list.get_waitinglist().IsList(student.get_id())) {
              System.out.println("你有在這號補名單，準備退出...");
              SqlDelWaitinglist(list);
              return;
            }
        }
    }
    System.out.println("你本來就沒在這號補名單!!");
  }

  public void SqlAddLearningplan(ElectiveStage course){
    AcademicAffairsOffice AAO = new AcademicAffairsOffice();
    MysqlFunction mysql = new MysqlFunction();
    Connection conn;
    try{
        // 打開鏈結
        conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

        // 開始新增
        String sql = "INSERT INTO learningplan(sid, mandatory, schoolyear, semester, subjectname, academicscore, teacher) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,student.get_id());
        ps.setString(2,course.get_mandatory());
        ps.setString(3,AAO.get_next_schoolyear());
        ps.setString(4,AAO.get_next_semester());
        ps.setString(5,course.get_subjectname());
        ps.setString(6,course.get_academicscore());
        ps.setString(7,course.get_teacher());

        if(ps.executeUpdate()>0){
            System.out.println("加選「" + course.get_subjectname() + "」成功");
        }else{
            System.out.println("加選「" + course.get_subjectname() + "」失敗");
        }
        // 開始修改
        String sql2 = "UPDATE startlist SET currentlimit=currentlimit+1 WHERE subjectname=?";
        PreparedStatement ps2 = conn.prepareStatement(sql2);
        ps2.setString(1,course.get_subjectname());
        if(ps2.executeUpdate()>0){
            System.out.println("「" + course.get_subjectname() + "」人數加1執行成功");
        }else{
            System.out.println("「" + course.get_subjectname() + "」人數加1執行失敗");
        }
        // 完成后关闭
        conn.close();

    } catch(Exception e){
        System.out.println(e);
    }
  }

  public void SqlAddWaitinglist(ElectiveStage course){
      AcademicAffairsOffice AAO = new AcademicAffairsOffice();
      MysqlFunction mysql = new MysqlFunction();
      Connection conn;
      try{
          // 打開鏈結
          conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

          // 開始查詢
          String sql1 = "SELECT * FROM waitinglist WHERE subjectname=? AND student=?";
          PreparedStatement ps1 = conn.prepareStatement(sql1);
          ps1.setString(1, course.get_subjectname());
          ps1.setString(2, student.get_id());
          ResultSet rs1 = ps1.executeQuery();
          // 展開結果
          while(rs1.next()){
              System.out.println("學生「" + student.get_id() + "」已在候選名單中");
              return;
          }


          // 開始新增
          String sql2 = "INSERT INTO waitinglist(schoolyear, semester, subjectname, student, time) VALUES(?, ?, ?, ?, NOW())";
          PreparedStatement ps2 = conn.prepareStatement(sql2);
          ps2.setString(1,AAO.get_next_schoolyear());
          ps2.setString(2,AAO.get_next_semester());
          ps2.setString(3,course.get_subjectname());
          ps2.setString(4,student.get_id());

          if(ps2.executeUpdate()>0){
              System.out.println("學生進入「" + course.get_subjectname() + "」候選名單成功");
          }else{
              System.out.println("學生進入「" + course.get_subjectname() + "」候選名單失敗");
          }
          // 完成后关闭
          conn.close();

      } catch(Exception e){
          System.out.println(e);
      }
  }

  public void SqlDelLearningplan(ElectiveStage course){
      MysqlFunction mysql = new MysqlFunction();
      Connection conn;
      try{
          // 打開鏈結
          conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());
          // 開始摻除
          String sql;
          sql = "DELETE FROM learningplan WHERE sid=? AND subjectname=?";
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.setString(1,student.get_id());
          ps.setString(2,course.get_subjectname());

          if(ps.executeUpdate()>0){
              System.out.println("退選「" + course.get_subjectname() + "」成功");
          }else{
          		System.out.println("退選「" + course.get_subjectname() + "」失敗");
          }
          // 開始修改
          String sql2 = "UPDATE startlist SET currentlimit=currentlimit-1 WHERE subjectname=?";
          PreparedStatement ps2 = conn.prepareStatement(sql2);
          ps2.setString(1,course.get_subjectname());
          if(ps2.executeUpdate()>0){
              System.out.println("「" + course.get_subjectname() + "」人數減1執行成功");
          }else{
          		System.out.println("「" + course.get_subjectname() + "」人數減1執行成功");
          }
          // 完成后关闭
          conn.close();

      } catch(Exception e){
          System.out.println(e);
      }
  }

  public void SqlDelWaitinglist(ElectiveStage course){
      MysqlFunction mysql = new MysqlFunction();
      Connection conn;
      try{
          // 打開鏈結
          conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());
          // 開始摻除
          String sql;
          sql = "DELETE FROM waitinglist WHERE subjectname=? AND student=?";
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.setString(1,course.get_subjectname());
          ps.setString(2,student.get_id());

          if(ps.executeUpdate()>0){
              System.out.println("退出「" + course.get_subjectname() + "」課程候補名單成功");
          }else{
              System.out.println("退出「" + course.get_subjectname() + "」課程候補名單失敗");
          }
          // 完成后关闭
          conn.close();

      } catch(Exception e){
          System.out.println(e);
      }
  }

  public void SQLWaitingListMakeUp(ElectiveStage course){
    AcademicAffairsOffice AAO = new AcademicAffairsOffice();
    MysqlFunction mysql = new MysqlFunction();
    Connection conn;
    System.out.println("針對該「" + course.get_subjectname() + "」課程執行遞補動作...");
    try{
        // 打開鏈結
        conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());
        // 開始新增
        String sql = "SELECT * FROM waitinglist WHERE subjectname=? ORDER BY time ASC LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,course.get_subjectname());
        ResultSet rs = ps.executeQuery();
        // 展開結果
        while(rs.next()){
            System.out.println("學號「" + rs.getString("student") + "」開始遞補...");

            try{
                // 打開鏈結
                conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

                // 開始新增
                String sql1 = "INSERT INTO learningplan(sid, mandatory, schoolyear, semester, subjectname, academicscore, teacher) VALUES(?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps1 = conn.prepareStatement(sql1);
                ps1.setString(1,rs.getString("student"));
                ps1.setString(2,course.get_mandatory());
                ps1.setString(3,AAO.get_next_schoolyear());
                ps1.setString(4,AAO.get_next_semester());
                ps1.setString(5,course.get_subjectname());
                ps1.setString(6,course.get_academicscore());
                ps1.setString(7,course.get_teacher());

                if(ps1.executeUpdate()>0){
                    System.out.println("加選「" + course.get_subjectname() + "」成功");
                }else{
                    System.out.println("加選「" + course.get_subjectname() + "」失敗");
                }
                // 開始修改
                String sql2 = "UPDATE startlist SET currentlimit=currentlimit+1 WHERE subjectname=?";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setString(1,course.get_subjectname());
                if(ps2.executeUpdate()>0){
                    System.out.println("「" + course.get_subjectname() + "」人數加1執行成功");
                }else{
                    System.out.println("「" + course.get_subjectname() + "」人數加1執行失敗");
                }
                // 開始摻除
                String sql3 = "DELETE FROM waitinglist WHERE student=?";
                PreparedStatement ps3 = conn.prepareStatement(sql3);
                ps3.setString(1,rs.getString("student"));

                if(ps3.executeUpdate()>0){
                    System.out.println("候補名單學生「" + rs.getString("student") + "」以剔除");
                }else{
                		System.out.println("候補名單學生「" + rs.getString("student") + "」剔除失敗");
                }


            } catch(Exception e){
                System.out.println(e);
            }

            System.out.println("學號「" + rs.getString("student") + "」遞補完成!!");
            // 完成后关闭
            rs.close();
            conn.close();
            return;
        }
        // 完成后关闭
        rs.close();
        conn.close();
        System.out.println("無遞補!!");
    } catch(Exception e){
        System.out.println(e);
    }
  }

}
