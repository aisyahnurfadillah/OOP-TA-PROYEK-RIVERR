package models;

/**
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */
public class Masyarakat extends Pengguna {
    private String email; // field eksklusif masyarakat
    private String alamat;

    public Masyarakat() {
    }

    public Masyarakat(int id, String nama, String email, String alamat, String password, String statusAkun) {
        super(id, nama, password, statusAkun); // serahkan field bersama ke Pengguna
        this.email = email;
        this.alamat = alamat;
    }

    @Override
    public String getIdentifier() {
        return email; // Masyarakat login pakai EMAIL
    }

    @Override
    public String getTipeAkun() {
        return "Masyarakat";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public boolean validate() throws IllegalArgumentException {
        super.validate(); // cek nama & password dari Pengguna dulu
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException(" Format Email tidak valid.");
        if (alamat == null || alamat.isBlank())
            throw new IllegalArgumentException("alamat tidak boleh kosong.");
        return true;
    }
}
