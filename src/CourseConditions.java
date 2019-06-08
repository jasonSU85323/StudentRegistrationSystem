package src;
import java.util.*;
import java.sql.*;

public class CourseConditions {
  private String course;
  private HashMap<String, String> conditions = new HashMap<String, String>();

  public CourseConditions(String course){
    this.course = course;
    MysqlFunction mysql = new MysqlFunction();
    Connection conn;
    try{
        // 打開鏈結
        conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

        // 開始查詢
        String sql;
        sql = "SELECT * FROM courseconditions WHERE subjectname=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,course);
        ResultSet rs = ps.executeQuery();

        // 展開結果
        int count = 0;
        while(rs.next()){
            conditions.put(rs.getString("ifcourse"), rs.getString("ifgrade"));
        }
        // 完成后关闭
        rs.close();
        conn.close();

    } catch(Exception e){
        System.out.println(e);
    }
  }

  public boolean PassAndFail(LearningPlan LP){
    Iterator iterator = conditions.keySet().iterator();
    while (iterator.hasNext()){
       String key = (String)iterator.next();
       int val    =  Integer.valueOf(conditions.get(key));
       // System.out.println(key + "--" + String.valueOf(val));
       if (!LP.PassingTheResults(key, val)) {
         return false;
       }
    }
    return true;
  }

}
