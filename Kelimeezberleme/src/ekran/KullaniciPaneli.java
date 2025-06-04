package ekran;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class KullaniciPaneli extends JFrame {

    private JTextField txtKullaniciAdi;
    private JPasswordField txtSifre;
    private JButton btnGiris;
    private JButton btnKayit;

    public KullaniciPaneli() {
        setTitle("Giriş ve Kayıt Paneli");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Arka plan paneli
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        add(mainPanel);

        // Başlık
        JLabel lblBaslik = new JLabel("Kullanıcı Girişi");
        lblBaslik.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBaslik.setHorizontalAlignment(SwingConstants.CENTER);
        lblBaslik.setForeground(new Color(33, 37, 41));
        mainPanel.add(lblBaslik, BorderLayout.NORTH);

        // Form alanları
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        formPanel.setBackground(new Color(240, 240, 240));

        txtKullaniciAdi = new JTextField();
        txtKullaniciAdi.setBorder(BorderFactory.createTitledBorder("Kullanıcı Adı"));
        txtKullaniciAdi.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtSifre = new JPasswordField();
        txtSifre.setBorder(BorderFactory.createTitledBorder("Şifre"));
        txtSifre.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btnGiris = new JButton("Giriş Yap");
        btnGiris.setFocusPainted(false);
        btnGiris.setBackground(new Color(70, 130, 180));
        btnGiris.setForeground(Color.WHITE);
        btnGiris.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGiris.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(txtKullaniciAdi);
        formPanel.add(txtSifre);
        formPanel.add(btnGiris);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Alt panel: kayıt butonu
        JPanel altPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        altPanel.setBackground(new Color(240, 240, 240));

        JLabel lblKayit = new JLabel("Üye değil misiniz?");
        lblKayit.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btnKayit = new JButton("Kayıt Ol");
        btnKayit.setFocusPainted(false);
        btnKayit.setBackground(new Color(100, 149, 237));
        btnKayit.setForeground(Color.WHITE);
        btnKayit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnKayit.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        altPanel.add(lblKayit);
        altPanel.add(btnKayit);

        mainPanel.add(altPanel, BorderLayout.SOUTH);

        // Aksiyonlar
        btnGiris.addActionListener(e -> girisYap());
        btnKayit.addActionListener(e -> kayitOl());
    }

    private void girisYap() {
        String kullaniciAdi = txtKullaniciAdi.getText().trim();
        String sifre = new String(txtSifre.getPassword());

        if (kullaniciAdi.isEmpty() || sifre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.");
            return;
        }

        try (Connection conn = db.DBhelper.getConnection()) {
            String sql = "SELECT * FROM Users WHERE UserName = ? AND Password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kullaniciAdi);
            pstmt.setString(2, sifre);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Giriş başarılı!");
                dispose();
                new AnaMenu(kullaniciAdi).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı kullanıcı adı veya şifre.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage());
        }
    }

    private void kayitOl() {
        String kullaniciAdi = txtKullaniciAdi.getText().trim();
        String sifre = new String(txtSifre.getPassword());

        if (kullaniciAdi.isEmpty() || sifre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.");
            return;
        }

        try (Connection conn = db.DBhelper.getConnection()) {
            String kontrolSQL = "SELECT * FROM Users WHERE UserName = ?";
            PreparedStatement kontrolStmt = conn.prepareStatement(kontrolSQL);
            kontrolStmt.setString(1, kullaniciAdi);
            ResultSet rs = kontrolStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Bu kullanıcı adı zaten alınmış.");
                return;
            }

            String kayitSQL = "INSERT INTO Users (UserName, Password) VALUES (?, ?)";
            PreparedStatement kayitStmt = conn.prepareStatement(kayitSQL);
            kayitStmt.setString(1, kullaniciAdi);
            kayitStmt.setString(2, sifre);
            kayitStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Kayıt başarılı, şimdi giriş yapabilirsiniz.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KullaniciPaneli().setVisible(true));
    }
}
