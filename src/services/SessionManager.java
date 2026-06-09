package services;

import models.Admin;
import models.Masyarakat;
import models.Pengguna;

/**
 * Menyimpan state pengguna yang sedang login.
 * Bisa diakses dari mana saja tanpa membuat object.
 *
 * KONSEP OOP:
 * - Static fields & methods : tidak perlu instance, akses langsung via class
 * - Polymorphism : menyimpan Admin/Masyarakat sebagai tipe Pengguna
 * - instanceof : cek tipe runtime saat diperlukan
 *
 * @author Silfina (Role 2 - Data & Logic Engineer)
 */
public class SessionManager {

    // Static field — satu nilai untuk seluruh program
    private static Pengguna penggunaAktif = null;

    // Semua method static — tidak perlu new SessionManager()
    public static void login(Pengguna p) { 
        penggunaAktif = p; }

    public static void logout() {
        penggunaAktif = null;
    }

    public static Pengguna getPenggunaAktif() {
        return penggunaAktif;
    }

    public static boolean isLoggedIn() {
        return penggunaAktif != null;
    }

    // instanceof — cek tipe runtime object
    public static boolean isAdmin() {
        return penggunaAktif instanceof Admin;
    }

    public static boolean isMasyarakat() {
        return penggunaAktif instanceof Masyarakat;
    }

    // Casting aman — sudah dicek instanceof sebelumnya
    public static Admin getAdmin() {
        return isAdmin() ? (Admin) penggunaAktif : null;
    }

    public static Masyarakat getMasyarakat() {
        return isMasyarakat() ? (Masyarakat) penggunaAktif : null;
    }

    private SessionManager() {
    } // cegah instantiasi

}