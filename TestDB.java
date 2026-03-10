import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "root";

        try {
            System.out.println("Attempting to connect to MySQL...");
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("SUCCESS: Connected to MySQL database!");
            connection.close();
        } catch (SQLException e) {
            System.err.println("FAILED TO CONNECT: ");
            e.printStackTrace();
        }
    }
}
