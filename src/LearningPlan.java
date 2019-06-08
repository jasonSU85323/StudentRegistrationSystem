package src;
import java.util.*;
import java.sql.*;

public class LearningPlan {
    private String sid;
    private HashSet<String[]> courselist = new HashSet<String[]>();

    public LearningPlan(String sid){
        this.sid = sid;
        updataCourselist(this.sid);
    }

    public void updataCourselist(String sid){
        courselist.clear();
        MysqlFunction mysql = new MysqlFunction();
        Connection conn;


        try{
            // 打開鏈結
            conn = DriverManager.getConnection( mysql.get_DB_URL(), mysql.get_USER(), mysql.get_PASS());

            // 開始查詢
            String sql;
            sql = "SELECT * FROM learningplan WHERE sid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,sid);
            ResultSet rs = ps.executeQuery();

            // 展開結果
            int count = 0;
            while(rs.next()){
                String[] temp = new String[7];
                temp[0] = rs.getString("mandatory");
                temp[1] = rs.getString("schoolyear");
                temp[2] = rs.getString("semester");
                temp[3] = rs.getString("subjectname");
                temp[4] = rs.getString("academicscore");
                temp[5] = rs.getString("teacher");
                temp[6] = rs.getString("grade");
                courselist.add(temp);
            }
            // 完成后关闭
            rs.close();
            conn.close();

        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void CourseListshow(){
        updataCourselist(this.sid);
        Formatter formatter = new Formatter(System.out);
        Iterator clist = courselist.iterator();
        while (clist.hasNext()) {
            String[] list = (String[])clist.next();
            formatter.format("%s %s %s %-10s \t %5s %5s \n",list[0] ,list[1] ,list[2] ,list[3] ,list[4] ,list[5]);
        }
        System.out.println("您修了 " + courselist.size() + " 科");
    }
    public void ElectiveCourseListshow(){
        updataCourselist(this.sid);
        AcademicAffairsOffice AAO = new AcademicAffairsOffice();
        Formatter formatter = new Formatter(System.out);
        Iterator clist = courselist.iterator();
        int count = 0;
        while (clist.hasNext()) {
            String[] list = (String[])clist.next();
            if (list[1].equals(AAO.get_next_schoolyear()) && list[2].equals(AAO.get_next_semester())) {
                count++;
                formatter.format("%s %s %s %-10s\t %5s %5s \n",list[0] ,list[1] ,list[2] ,list[3] ,list[4] ,list[5]);
            }
        }
        System.out.println("您選了 " + String.valueOf(count) + " 科");
    }
    public void AllGradeListshow(){
        updataCourselist(this.sid);
        AcademicAffairsOffice AAO = new AcademicAffairsOffice();
        Formatter formatter = new Formatter(System.out);
        Iterator clist = courselist.iterator();
        while (clist.hasNext()) {
            String[] list = (String[])clist.next();
            if (!list[1].equals(AAO.get_next_schoolyear()) || !list[2].equals(AAO.get_next_semester())) {
                formatter.format("%s %s %s %-10s\t %5s %5s  %5s\n",list[0] ,list[1] ,list[2] ,list[3] ,list[4] ,list[5] ,list[6]);
            }
        }
    }

    public boolean PassingTheResults(String course, int grade){
        updataCourselist(this.sid);
        Iterator clist = courselist.iterator();
        while (clist.hasNext()) {
            String[] list = (String[])clist.next();
            if (list[3].equals(course) && Integer.valueOf(list[6])>=grade) {
                return true;
            }
        }
        return false;
    }
    public boolean CourseStatus(String course){
      updataCourselist(this.sid);
      Iterator clist = courselist.iterator();
      while (clist.hasNext()) {
          String[] list = (String[])clist.next();
          if (list[3].equals(course)) {
              return true;
          }
      }
      return false;
    }

}
