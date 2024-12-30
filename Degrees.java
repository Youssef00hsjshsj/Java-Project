import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Degrees {

    private int examID;
    private int studentId;
    private int degree;
    int count = 0;

    public Degrees(int examID, int studentId, int degree) {
        this.examID = examID;
        this.studentId = studentId;
        this.degree = degree;
    }

    // Method to submit a degree
    public void submitDegree() throws ClassNotFoundException, SQLException {
        Connection con = DatabaseConnection.getDatabaseConnection();

        String query = "INSERT INTO Degrees (exam_id, student_id, degree) VALUES (?, ?, ?)";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, this.examID);
        st.setInt(2, this.studentId);
        st.setInt(3, this.degree);

        int rowsAffected = st.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Degree submitted successfully!");
        } else {
            System.out.println("Failed to submit degree.");
        }

        st.close();
        con.close();
    }

    // Method to get a report card for a specific student
    public static void getReportCard(int studentId) throws ClassNotFoundException, SQLException {
        Connection con = DatabaseConnection.getDatabaseConnection();

        String query = "SELECT e.exam_name, d.degree FROM Degrees d " +
                       "JOIN Exams e ON d.exam_id = e.id " +
                       "WHERE d.student_id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, studentId);

        ResultSet result = st.executeQuery();

        System.out.println("\nReport Card for Student ID: " + studentId);
        System.out.println("Exam Name\t\tDegree");
        System.out.println("--------------------------------");

        boolean hasRecords = false;
        while (result.next()) {
            hasRecords = true;
            String examName = result.getString("exam_name");
            int degree = result.getInt("degree");
            System.out.println(examName + "\t\t" + degree);
        }

        if (!hasRecords) {
            System.out.println("No records found.");
        }

        result.close();
        st.close();
        con.close();
    }
}
