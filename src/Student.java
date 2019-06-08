package src;
import java.util.*;
import java.sql.*;

public class Student {
    private String id = null;
    private String name = null;
    private String course = null;
    private String department = null;
    private String grade = null;

    public Student(String Snum) {
      MysqlFunction mysql = new MysqlFunction();
      Dictionary Information = StudentInformation(Snum);

      id          = Information.get("id").toString();
      name        = Information.get("name").toString();
      course      = Information.get("course").toString();
      department  = Information.get("department").toString();
      grade       = Information.get("grade").toString();
    }

    public Dictionary StudentInformation(String Snum){
      MysqlFunction mysql = new MysqlFunction();
      Connection conn;
      Dictionary dict = new Hashtable();

      try{
        // 打開鏈結
        conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

        // 開始查詢
        String sql = "SELECT * FROM students WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,Snum);
        ResultSet rs = ps.executeQuery();

        // 展開結果

        while(rs.next()){
            dict.put("id",          rs.getString("id") );
            dict.put("name",        rs.getString("name") );
            dict.put("course",      rs.getString("course") );
            dict.put("department",  rs.getString("department") );
            dict.put("grade",       rs.getString("grade") );
            // System.out.println(rs.getString("id"));
        }
        // 完成后关闭
        rs.close();
        conn.close();
        return dict;
      } catch(Exception e){
        System.out.println(e);
      }
      return dict;
    }

    public void show(){
      System.out.println("學號: " + id);
      System.out.println("姓名: " + name);
      System.out.println("學系: " + course);
      System.out.println("班別: " + department);
      System.out.println("年級: " + grade);
    }
    public String get_id(){
        return id;
    }
    public String get_name(){
        return name;
    }
    public String get_course(){
        return course;
    }
    public String get_department(){
        return department;
    }
    public String get_grade(){
        return grade;
    }
}
