import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.xdevapi.Statement;

public class Teacher extends User {

    public Teacher( int id ,String name, String email, String phone, String password, int roleId, boolean currentState) {

        super( name, email, phone, password, roleId, currentState);

    }

    public static int addExam(int courseId, String examName, String examDate) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseConnection.getDatabaseConnection(); // Replace with actual DB connection method
        String sql = "INSERT INTO exams (course_id, exam_name, exam_date) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, courseId);
        pstmt.setString(2, examName);
        pstmt.setString(3, examDate);
        pstmt.executeUpdate();
    
        // Retrieve the generated key (ID)
        ResultSet rs = pstmt.getGeneratedKeys();
        int generatedId = -1; // Default value if ID is not retrieved
        if (rs.next()) {
            generatedId = rs.getInt(1); // Assuming the ID is the first generated key
        }
    
        connection.close();
        return generatedId;
    }
    
    static String updateExam(int examId, String newExamName, String newExamDate) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseConnection.getDatabaseConnection();
        String sql = "UPDATE exams SET exam_name = ?, exam_date = ? WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newExamName);
        pstmt.setString(2, newExamDate);
        pstmt.setInt(3, examId);
        int rowsUpdated = pstmt.executeUpdate();
    
        connection.close();
        if (rowsUpdated > 0) {
            return "Exam updated successfully!";
        } else {
            return "Failed to update the exam. Exam ID may not exist.";
        }
    }
    
    static String getExamDetails(int courseId) throws SQLException, ClassNotFoundException {
    Connection connection = DatabaseConnection.getDatabaseConnection();
    String sql = "SELECT * FROM exams WHERE course_id = ?";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setInt(1, courseId);
    ResultSet rs = pstmt.executeQuery();

    StringBuilder examDetails = new StringBuilder("Exams for Course ID: " + courseId + "\n\n");

    int examCount = 0;

    while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("exam_name");
        String date = rs.getString("exam_date");
        examDetails.append("Exam ID: ").append(id).append("\n")
                   .append("Exam Name: ").append(name).append("\n")
                   .append("Exam Date: ").append(date).append("\n\n");
        examCount++;
    }

    connection.close();

    if (examCount == 0) {
        return "No exams found for this course ID.";
    } else {
        return examDetails.toString();
    }
}

static String submitDegree(int examId, int studentId, double degree) throws SQLException, ClassNotFoundException {
    Connection connection = DatabaseConnection.getDatabaseConnection();
    String sql = "INSERT INTO degrees (ExamID, StudentID, Degree) VALUES (?, ?, ?)";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setInt(1, examId);
    pstmt.setInt(2, studentId);
    pstmt.setDouble(3, degree);
    int rowsInserted = pstmt.executeUpdate();

    connection.close();
    if (rowsInserted > 0) {
        return "Degree submitted successfully!";
    } else {
        return "Failed to submit the degree.";
    }
}

public static List<Map<String, String>> getNotifications() throws SQLException, ClassNotFoundException {
    Connection connection = DatabaseConnection.getDatabaseConnection();
    String sql = "SELECT title, message FROM communications ";
    PreparedStatement pstmt = connection.prepareStatement(sql);
     // Filter notifications for a specific user
    ResultSet rs = pstmt.executeQuery();

    List<Map<String, String>> notifications = new ArrayList<>();

    while (rs.next()) {
        Map<String, String> notification = new HashMap<>();
        notification.put("title", rs.getString("title"));
        notification.put("message", rs.getString("message"));
        notifications.add(notification);
    }

    connection.close();
    return notifications;
}

static String markAttendance(int courseId, int studentId, String date, String status) throws SQLException, ClassNotFoundException {
    Connection connection = DatabaseConnection.getDatabaseConnection();
    String sql = "INSERT INTO attendance (course_id, student_id, date, status) VALUES (?, ?, ?, ?)";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setInt(1, courseId);
    pstmt.setInt(2, studentId);
    pstmt.setString(3, date);
    pstmt.setString(4, status);
    int rowsInserted = pstmt.executeUpdate();

    connection.close();
    if (rowsInserted > 0) {
        return "Attendance marked successfully!";
    } else {
        return "Failed to mark attendance.";
    }
}
static String getEnrolledStudents(int courseId) throws SQLException, ClassNotFoundException {
    Connection connection = DatabaseConnection.getDatabaseConnection();
    String sql = "SELECT u.name, u.email FROM users u JOIN student_enrolled se ON u.id = se.student_id WHERE se.course_id = ?";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setInt(1, courseId);
    ResultSet rs = pstmt.executeQuery();

    StringBuilder students = new StringBuilder("Enrolled Students:\n\n");
    int studentCount = 0;
    while (rs.next()) {
        String name = rs.getString("name");
        String email = rs.getString("email");
        students.append("Name: ").append(name).append("\n")
                .append("Email: ").append(email).append("\n\n");
        studentCount++;
    }

    connection.close();

    if (studentCount == 0) {
        return "No students enrolled in this course.";
    } else {
        return students.toString();
    }
}

}
