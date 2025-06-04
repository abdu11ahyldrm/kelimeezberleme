package ekran;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class KullaniciKayit extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public KullaniciKayit() {
        setTitle("Kayıt Ol");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(220, 240, 255)); // Açık mavi arka plan
        setLayout(null);

        JLabel lblTitle = new JLabel("Yeni Kullanıcı Kaydı");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 60, 90));
        lblTitle.setBounds(100, 20, 250, 30);
        add(lblTitle);

        JLabel lblUsername = new JLabel("Kullanıcı Adı:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setBounds(50, 80, 100, 25);
        add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(160, 80, 180, 25);
        txtUsername.setBackground(new Color(245, 250, 255));
        add(txtUsername);

        JLabel lblPassword = new JLabel("Şifre:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(50, 120, 100, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 120, 180, 25);
        txtPassword.setBackground(new Color(245, 250, 255));
        add(txtPassword);

        JButton btnRegister = new JButton("Kayıt Ol");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setBackground(new Color(70, 130, 180)); // Steel blue
        btnRegister.setForeground(Color.white);
        btnRegister.setBounds(140, 180, 100, 35);
        add(btnRegister);

        btnRegister.addActionListener(e -> kullaniciKaydet());

        setVisible(true);
    }

    private void kullaniciKaydet() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen kullanıcı adı ve şifre girin.");
            return;
        }

        try {
            String url = "jdbc:sqlserver://ABDULLAH\\SQLEXPRESS:1433;"
                       + "databaseName=kelimeezberDB;"
                       + "encrypt=true;"
                       + "trustServerCertificate=true;"
                       + "integratedSecurity=true ";
            Connection conn = DriverManager.getConnection(url, "", "");

            // Aynı kullanıcı var mı kontrolü
            PreparedStatement check = conn.prepareStatement("SELECT * FROM Users WHERE UserName = ?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Bu kullanıcı adı zaten kayıtlı.");
                return;
            }

            // Yeni kullanıcı ekle
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Users (UserName, Password) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Kayıt başarılı!");
            conn.close();

            txtUsername.setText("");
            txtPassword.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new KullaniciKayit();
    }
}
