package exceptions;

/**
 * Custom Exception untuk input yang tidak valid dari pengguna.
 *
 * @author Silfina (254311007)
 */
public class InputTidakValidException extends Exception {
    public InputTidakValidException(String pesan) {
        super(pesan);
    }
}