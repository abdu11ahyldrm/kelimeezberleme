package ekran;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import kelimeezberleme.GunlukTest;

public class AnaMenu extends JFrame {

    private String kullaniciAdi;

    public AnaMenu(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
        setTitle("Kelime Ezberleme Uygulaması - Ana Menü");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Hoş geldiniz etiketi
        JLabel lblHosgeldin = new JLabel("Hoş geldiniz, " + kullaniciAdi + " 👋");
        lblHosgeldin.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHosgeldin.setHorizontalAlignment(SwingConstants.CENTER);
        lblHosgeldin.setForeground(new Color(0, 102, 153));
        lblHosgeldin.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(lblHosgeldin, BorderLayout.NORTH);

        // Butonlar paneli
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton btnKelimeEkle = new JButton("Kelime Ekle");
        JButton btnKelimeListesi = new JButton("Kelime Listesi");
        JButton btnGunlukTest = new JButton("Günlük Test");
        JButton btnBulmaca = new JButton("Bulmaca");

        Color btnColor = new Color(0, 123, 255);
        Font btnFont = new Font("Segoe UI", Font.PLAIN, 16);
        JButton[] buttons = { btnKelimeEkle, btnKelimeListesi, btnGunlukTest, btnBulmaca };
        for (JButton btn : buttons) {
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(btnFont);
            btn.setFocusPainted(false);
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);

        // Çıkış butonu
        JButton btnCikis = new JButton("Çıkış Yap");
        btnCikis.setBackground(Color.DARK_GRAY);
        btnCikis.setForeground(Color.WHITE);
        btnCikis.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCikis.setFocusPainted(false);
        btnCikis.setPreferredSize(new Dimension(100, 40));
        add(btnCikis, BorderLayout.SOUTH);

        // --- Buton Aksiyonları ---
        btnKelimeEkle.addActionListener(e -> {
            new KelimeEkle().setVisible(true);
            dispose();
        });

        btnKelimeListesi.addActionListener(e -> {
            new KelimeListem().setVisible(true);
            dispose();
        });

        btnGunlukTest.addActionListener(e -> {
            new GunlukTest(kullaniciAdi).setVisible(true);
            dispose();
        });

        btnBulmaca.addActionListener(e -> {
            new BulmacaUygulamasi(kullaniciAdi).setVisible(true);
            dispose();
        });

        btnCikis.addActionListener(e -> {
            int secim = JOptionPane.showConfirmDialog(this, "Çıkmak istediğinize emin misiniz?", "Çıkış", JOptionPane.YES_NO_OPTION);
            if (secim == JOptionPane.YES_OPTION) {
                new KullaniciPaneli().setVisible(true);
                dispose();
            }
        });
    }

    // Test için main metodu (dilersen silebilirsin)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnaMenu("TestKullanici").setVisible(true));
    }
}
