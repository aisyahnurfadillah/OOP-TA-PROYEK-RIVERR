package interfaces;

/**
 * untuk menjebak data kosong atau manipulasi ilegal
 */
public class InputTidakValidException extends Exception {
    public InputTidakValidException(String pesan) {
        super(pesan);
    }
}