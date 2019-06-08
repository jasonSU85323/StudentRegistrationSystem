package src;
import java.util.*;
import java.sql.*;

public class WaitingList {
    private String course;
    public WaitingList(String course){
        this.course = course;
    }

    public boolean IsVacancy(){
      MysqlFunction mysql = new MysqlFunction();
      Connection conn;
      try{
          // 打開鏈結
          conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

          // 開始查詢
          String sql;
          sql = "SELECT * FROM startlist WHERE subjectname=?";
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.setString(1,course);
          ResultSet rs = ps.executeQuery();

          // 展開結果
          int count = 0;
          while(rs.next()){
            int upperlimit   = Integer.valueOf(rs.getString("upperlimit"));
            int currentlimit = Integer.valueOf(rs.getString("currentlimit"));
            if (upperlimit>currentlimit) {
              rs.close();
              conn.close();
              return true;
            }
          }
          // 完成后关闭
          rs.close();
          conn.close();
          return false;

      } catch(Exception e){
          System.out.println(e);
      }
      return false;
    }

    public boolean IsList(String sid){
        MysqlFunction mysql = new MysqlFunction();
        Connection conn;
        try{
            // 打開鏈結
            conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

            // 開始查詢
            String sql;
            sql = "SELECT * FROM waitinglist WHERE subjectname=? AND student=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,course);
            ps.setString(2,sid);
            ResultSet rs = ps.executeQuery();

            // 展開結果
            while(rs.next()){
                return true;
            }
            // 完成后关闭
            rs.close();
            conn.close();

        } catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
}
