package db;

import java.sql.*;

public class DBhelper {

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Aynı bağlantı stringini kullanıyoruz:
            String url = "jdbc:sqlserver://ABDULLAH\\SQLEXPRESS:1433;databaseName=kelimeezberDB;integratedSecurity=true;encrypt=false;trustServerCertificate=true";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
        }
        return conn;
    }

    public static String rastgeleBesHarfliKelimeGetir() {
        String kelime = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) {
                System.out.println("Veritabanı bağlantısı kurulamadı.");
                return null;
            }

            String sql = "SELECT TOP 1 kelime FROM Kelimeler WHERE LEN(kelime) = 5 ORDER BY NEWID()";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                kelime = rs.getString("kelime");
            }

        } catch (SQLException e) {
            System.out.println("Kelime çekilirken hata oluştu: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }

        return kelime;
    }
}
