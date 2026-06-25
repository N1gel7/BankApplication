import java.sql.*;
import java.util.UUID;

public class TestSave {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/banking_app_main";
        String user = "postgres";
        String password = "postgres";
        
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement stmt = conn.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE email='nigel@gmail.com'");
        if(rs.next()) {
            int uid = rs.getInt("id");
            System.out.println("User nigel@gmail.com found with ID: " + uid);
            
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM kyc_document WHERE user_id=" + uid);
            if(rs2.next()) {
                System.out.println("KYC Doc found! Status: " + rs2.getString("status"));
            } else {
                System.out.println("NO KYC DOC FOUND FOR USER!");
            }
        } else {
            System.out.println("User not found!");
        }
        conn.close();
    }
}
