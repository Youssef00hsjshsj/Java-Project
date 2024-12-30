import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Communication {
    private int id;
    private int userId;
    private String title;
    private String message;
    private String date;

    public Communication(int userId, String title, String message) {
        this.userId = userId;
        this.title = title;
        this.message = message;
    }

    public void sendNotification() throws ClassNotFoundException {
        String query = "INSERT INTO Communications (userId, title, message) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.userId);
            stmt.setString(2, this.title);
            stmt.setString(3, this.message);
            stmt.executeUpdate();
            System.out.println("Communication added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all unread notifications for a specific user
    public static Map<String, String> getNotifications() throws ClassNotFoundException {
        Map<String, String> notifications = new HashMap<>();
        String query = "SELECT title, message FROM Communications";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Add each unread communication to the map with title as key and message as value
                notifications.put(rs.getString("title"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    // Get a specific message by its ID
    public static Map<String, String> getMessage(int messageId) throws ClassNotFoundException {
        Map<String, String> messageDetails = new HashMap<>();
        String query = "SELECT title, message FROM Communications WHERE id = ?";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Add message details to the map
                messageDetails.put("Title", rs.getString("title"));
                messageDetails.put("Message", rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageDetails;
    }
    public static void sendMessage(int senderId, int receiverId, String title, String message) throws ClassNotFoundException {
        String query = "INSERT INTO Communications (userId, title, message, status, date) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set parameters for the message
            stmt.setInt(1, receiverId); // receiverId is the userId in the Communications table
            stmt.setString(2, title); // Message title
            stmt.setString(3, message); // Message content
            stmt.setString(4, "Unread"); // Default status for new messages is "Unread"

            // Execute the insert statement
            stmt.executeUpdate();
            System.out.println("Message sent successfully from user " + senderId + " to user " + receiverId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
