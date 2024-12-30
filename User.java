import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class User {
    
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private int roleId;
    private boolean currentState;
    
    // Constructor for User object
    public User( String name, String email, String phone, String password, int roleId, boolean currentState) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.roleId = roleId;
        this.currentState = currentState;
    }

    // Update Profile
    public void updateProfile(String newName, String newPhone, String newEmail) throws ClassNotFoundException, SQLException {
        String query = "UPDATE users SET name = ?, phone = ?, email = ? WHERE id = ?";

        try (Connection con = DatabaseConnection.getDatabaseConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, newName);
            st.setString(2, newPhone);
            st.setString(3, newEmail);
            st.setInt(4, this.id);

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                this.name = newName;
                this.phone = newPhone;
                this.email = newEmail;
                System.out.println("Profile updated successfully.");
            } else {
                System.out.println("Failed to update profile.");
            }
        }
    }

    // Authenticate User
    public static User authenticate(String email, String password) throws ClassNotFoundException, SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection con = DatabaseConnection.getDatabaseConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, email);
            st.setString(2, password); // Replace with hashed password if implemented

            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    
                     int id  = result.getInt("id");
                     String name  = result.getString("name");
                     String Email  = result.getString("email");
                     String phone  = result.getString("phone");
                     int role_id  = result.getInt("role_id");
                    boolean state = !result.getBoolean("current_state");

                    return new User(name, email, phone, password, role_id, state);
                }
            }
        }
        return null;  // Return null if authentication fails
    }

    // Register User
    public void register() throws ClassNotFoundException, SQLException {
        String query = "INSERT INTO users (name, email, phone, password, role_id, current_state) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getDatabaseConnection();
             PreparedStatement st = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            st.setString(1, this.name);
            st.setString(2, this.email);
            st.setString(3, this.phone);
            st.setString(4, this.password); // Replace with hashed password if implemented
            st.setInt(5, this.roleId);
            st.setBoolean(6, this.currentState);

            st.executeUpdate();

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1); // Retrieve generated ID
                }
            }
            System.out.println("User registered successfully with ID: " + this.id);
        }
    }

    // Logout User
    public  void logout() throws ClassNotFoundException, SQLException {
        String query = "UPDATE users SET current_state = ? WHERE id = ?";

        try (Connection con = DatabaseConnection.getDatabaseConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setBoolean(1, false);
            st.setInt(2, this.id);

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                this.currentState = false;
                System.out.println("User logged out successfully.");
            } else {
                System.out.println("Logout failed.");
            }
        }
    }

    // Forgot Password
    public static void forgotPassword(String email , String newPassword) throws ClassNotFoundException, SQLException {
        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection con = DatabaseConnection.getDatabaseConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, email);

            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    // Send a password reset email or generate a temporary password
                    
                    String updateQuery = "UPDATE users SET password = ? WHERE email = ?";

                    try (PreparedStatement updateSt = con.prepareStatement(updateQuery)) {
                        updateSt.setString(1, newPassword);
                        updateSt.setString(2, email);
                        updateSt.executeUpdate();

                        System.out.println("The Password has been changed successfully.");
                    }
                } else {
                    System.out.println("Email not found in the system.");
                }
            }
        }
    }

    // Retrieve User Details
    public Map<String, Object> getUserDetails(int userId) throws ClassNotFoundException, SQLException {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection con = DatabaseConnection.getDatabaseConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, userId);

            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    Map<String, Object> userDetails = new HashMap<>();
                    userDetails.put("id", result.getInt("id"));
                    userDetails.put("name", result.getString("name"));
                    userDetails.put("email", result.getString("email"));
                    userDetails.put("phone", result.getString("phone"));
                    userDetails.put("roleId", result.getInt("role_id"));
                    userDetails.put("currentState", result.getBoolean("current_state"));

                    return userDetails;
                }
            }
        }
        return null;  // Return null if no user found
    }

    // Getters and Setters for User
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public boolean isCurrentState() { return currentState; }
    public void setCurrentState(boolean currentState) { this.currentState = currentState; }
}
