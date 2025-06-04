package ekran;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class KullaniciGiris extends JFrame {
    private JTextField txtKullanici;
    private JPasswordField txtSifre;
    private JButton btnGiris;

    public KullaniciGiris() {
        setTitle("Kullanıcı Girişi");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblKullanici = new JLabel("Kullanıcı Adı:");
        JLabel lblSifre = new JLabel("Şifre:");
        txtKullanici = new JTextField();
        txtSifre = new JPasswordField();
        btnGiris = new JButton("Giriş Yap");

        add(lblKullanici);
        add(txtKullanici);
        add(lblSifre);
        add(txtSifre);
        add(new JLabel());
        add(btnGiris);

        btnGiris.addActionListener(e -> girisYap());
    }

    private void girisYap() {
        String kullanici = txtKullanici.getText().trim();
        String sifre = new String(txtSifre.getPassword());

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://ABDULLAH\\SQLEXPRESS:1433;databaseName=kelimeezberDB;integratedSecurity=true;encrypt=false;trustServerCertificate=true;");
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE Username = ? AND Password = ?");
            ps.setString(1, kullanici);
            ps.setString(2, sifre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dispose(); // giriş ekranını kapat
                new AnaMenu(kullanici).setVisible(true); // ana menüyü aç
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı kullanıcı adı veya şifre.");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KullaniciGiris().setVisible(true));
    }
}
