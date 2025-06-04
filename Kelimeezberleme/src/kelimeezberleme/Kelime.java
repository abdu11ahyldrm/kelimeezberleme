package kelimeezberleme;

public class Kelime {
    private int id;
    private String kelime;
    private String anlam;

    public Kelime(int id, String kelime, String anlam) {
        this.id = id;
        this.kelime = kelime;
        this.anlam = anlam;
    }

    public int getId() {
        return id;
    }

    public String getKelime() {
        return kelime;
    }

    public String getAnlam() {
        return anlam;
    }
}
