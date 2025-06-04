package ekran;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class KelimeListem extends JFrame {

    private JTable tblKelimeler;
    private JScrollPane scrollPane;
    private JButton btnAnaMenu, btnKelimeEkle;

    public KelimeListem() {
        setTitle("Kelime Listesi");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 255)); // pastel açık mor

        setLayout(new BorderLayout(10, 10));

        JLabel lblBaslik = new JLabel("Tüm Kelimeler");
        lblBaslik.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBaslik.setHorizontalAlignment(SwingConstants.CENTER);
        lblBaslik.setForeground(new Color(44, 62, 80));
        add(lblBaslik, BorderLayout.NORTH);

        // Tablo
        tblKelimeler = new JTable();
        scrollPane = new JScrollPane(tblKelimeler);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Buton Paneli
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 255));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnAnaMenu = new JButton("Ana Menüye Dön");
        btnKelimeEkle = new JButton("Yeni Kelime Ekle");
        styleButton(btnAnaMenu);
        styleButton(btnKelimeEkle);

        bottomPanel.add(btnAnaMenu);
        bottomPanel.add(btnKelimeEkle);

        add(bottomPanel, BorderLayout.SOUTH);

        // Buton Olayları
        btnAnaMenu.addActionListener(e -> {
            new AnaMenu("Kullanıcı").setVisible(true);
            dispose();
        });

        btnKelimeEkle.addActionListener(e -> {
            new KelimeEkle().setVisible(true);
            dispose();
        });

        verileriYukle();
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(52, 152, 219)); // mavi
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void verileriYukle() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Kelime");
        model.addColumn("Anlam");
        model.addColumn("Tarih");

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://ABDULLAH\\SQLEXPRESS:1433;databaseName=kelimeezberDB;integratedSecurity=true;encrypt=false;trustServerCertificate=true;");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, kelime, anlam, tarih FROM Kelimeler");

            while (rs.next()) {
                int id = rs.getInt("id");
                String kelime = rs.getString("kelime");
                String anlam = rs.getString("anlam");
                String tarih = rs.getString("tarih");
                model.addRow(new Object[]{id, kelime, anlam, tarih});
            }

            tblKelimeler.setModel(model);

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KelimeListem());
    }
}
