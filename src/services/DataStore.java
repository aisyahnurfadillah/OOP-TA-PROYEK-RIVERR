package services;

import models.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pusat penyimpanan data in-memory menggunakan ArrayList.
 * Semua Service mengambil referensi List dari class ini.
 *
 * KONSEP OOP:
 * - Singleton Pattern : hanya satu instance selama aplikasi jalan
 * - Collections : data disimpan di ArrayList, bukan database
 * - Encapsulation : List hanya bisa diakses lewat getter
 *
 * @author Silfina (Role 2 - Data & Logic Engineer)
 */
public class DataStore {

    // SINGLETON: satu instance untuk seluruh program
    private static DataStore instance;

    // COLLECTIONS: satu ArrayList per entitas
    private final List<Masyarakat> masyarakats = new ArrayList<>();
    private final List<Admin> admins = new ArrayList<>();
    private final List<Sungai> sungais = new ArrayList<>();
    private final List<TitikPantau> titikPantaus = new ArrayList<>();
    private final List<Laporan> laporans = new ArrayList<>();
    private final List<Validasi> validasis = new ArrayList<>();
    private final List<TindakLanjut> tindakLanjuts = new ArrayList<>();
    private final List<Pengguna> penggunas = new ArrayList<>(); // penambahan untuk service baru
    // ID auto-increment — pengganti AUTO_INCREMENT database
    private int idMasyarakatCounter = 1;
    private int idAdminCounter = 1;
    private int idSungaiCounter = 1;
    private int idTitikCounter = 1;
    private int idLaporanCounter = 1;
    private int idValidasiCounter = 1;
    private int idTindakCounter = 1;

    private int idPenggunaCounter = 1;

    // Constructor private — tidak bisa di-new dari luar class
    private DataStore() {
        seedData();
    }

    // Satu-satunya cara mendapat instance DataStore
    public static DataStore getInstance() {
        if (instance == null)
            instance = new DataStore();
        return instance;
    }

    // Getter — kembalikan List langsung agar Service bisa modifikasi
    public List<Masyarakat> getMasyarakats() {
        return masyarakats;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public List<Sungai> getSungais() {
        return sungais;
    }

    public List<TitikPantau> getTitikPantaus() {
        return titikPantaus;
    }

    public List<Laporan> getLaporans() {
        return laporans;
    }

    public List<Validasi> getValidasis() {
        return validasis;
    }

    public List<TindakLanjut> getTindakLanjuts() {
        return tindakLanjuts;
    }

    // ID generator — dipanggil Service sebelum add ke list
    public int nextIdMasyarakat() {
        return idMasyarakatCounter++;
    }

    public int nextIdAdmin() {
        return idAdminCounter++;
    }

    public int nextIdSungai() {
        return idSungaiCounter++;
    }

    public int nextIdTitik() {
        return idTitikCounter++;
    }

    public int nextIdLaporan() {
        return idLaporanCounter++;
    }

    public int nextIdValidasi() {
        return idValidasiCounter++;
    }

    public int nextIdTindak() {
        return idTindakCounter++;
    }

    public List<Pengguna> getPenggunas() {
        return penggunas;
    }

    public int nextIdPengguna() {
        return idPenggunaCounter++;
    }

    /**
     * Data awal agar aplikasi tidak kosong saat pertama dijalankan.
     * Dipanggil otomatis di constructor private.
     */
    private void seedData() {
        // Admin default → login: admin / admin123
        admins.add(new Admin(nextIdAdmin(), "Admin Utama", "admin", "admin123", "aktif"));

        // Masyarakat contoh → login: budi@email.com / budi123
        masyarakats.add(new Masyarakat(nextIdMasyarakat(),
                "Budi Santoso", "budi@email.com", "Jl. Merdeka No.1", "budi123", "aktif"));

        // Sungai
        Sungai s1 = new Sungai(nextIdSungai(), "Sungai Brantas", "Jawa Timur");
        Sungai s2 = new Sungai(nextIdSungai(), "Sungai Bengawan Solo", "Jawa Tengah");
        sungais.add(s1);
        sungais.add(s2);

        // Titik pantau per sungai
        titikPantaus.add(new TitikPantau(nextIdTitik(), s1.getIdSungai(), -7.9797, 112.6304));
        titikPantaus.add(new TitikPantau(nextIdTitik(), s1.getIdSungai(), -7.9820, 112.6100));
        titikPantaus.add(new TitikPantau(nextIdTitik(), s2.getIdSungai(), -7.5500, 110.8300));

        // PENAMBAHAN DATA PETUGAS: Seed data awal untuk Petugas Lapangan agar list
        // tidak kosong saat awal running
        penggunas.add(new PetugasLapangan(nextIdPengguna(), "Bangkitscl", "petugas123", "aktif", "SHT-001"));
    }
}
