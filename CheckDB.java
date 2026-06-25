import java.sql.*;

public class CheckDB {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/banking_app_main", "postgres", "postgres");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM kyc_document");
        int count = 0;
        while(rs.next()) {
            System.out.println("DocId: " + rs.getInt("doc_id") + " Status: " + rs.getString("status"));
            count++;
        }
        System.out.println("Total Docs: " + count);
        
        rs = stmt.executeQuery("SELECT * FROM users");
        while(rs.next()) {
            System.out.println("User: " + rs.getString("email") + " KYC: " + rs.getString("kyc_status"));
        }
        conn.close();
    }
}
