import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Notification {
    static int id;
    int user_id;
    String title;
    String message;
    String date;

    static void getNotifications() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseConnection.getDatabaseConnection();
        String sql = "SELECT * FROM communications WHERE user_id = ? AND status = 'unread'";  // Assume the user's ID is passed in
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);  // Replace with teacher's ID
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String title = rs.getString("title");
            String message = rs.getString("message");
            System.out.println("Title: " + title + "\nMessage: " + message);
        }

        connection.close();
    }

}
