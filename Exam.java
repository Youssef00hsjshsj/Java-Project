import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

 public  class Exam {

    private int id;
    private int courseId;
    private String examName;
    private String date;

    // Constructor
    public Exam(int courseId, String examName, String date) {
        this.courseId = courseId;
        this.examName = examName;
        this.date = date;
    }



    // Add Exam
    public static void addExam(int courseId, String examName, String date) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            con = DatabaseConnection.getDatabaseConnection();
            String query = "INSERT INTO Exams (course_id, exam_name, exam_date) VALUES (?, ?, ?)";

            st = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setInt(1, courseId);
            st.setString(2, examName);
            st.setString(3, date);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int examID = rs.getInt(1);
                    System.out.println("\nExam Added Successfully! Exam ID: " + examID);
                }
            } else {
                System.out.println("\nFailed to Add Exam.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding exam: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null) con.close();
        }
    }
    
    // Update Exam
    public static void updateExam(int examId, String newExamName, String newDate) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement st = null;

        try {
            con = DatabaseConnection.getDatabaseConnection();
            String query = "UPDATE Exams SET exam_name = ?, exam_date = ? WHERE id = ?";
            st = con.prepareStatement(query);

            st.setString(1, newExamName);
            st.setString(2, newDate);
            st.setInt(3, examId);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\nExam Updated Successfully!");
            } else {
                System.out.println("\nNo Exam Found with the Given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating exam: " + e.getMessage());
        } finally {
            if (st != null) st.close();
            if (con != null) con.close();
        }
    }

    // Get Exam Details
    public static void getExamDetails(int examId) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            con = DatabaseConnection.getDatabaseConnection();
            String query = "SELECT * FROM Exams WHERE id = ?";
            st = con.prepareStatement(query);

            st.setInt(1, examId);
            rs = st.executeQuery();

            if (rs.next()) {
                System.out.println("\nExam Details:");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Course ID: " + rs.getInt("course_id"));
                System.out.println("Exam Name: " + rs.getString("exam_name"));
                System.out.println("Exam Date: " + rs.getString("exam_date"));
            } else {
                System.out.println("\nNo Exam Found with the Given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching exam details: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null) con.close();
        }
    }
     public static void submitDegree(int studentId, int examId, int courseId, String grade) throws ClassNotFoundException, SQLException {
         Connection con = null;
         PreparedStatement st = null;

         try {
             // Establishing the database connection
             con = DatabaseConnection.getDatabaseConnection();

             // SQL query to insert degree (grade) into the Degrees table
             String query = "INSERT INTO Degrees (student_id, exam_id, course_id, grade, submission_date) VALUES (?, ?, ?, ?, ?)";
             st = con.prepareStatement(query);

             // Setting the values for the SQL query
             st.setInt(1, studentId); // Set Student ID
             st.setInt(2, examId);    // Set Exam ID
             st.setInt(3, courseId);  // Set Course ID
             st.setString(4, grade);  // Set Grade (e.g., 'A', 'B', or numeric value)
             st.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now())); // Set Submission Date to today's date

             // Execute the SQL insert
             int rowsAffected = st.executeUpdate();

             if (rowsAffected > 0) {
                 System.out.println("Degree (Grade) Submitted Successfully for Student ID: " + studentId);
             } else {
                 System.out.println("Failed to submit degree. Please try again.");
             }
         } catch (SQLException e) {
             System.out.println("Error submitting degree: " + e.getMessage());
         } finally {
             if (st != null) st.close();
             if (con != null) con.close();
         }
     }
}
