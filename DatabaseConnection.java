import java.sql.*;

public class DatabaseConnection {

    private static Connection con = null;
    private static String USERNAME = "root";
    private static String PASSWORD = "root";
    private static String URL = "jdbc:mysql://localhost:3306/school?useSSL=false&serverTimezone=UTC";

    public static Connection getDatabaseConnection() throws ClassNotFoundException, SQLException {
        
        return con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
}

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getDatabaseConnection();
        String Query = "Select * from Courses";
        Statement st = con.createStatement();
        ResultSet resultSet = st.executeQuery(Query);

        while (resultSet.next()){
            System.out.println(resultSet.getString("Course_name"));
        }
    }

}
