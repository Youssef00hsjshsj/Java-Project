import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Attendance {
    private int courseId;
    private int studentId;
    private String date;
    private String status;

    public Attendance(int courseId, int studentId, String date, String status) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    // Method to mark attendance for a student in a particular course
    public void markAttendance() throws ClassNotFoundException {
        String query = "INSERT INTO Attendance (courseId, studentId, date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.courseId);
            stmt.setInt(2, this.studentId);
            stmt.setString(3, this.date);
            stmt.setString(4, this.status);
            
            stmt.executeUpdate();
            System.out.println("Attendance marked successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view attendance for a student in a particular course
    public static void viewAttendance(int courseId, int studentId) throws ClassNotFoundException {
        String query = "SELECT * FROM Attendance WHERE courseId = ? AND studentId = ?";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, courseId);
            stmt.setInt(2, studentId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date");
                String status = rs.getString("status");
                System.out.println("Date: " + date + ", Status: " + status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Example to use this class in a method, like the Admin menu
    public static void markStudentAttendance() throws SQLException, ClassNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.println("\nMark Attendance:");
        System.out.print("Enter Course ID: ");
        int courseId = input.nextInt();
        System.out.print("Enter Student ID: ");
        int studentId = input.nextInt();
        input.nextLine();  // Clear the buffer
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = input.nextLine();
        System.out.print("Enter Status (Present/Absent): ");
        String status = input.nextLine();

        // Create an instance and mark attendance
        Attendance attendance = new Attendance(courseId, studentId, date, status);
        attendance.markAttendance();

        System.out.println("Attendance has been marked.");
    }

    // Example to view attendance for a student
    public static void viewStudentAttendance() throws SQLException, ClassNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.println("\nView Attendance:");
        System.out.print("Enter Course ID: ");
        int courseId = input.nextInt();
        System.out.print("Enter Student ID: ");
        int studentId = input.nextInt();

        // View attendance for the student in the given course
        Attendance.viewAttendance(courseId, studentId);
    }
}
