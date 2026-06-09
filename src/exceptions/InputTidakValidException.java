package exceptions;

/**
 * BAGIAN C - Custom Exception
 * @author Aisyah Nur Fadillah (254311001) - Role 4: QA & Repo Master
 */
public class InputTidakValidException extends Exception {

    private final String namaField;

    public InputTidakValidException(String namaField, String pesan) {
        super("[VALIDASI GAGAL] Field '" + namaField + "': " + pesan);
        this.namaField = namaField;
    }

    public String getNamaField() {
        return namaField;
    }
}
