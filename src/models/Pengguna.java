package models;

import interfaces.IValidatable;

/**
 * SUPERCLASS — Abstract Class
 * Admin dan Masyarakat adalah SUBCLASS dari ini.
 *
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */
public abstract class Pengguna implements IValidatable {

    // ENCAPSULATION: field protected
    // protected = bisa diakses subclass (Admin, Masyarakat)
    // TIDAK bisa diakses dari class luar
    protected int id;
    protected String nama;
    protected String password;
    protected String statusAkun; // "aktif" | "nonaktif"

    public Pengguna() {
    }

    public Pengguna(int id, String nama, String password, String statusAkun) {
        this.id = id;
        this.nama = nama;
        this.password = password;
        this.statusAkun = statusAkun;
    }

    // ABSTRACT METHOD — wajib diimplementasi subclass
    // Jika subclass tidak mengimplementasi → compile error

    /** Admin return username | Masyarakat return email */
    public abstract String getIdentifier();

    /** Admin return "Admin" | Masyarakat return "Masyarakat" */
    public abstract String getTipeAkun();

    // GETTER & SETTER (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pw) {
        this.password = pw;
    }

    public String getStatusAkun() {
        return statusAkun;
    }

    public void setStatusAkun(String status) {
        this.statusAkun = status;
    }

    // IValidatable — validasi field bersama
    // Subclass panggil super.validate() lalu tambah validasi field eksklusif
    @Override
    public boolean validate() throws IllegalArgumentException {
        if (nama == null || nama.isBlank())
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Password minimal 6 karakter.");
        return true;
    }

    @Override
    public String toString() {
        // getTipeAkun() di sini adalah RUNTIME POLYMORPHISM
        // Java otomatis pakai implementasi subclass yang sebenarnya
        return getTipeAkun() + "{id=" + id + ", nama=" + nama + ", status=" + statusAkun + "}";
    }
}