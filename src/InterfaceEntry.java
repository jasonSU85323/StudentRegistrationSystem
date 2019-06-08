package src;
import java.util.*;
import java.sql.*;

public class InterfaceEntry {
  private String Snum;
  private String Spas;

  public InterfaceEntry(){
    Snum = "";
    Spas = "";
  }

  public Student Login(){
    Scanner scanner = new Scanner(System.in);

    System.out.println("Please enter the student number and password.");
    System.out.print("Student number   ： "); Snum = scanner.nextLine();
    System.out.print("Student password ： "); Spas = scanner.nextLine();

    // System.out.println(Snum + "--" + Spas);
    while(true){
      if ( StudentInquiry(Snum, Spas) ) {
        return new Student(Snum);
      }else {
        System.out.println("Please login again if you fail to log in.");
        System.out.print("Student number   ： "); Snum = scanner.nextLine();
        System.out.print("Student password ： "); Spas = scanner.nextLine();
      }
    }
  }

  public boolean StudentInquiry(String number, String password){
    MysqlFunction mysql = new MysqlFunction();
    Connection conn;

    try{
      // 打開鏈結
      conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

      // 開始查詢
      String sql = "SELECT id, password FROM students WHERE id=? AND password=?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,number);
      ps.setString(2,password);
      ResultSet rs = ps.executeQuery();

      // 展開結果
      if(rs.next()){
        rs.close();
        conn.close();
        return true;
      }
      // 完成后关闭
      rs.close();
      conn.close();
      return false;

    } catch(Exception e){
      System.out.println(e);
      return false;
    }
  }



}
