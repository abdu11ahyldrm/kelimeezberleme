package kelimeezberleme;

import javax.swing.*;
import ekran.KullaniciPaneli;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new KullaniciPaneli().setVisible(true);
        });
    }
}
