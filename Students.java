import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Students {

    public static String getReportCard(int studentId) throws SQLException, ClassNotFoundException {
    StringBuilder reportCard = new StringBuilder();
    Connection con = DatabaseConnection.getDatabaseConnection();
    String query = "SELECT e.exam_name, d.degree " +
    "FROM Degrees d " +
    "JOIN Exams e ON d.examID = e.id " +
    "WHERE d.StudentID = ?";
                
    PreparedStatement st = con.prepareStatement(query);
    st.setInt(1, studentId);
    ResultSet rs = st.executeQuery();

    reportCard.append("Exam Name : Degree\n");
    reportCard.append("--------------------------------\n");
    while (rs.next()) {
        reportCard.append(rs.getString("exam_name")).append(" : ").append(rs.getInt("degree")).append("\n");
    }

    rs.close();
    st.close();
    con.close();

    return reportCard.toString();
}

public static Map<String, String> getNotifications() throws SQLException, ClassNotFoundException {
    Map<String, String> notifications = new HashMap<>();
    Connection con = DatabaseConnection.getDatabaseConnection();
    String query = "SELECT title, message FROM communications";
    PreparedStatement st = con.prepareStatement(query);
    ResultSet rs = st.executeQuery();

    while (rs.next()) {
        notifications.put(rs.getString("title"), rs.getString("message"));
    }

    rs.close();
    st.close();
    con.close();

    return notifications;
}


public static String getCourses(int studentId) throws SQLException, ClassNotFoundException {
    StringBuilder courseList = new StringBuilder();
    Connection con = DatabaseConnection.getDatabaseConnection();
    
    String query = "SELECT c.name\r\n" + 
                "FROM courses c\r\n" + 
                "JOIN student_enrolled sc ON c.id = sc.course_id\r\n" + 
                "WHERE sc.student_id = ?;";
    PreparedStatement st = con.prepareStatement(query);
    st.setInt(1, studentId);
    ResultSet rs = st.executeQuery();

    courseList.append("Course Name\n");
    courseList.append("--------------------------------\n");
    while (rs.next()) {
        courseList.append(rs.getString("name")).append("\n");
    }

    rs.close();
    st.close();
    con.close();

    return courseList.toString();
}

}
