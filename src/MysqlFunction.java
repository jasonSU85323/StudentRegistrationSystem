package src;
import java.util.*;
import java.sql.*;

public class MysqlFunction {

    // private static final String DB_URL = "jdbc:mysql://haoyi.synology.me:3307/jasonsu?serverTimezone=UTC";
    // private static final String USER = "root";
    // private static final String PASS = "0000";

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/srs?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    public MysqlFunction(){

    }

    public String get_DB_URL(){
      return DB_URL;
    }
    public String get_USER(){
      return USER;
    }
    public String get_PASS(){
      return PASS;
    }

}
