package models;

/**
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */
public class Admin extends Pengguna {

    private String username; // field eksklusif Admin

    public Admin() {
    }

    public Admin(int id, String nama, String username, String password, String statusAkun) {
        super(id, nama, password, statusAkun); // serahkan field bersama ke Pengguna
        this.username = username;
    }

    // Override 2 abstract method dari Pengguna
    @Override
    public String getIdentifier() {
        return username; // Admin login pakai USERNAME
    }

    @Override
    public String getTipeAkun() {
        return "Admin";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    @Override
    public boolean validate() throws IllegalArgumentException {
        super.validate(); // cek nama & password dari Pengguna dulu
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username admin tidak boleh kosong.");
        return true;
    }
}