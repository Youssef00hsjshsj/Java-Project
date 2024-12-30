import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    
    public String addCourse(String name, String description, int teacherId) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "INSERT INTO Courses (name, description, teacher_id) VALUES (?, ?, ?)";
        PreparedStatement st = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        st.setString(1, name);
        st.setString(2, description);
        st.setInt(3, teacherId);
        st.executeUpdate();


        ResultSet resultSet = st.getGeneratedKeys();
        if (resultSet.next()) {
            int courseId = resultSet.getInt(1);  // Get the generated course ID
            return "Course added successfully! Course ID: " + courseId;
        } else {
            return "Error: Unable to retrieve course ID.";
        }
    }

    // Update an existing course
    public String updateCourse(int courseId, String name, String description) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "UPDATE Courses SET name = ?, description = ? WHERE id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, name);
        st.setString(2, description);
        st.setInt(3, courseId);
        int rowsUpdated = st.executeUpdate();

        if (rowsUpdated > 0) {
            return "Course updated successfully!";
        } else {
            return "Error: Course not found or update failed.";
        }
    }

    // Retrieve a course by ID
    public String getCourse(int courseId) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "SELECT * FROM Courses WHERE id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, courseId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return "Course ID: " + rs.getInt("id") + "\n" +
                   "Name: " + rs.getString("name") + "\n" +
                   "Description: " + rs.getString("description");
        } else {
            return "Course not found!";
        }
    }

    // Enroll a student in a course
    public String enrollStudent(int courseId, int studentId) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "INSERT INTO student_enrolled (course_id, student_id) VALUES (?, ?)";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, courseId);
        st.setInt(2, studentId);
        int rowsAffected = st.executeUpdate();

        if (rowsAffected > 0) {
            return "Student enrolled successfully!";
        } else {
            return "Error: Could not enroll student.";
        }
    }

    // Retrieve all courses
    public List<String> getCourses() throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "SELECT * FROM Courses";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<String> courses = new ArrayList<>();
        while (rs.next()) {
            courses.add("Course ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
        }
        return courses;
    }

    // Retrieve exam details
    public String getExamDetails(int examId) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "SELECT * FROM Exams WHERE id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, examId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return "Exam ID: " + rs.getInt("id") + "\n" +
                   "Course ID: " + rs.getInt("course_id") + "\n" +
                   "Name: " + rs.getString("exam_name") + "\n" +
                   "Date: " + rs.getString("exam_date");
        } else {
            return "Exam not found!";
        }
    }

    // Update a student's grade
    public String updateGrade(int examId, int studentId, int grade) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "UPDATE Degrees SET Degree = ? WHERE ExamID = ? AND StudentID = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, grade);
        st.setInt(2, examId);
        st.setInt(3, studentId);
        int rowsUpdated = st.executeUpdate();

        if (rowsUpdated > 0) {
            return "Grade updated successfully!";
        } else {
            return "Error: Could not update grade.";
        }
    }

    // Send notification to a user
    public String sendNotification(int userId, String title, String message) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "INSERT INTO communications (user_id, title, message, status) VALUES (?, ?, ?, 'unread')";
        PreparedStatement st = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        st.setInt(1, userId);
        st.setString(2, title);
        st.setString(3, message);
        st.executeUpdate();

        ResultSet resultSet = st.getGeneratedKeys();
        if (resultSet.next()) {
            int notificationId = resultSet.getInt(1);
            return "Notification sent successfully! Notification ID: " + notificationId;
        } else {
            return "Error: Unable to send notification.";
        }
    }

    // View attendance for a course and student
    public String viewAttendance(int courseId, int studentId) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "SELECT * FROM Attendance WHERE course_id = ? AND student_id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, courseId);
        st.setInt(2, studentId);
        ResultSet rs = st.executeQuery();
        StringBuilder attendanceRecords = new StringBuilder();

        while (rs.next()) {
            attendanceRecords.append("Date: ").append(rs.getString("date")).append(", Status: ").append(rs.getString("status")).append("\n");
        }

        if (attendanceRecords.length() == 0) {
            return "No attendance records found.";
        } else {
            return attendanceRecords.toString();
        }
    }

    // Delete a student
    public String deleteStudent(int studentId) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String query = "DELETE FROM Users WHERE id = ? ";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, studentId);
        int rowsDeleted = st.executeUpdate();

        if (rowsDeleted > 0) {
            return "Student deleted successfully!";
        } else {
            return "Error: Student not found or deletion failed.";
        }
    }
}
