package ekran;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import db.DBhelper;
public class BulmacaUygulamasi extends JFrame {
    private final int satirSayisi = 6;
    private final int kelimeUzunlugu;
    private final String hedefKelime;
    private final JTextField[][] kutular = new JTextField[satirSayisi][];
    private int aktifSatir = 0;
    private final String kullaniciAdi;

    public BulmacaUygulamasi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
        this.hedefKelime = kelimeGetir().toUpperCase();
        this.kelimeUzunlugu = hedefKelime.length();

        setTitle("Bulmaca Oyunu");
        setSize(70 * kelimeUzunlugu, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        JPanel gridPanel = new JPanel(new GridLayout(satirSayisi, kelimeUzunlugu, 5, 5));
        gridPanel.setBackground(Color.BLACK);
        for (int i = 0; i < satirSayisi; i++) {
            kutular[i] = new JTextField[kelimeUzunlugu];
            for (int j = 0; j < kelimeUzunlugu; j++) {
                JTextField tf = new JTextField();
                tf.setFont(new Font("SansSerif", Font.BOLD, 24));
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setEditable(i == 0);
                kutular[i][j] = tf;
                gridPanel.add(tf);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        JButton btnTahmin = new JButton("Tahmin Et");
        btnTahmin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnTahmin.setBackground(new Color(30, 144, 255));
        btnTahmin.setForeground(Color.WHITE);
        btnTahmin.addActionListener(e -> tahminKontrol());
        add(btnTahmin, BorderLayout.SOUTH);

        // Ön ipucu: bazı harfleri göster
        ipucuHarfleriYerlestir();

        // Ana Menüye Dön Butonu
        JButton btnAnaMenu = new JButton("Ana Menüye Dön");
        btnAnaMenu.setBackground(Color.DARK_GRAY);
        btnAnaMenu.setForeground(Color.WHITE);
        btnAnaMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAnaMenu.addActionListener(e -> {
            dispose();
            new AnaMenu(kullaniciAdi).setVisible(true);
        });
        add(btnAnaMenu, BorderLayout.NORTH);
    }

    private void tahminKontrol() {
        StringBuilder tahmin = new StringBuilder();
        for (int i = 0; i < kelimeUzunlugu; i++) {
            String harf = kutular[aktifSatir][i].getText().toUpperCase();
            if (harf.length() != 1 || !Character.isLetter(harf.charAt(0))) {
                JOptionPane.showMessageDialog(this, "Geçerli harfler girin.");
                return;
            }
            tahmin.append(harf);
        }

        String tahminStr = tahmin.toString();
        if (tahminStr.equals(hedefKelime)) {
            renklendir(tahminStr);
            JOptionPane.showMessageDialog(this, "🎉 Tebrikler, doğru tahmin!");
            yeniKelime();
            return;
        }

        renklendir(tahminStr);
        aktifSatir++;
        if (aktifSatir >= satirSayisi) {
            JOptionPane.showMessageDialog(this, "😢 Tahmin hakkın bitti!\nDoğru cevap: " + hedefKelime);
            yeniKelime();
        } else {
            for (int i = 0; i < kelimeUzunlugu; i++) {
                kutular[aktifSatir][i].setEditable(true);
            }
        }
    }

    private void renklendir(String tahmin) {
        for (int i = 0; i < kelimeUzunlugu; i++) {
            char girilen = tahmin.charAt(i);
            JTextField kutu = kutular[aktifSatir][i];
            if (hedefKelime.charAt(i) == girilen) {
                kutu.setBackground(Color.GREEN);
            } else if (hedefKelime.contains(String.valueOf(girilen))) {
                kutu.setBackground(Color.YELLOW);
            } else {
                kutu.setBackground(Color.RED);
            }
            kutu.setEditable(false);
        }
    }

    private void yeniKelime() {
        dispose();
        new BulmacaUygulamasi(kullaniciAdi).setVisible(true);
    }

    private void ipucuHarfleriYerlestir() {
        Random rnd = new Random();
        int ipucuSayisi = rnd.nextInt(2, 4); // 2 veya 3 ipucu harf
        for (int i = 0; i < ipucuSayisi; i++) {
            int pozisyon = rnd.nextInt(kelimeUzunlugu);
            JTextField tf = kutular[0][pozisyon];
            char harf = hedefKelime.charAt(pozisyon);
            tf.setText(String.valueOf(harf));
            tf.setBackground(Color.YELLOW);
        }
    }

    private String kelimeGetir() {
        String sql = "SELECT kelime FROM Kelimeler ORDER BY NEWID()";
        try (Connection conn = DBhelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("kelime");
            } else {
                return "KELIME"; // Yedek kelime
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "HATA";
        }
    }
}
