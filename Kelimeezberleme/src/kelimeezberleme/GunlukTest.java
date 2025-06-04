package kelimeezberleme;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ekran.AnaMenu;
public class GunlukTest extends JFrame {

    private List<Kelime> kelimeListesi;
    private int soruIndex = 0;
    private int dogruSayisi = 0;
    private int yanlisSayisi = 0;
    private List<Kelime> yanlisKelimeler = new ArrayList<>();

    private JLabel lblSoru;
    private JTextField txtCevap;
    private JButton btnKontrol, btnAnaMenu, btnYeniTest;
    private JTextArea txtSonuc;

    private String kullaniciAdi; // kullanıcı adını tutacak

    public GunlukTest(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;

        setTitle("Günlük Test - 6 Sefer Tekrar");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        lblSoru = new JLabel("Soru yüklendi...");
        lblSoru.setFont(new Font("Arial", Font.BOLD, 18));
        lblSoru.setForeground(Color.WHITE);

        txtCevap = new JTextField();
        btnKontrol = new JButton("Kontrol Et");
        btnAnaMenu = new JButton("Ana Menüye Dön");
        btnYeniTest = new JButton("Yeni Test");

        txtSonuc = new JTextArea();
        txtSonuc.setEditable(false);
        txtSonuc.setFont(new Font("Arial", Font.BOLD, 14));
        txtSonuc.setForeground(Color.WHITE);
        txtSonuc.setBackground(Color.DARK_GRAY);

        JPanel panelUst = new JPanel(new GridLayout(4, 1, 5, 5));
        panelUst.setBackground(Color.BLACK);
        panelUst.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelUst.add(lblSoru);
        panelUst.add(txtCevap);
        panelUst.add(btnKontrol);

        JPanel panelAlt = new JPanel(new FlowLayout());
        panelAlt.setBackground(Color.BLACK);
        panelAlt.add(btnAnaMenu);
        panelAlt.add(btnYeniTest);

        add(panelUst, BorderLayout.NORTH);
        add(new JScrollPane(txtSonuc), BorderLayout.CENTER);
        add(panelAlt, BorderLayout.SOUTH);

        btnKontrol.addActionListener(e -> cevabiKontrolEt());
        btnAnaMenu.addActionListener(e -> {
            dispose();
            new AnaMenu(kullaniciAdi).setVisible(true);
        });
        btnYeniTest.addActionListener(e -> {
            soruIndex = 0;
            dogruSayisi = 0;
            yanlisSayisi = 0;
            yanlisKelimeler.clear();
            kelimeleriGetir();
            testiBaslat();
        });

        kelimeleriGetir();
        testiBaslat();
    }

    private void kelimeleriGetir() {
        kelimeListesi = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlserver://ABDULLAH\\SQLEXPRESS:1433;databaseName=kelimeezberDB;integratedSecurity=true;encrypt=false;trustServerCertificate=true;");
            Statement stmt = conn.createStatement();

            // Eğer kullanıcıya özel kelimeler varsa buraya kullanıcı filtresi eklenebilir
            ResultSet rs = stmt.executeQuery("SELECT TOP 5 * FROM Kelimeler WHERE dogruSayisi < 6 ORDER BY NEWID()");

            while (rs.next()) {
                int id = rs.getInt("id");
                String kelime = rs.getString("kelime");
                String anlam = rs.getString("anlam");
                int dogruSayisi = rs.getInt("dogruSayisi");
                kelimeListesi.add(new Kelime(id, kelime, anlam, dogruSayisi));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
    }

    private void testiBaslat() {
        if (kelimeListesi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Test için kelime bulunamadı.");
            btnKontrol.setEnabled(false);
            return;
        }
        soruyuGoster();
    }

    private void soruyuGoster() {
        if (soruIndex < kelimeListesi.size()) {
            Kelime k = kelimeListesi.get(soruIndex);
            lblSoru.setText((soruIndex + 1) + ". " + k.kelime + " kelimesinin anlamı nedir?");
            txtCevap.setText("");
            txtSonuc.setText("");
            btnKontrol.setEnabled(true);
            txtCevap.setEnabled(true);
        } else {
            testiBitir();
        }
    }

    private void cevabiKontrolEt() {
        if (soruIndex >= kelimeListesi.size()) return;

        Kelime k = kelimeListesi.get(soruIndex);
        String cevap = txtCevap.getText().trim();

        if (cevap.equalsIgnoreCase(k.anlam)) {
            dogruSayisi++;
            kelimeyiGuncelle(k.id, k.dogruSayisi + 1);
        } else {
            yanlisSayisi++;
            yanlisKelimeler.add(k);
        }

        soruIndex++;
        soruyuGoster();
    }

    private void kelimeyiGuncelle(int id, int yeniDogruSayisi) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlserver://ABDULLAH\\SQLEXPRESS:1433;databaseName=kelimeezberDB;integratedSecurity=true;encrypt=false;trustServerCertificate=true;");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE Kelimeler SET dogruSayisi = ? WHERE id = ?");
            pstmt.setInt(1, yeniDogruSayisi);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Güncelleme hatası: " + e.getMessage());
        }
    }

    private void testiBitir() {
        lblSoru.setText("Test tamamlandı.");
        txtCevap.setEnabled(false);
        btnKontrol.setEnabled(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Doğru: ").append(dogruSayisi).append("\n");
        sb.append("Yanlış: ").append(yanlisSayisi).append("\n\n");

        if (!yanlisKelimeler.isEmpty()) {
            sb.append("Yanlış Yapılan Kelimeler:\n");
            for (Kelime k : yanlisKelimeler) {
                sb.append("- ").append(k.kelime).append(" → ").append(k.anlam).append("\n");
            }
        }

        txtSonuc.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GunlukTest("TestKullanici").setVisible(true));
    }

    class Kelime {
        int id;
        String kelime;
        String anlam;
        int dogruSayisi;

        public Kelime(int id, String kelime, String anlam, int dogruSayisi) {
            this.id = id;
            this.kelime = kelime;
            this.anlam = anlam;
            this.dogruSayisi = dogruSayisi;
        }
    }
}
