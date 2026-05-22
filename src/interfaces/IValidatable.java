package interfaces;

/**
 * Kontrak validasi mandiri tiap entitas.
 * Setiap model mengimplementasi ini dan memvalidasi field-nya sendiri.
 *
 * KONSEP OOP: Interface
 *
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */
public interface IValidatable {
    /**
     * Validasi semua field wajib entitas.
     * 
     * @return true jika valid
     * @throws IllegalArgumentException pesan error spesifik jika tidak valid
     */
    boolean validate() throws IllegalArgumentException;
}