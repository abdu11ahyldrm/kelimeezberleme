package kelimeezberleme;

import java.util.*;

public class BulmacaHelper {

    public static LetterResult[] kelimeyiKarsilastir(String tahmin, String hedef) {
        LetterResult[] sonuc = new LetterResult[5];
        boolean[] hedefteKullanildi = new boolean[5];

        // İlk geçiş: Doğru harf ve doğru yer kontrolü (CORRECT)
        for (int i = 0; i < 5; i++) {
            if (tahmin.charAt(i) == hedef.charAt(i)) {
                sonuc[i] = LetterResult.CORRECT;
                hedefteKullanildi[i] = true;
            }
        }

        // İkinci geçiş: Harf var ama yer yanlış (PRESENT)
        for (int i = 0; i < 5; i++) {
            if (sonuc[i] == null) {
                boolean bulundu = false;
                for (int j = 0; j < 5; j++) {
                    if (!hedefteKullanildi[j] && tahmin.charAt(i) == hedef.charAt(j)) {
                        bulundu = true;
                        hedefteKullanildi[j] = true;
                        break;
                    }
                }
                sonuc[i] = bulundu ? LetterResult.PRESENT : LetterResult.ABSENT;
            }
        }

        return sonuc;
    }
}

