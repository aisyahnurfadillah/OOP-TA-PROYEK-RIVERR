package interfaces;

import models.Pengguna;

/**
 * Kontrak autentikasi login.
 * Diimplementasi oleh AdminService dan MasyarakatService (Role 2 - Silpik).
 *
 * KONSEP OOP: Interface, Polymorphism (return Pengguna = superclass)
 *
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */
public interface IAuthenticatable {
    /**
     * @param identifier email (Masyarakat) atau username (Admin)
     * @param password   password akun
     * @return object Pengguna jika berhasil, null jika gagal
     */
    Pengguna login(String identifier, String password);

    boolean isAktif(Pengguna pengguna);
}