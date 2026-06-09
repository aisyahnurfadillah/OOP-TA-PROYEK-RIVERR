package models;

/**
 * Subclass dari Laporan untuk laporan pencemaran yang bersifat darurat.
 *
 * KONSEP OOP:
 *  - Inheritance   : extends Laporan
 *  - Polymorphism  : override toString()
 *  - Encapsulation : tingkatUrgensi private, akses via getter/setter
 *
 * @author Silfina (254311007)
 */
public class LaporanUrgent extends Laporan {

    private String tingkatUrgensi; // "TINGGI" atau "KRITIS"

    public LaporanUrgent() {
        super();
    }

    public LaporanUrgent(int idMasyarakat, int idTitikPantau,
                        String jenisPencemaran, String tingkatPencemaran,
                        String deskripsi, String tingkatUrgensi) {
        super(0, idMasyarakat, idTitikPantau,
            jenisPencemaran, tingkatPencemaran, deskripsi, java.time.LocalDate.now());
        this.tingkatUrgensi = tingkatUrgensi;
    }

    public String getTingkatUrgensi() { return tingkatUrgensi; }

    public void setTingkatUrgensi(String tingkatUrgensi) {
        this.tingkatUrgensi = tingkatUrgensi;
    }

   @Override
    public boolean validate() throws IllegalArgumentException {
        if (getIdMasyarakat() <= 0)
            throw new IllegalArgumentException("ID Masyarakat tidak valid.");
        if (getJenisPencemaran() == null || getJenisPencemaran().isBlank())
            throw new IllegalArgumentException("Jenis pencemaran tidak boleh kosong.");
        if (getDeskripsi() == null || getDeskripsi().isBlank())
            throw new IllegalArgumentException("Deskripsi tidak boleh kosong.");
        if (tingkatUrgensi == null || tingkatUrgensi.isBlank())
            throw new IllegalArgumentException("Tingkat urgensi tidak boleh kosong.");
        if (!tingkatUrgensi.equalsIgnoreCase("TINGGI") &&
            !tingkatUrgensi.equalsIgnoreCase("KRITIS"))
            throw new IllegalArgumentException("Tingkat urgensi harus TINGGI atau KRITIS.");
        return true;
    }

    @Override
    public String toString() {
        return "[!! URGENT - " + tingkatUrgensi.toUpperCase() + "] " + super.toString();
    }
}