package ekran;

import db.DBhelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class KelimeEkle extends JFrame {

    private JTextField txtKelime, txtAnlam;
    private JButton btnEkle, btnAnaMenu;

    public KelimeEkle() {
        setTitle("Kelime Ekle");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 255)); // açık pastel mor ton

        JLabel lblTitle = new JLabel("Yeni Kelime Ekle");
        lblTitle.setBounds(150, 20, 250, 30);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        add(lblTitle);

        JLabel lblKelime = new JLabel("Kelime:");
        lblKelime.setBounds(50, 80, 100, 25);
        lblKelime.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(lblKelime);

        txtKelime = new JTextField();
        txtKelime.setBounds(150, 80, 200, 25);
        txtKelime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(txtKelime);

        JLabel lblAnlam = new JLabel("Anlam:");
        lblAnlam.setBounds(50, 120, 100, 25);
        lblAnlam.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(lblAnlam);

        txtAnlam = new JTextField();
        txtAnlam.setBounds(150, 120, 200, 25);
        txtAnlam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(txtAnlam);

        btnEkle = new JButton("Ekle");
        btnEkle.setBounds(370, 100, 90, 35);
        styleButton(btnEkle);
        add(btnEkle);

        btnAnaMenu = new JButton("Ana Menüye Dön");
        btnAnaMenu.setBounds(170, 180, 160, 35);
        styleButton(btnAnaMenu);
        add(btnAnaMenu);

        // Buton olayları
        btnEkle.addActionListener(e -> kelimeEkle());
        btnAnaMenu.addActionListener(e -> {
            new AnaMenu("Kullanıcı").setVisible(true); // Girişten gelen adı kullanabilirsiniz
            dispose();
        });

        setVisible(true);
    }

    private void kelimeEkle() {
        String kelime = txtKelime.getText().trim();
        String anlam = txtAnlam.getText().trim();

        if (kelime.isEmpty() || anlam.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen kelime ve anlam girin.");
            return;
        }

        try (Connection conn = DBhelper.getConnection()) {
            String sql = "INSERT INTO Kelimeler (kelime, anlam, tarih) VALUES (?, ?, GETDATE())";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kelime);
            stmt.setString(2, anlam);

            int result = stmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Kelime başarıyla eklendi!");
                txtKelime.setText("");
                txtAnlam.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Ekleme başarısız.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage());
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(52, 152, 219)); // mavi
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        new KelimeEkle();
    }
}
