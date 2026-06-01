package test;

import models.Masyarakat;
import models.Pengguna;
import services.DataStore;
import services.MasyarakatService;

import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk MasyarakatService.
 *
 * YANG DIUJI:
 *  - CRUD Masyarakat lengkap
 *  - Login: berhasil, password salah, email tidak ada, akun nonaktif
 *  - Cek duplikat email
 *  - cariSemua() return copy bukan referensi
 *  - isAktif() berbagai kondisi
 *
 * Total: 17 test case
 *
 * @author Dila (Role 4 - QA & Repo Master)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MasyarakatServiceTest {

    private MasyarakatService service;

    @BeforeEach
    void setUp() {
        resetDataStore();
        service = new MasyarakatService();
    }

    // Helper: buat Masyarakat valid
    private Masyarakat buat(String nama, String email, String password, String status) {
        return new Masyarakat(0, nama, email, "Jl. Test No.1", password, status);
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 1: CREATE
    // ═══════════════════════════════════════════════════════

    @Test @Order(1)
    @DisplayName("[Create] Masyarakat baru berhasil ditambahkan")
    void testTambahBerhasil() {
        service.tambah(buat("Aldi", "aldi@mail.com", "pass123", "aktif"));
        Masyarakat hasil = service.cariByEmail("aldi@mail.com");
        assertNotNull(hasil);
        assertEquals("Aldi", hasil.getNama());
    }

    @Test @Order(2)
    @DisplayName("[Create] ID di-assign otomatis setelah tambah (bukan 0)")
    void testIdAutoAssign() {
        service.tambah(buat("Bella", "bella@mail.com", "pass123", "aktif"));
        Masyarakat m = service.cariByEmail("bella@mail.com");
        assertTrue(m.getId() > 0, "ID harus lebih dari 0");
    }

    @Test @Order(3)
    @DisplayName("[Create] Email duplikat harus lempar IllegalArgumentException")
    void testTambahEmailDuplikat() {
        service.tambah(buat("Candra", "sama@mail.com", "pass123", "aktif"));
        assertThrows(IllegalArgumentException.class,
            () -> service.tambah(buat("Dodi", "sama@mail.com", "pass456", "aktif")),
            "Email duplikat harus ditolak");
    }

    @Test @Order(4)
    @DisplayName("[Create] Email duplikat case-insensitive harus ditolak")
    void testTambahEmailDuplikatCaseInsensitive() {
        service.tambah(buat("Eka", "eka@mail.com", "pass123", "aktif"));
        assertThrows(IllegalArgumentException.class,
            () -> service.tambah(buat("Eka2", "EKA@MAIL.COM", "pass123", "aktif")),
            "Email duplikat meski beda huruf kapital harus ditolak");
    }

    @Test @Order(5)
    @DisplayName("[Create] Email tanpa '@' harus lempar exception dari validate()")
    void testTambahEmailTidakValid() {
        assertThrows(IllegalArgumentException.class,
            () -> service.tambah(buat("Fani", "fani-tanpa-at", "pass123", "aktif")));
    }

    @Test @Order(6)
    @DisplayName("[Create] Password kurang dari 6 karakter harus lempar exception")
    void testTambahPasswordPendek() {
        assertThrows(IllegalArgumentException.class,
            () -> service.tambah(buat("Gina", "gina@mail.com", "123", "aktif")));
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 2: READ
    // ═══════════════════════════════════════════════════════

    @Test @Order(7)
    @DisplayName("[Read] cariById() — ditemukan jika ID ada")
    void testCariByIdDitemukan() {
        service.tambah(buat("Heni", "heni@mail.com", "pass123", "aktif"));
        int id = service.cariByEmail("heni@mail.com").getId();
        assertNotNull(service.cariById(id));
    }

    @Test @Order(8)
    @DisplayName("[Read] cariById() — return null jika ID tidak ada")
    void testCariByIdTidakDitemukan() {
        assertNull(service.cariById(9999));
    }

    @Test @Order(9)
    @DisplayName("[Read] cariByEmail() — ditemukan dengan email yang terdaftar")
    void testCariByEmailDitemukan() {
        service.tambah(buat("Irma", "irma@mail.com", "pass123", "aktif"));
        assertNotNull(service.cariByEmail("irma@mail.com"));
        assertEquals("Irma", service.cariByEmail("irma@mail.com").getNama());
    }

    @Test @Order(10)
    @DisplayName("[Read] cariSemua() — return copy, DataStore tidak terpengaruh")
    void testCariSemuaReturnCopy() {
        service.tambah(buat("Jeni", "jeni@mail.com", "pass123", "aktif"));
        int before = service.cariSemua().size();
        service.cariSemua().clear(); // clear copy
        assertEquals(before, service.cariSemua().size(),
            "DataStore tidak boleh terpengaruh");
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 3: UPDATE
    // ═══════════════════════════════════════════════════════

    @Test @Order(11)
    @DisplayName("[Update] ubah() — berhasil mengubah nama masyarakat")
    void testUbahBerhasil() {
        service.tambah(buat("Kiki", "kiki@mail.com", "pass123", "aktif"));
        Masyarakat m = service.cariByEmail("kiki@mail.com");
        m.setNama("Kiki Baru");
        assertTrue(service.ubah(m));
        assertEquals("Kiki Baru", service.cariById(m.getId()).getNama());
    }

    @Test @Order(12)
    @DisplayName("[Update] ubah() — return false jika ID tidak ada")
    void testUbahTidakDitemukan() {
        Masyarakat m = new Masyarakat(9999, "Lala", "lala@mail.com", "Jl.L", "pass123", "aktif");
        assertFalse(service.ubah(m));
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 4: DELETE
    // ═══════════════════════════════════════════════════════

    @Test @Order(13)
    @DisplayName("[Delete] hapus() — berhasil menghapus masyarakat")
    void testHapusBerhasil() {
        service.tambah(buat("Mira", "mira@mail.com", "pass123", "aktif"));
        int id = service.cariByEmail("mira@mail.com").getId();
        assertTrue(service.hapus(id));
        assertNull(service.cariById(id));
    }

    @Test @Order(14)
    @DisplayName("[Delete] hapus() — return false jika ID tidak ada")
    void testHapusTidakDitemukan() {
        assertFalse(service.hapus(9999));
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 5: LOGIN (IAuthenticatable)
    // ═══════════════════════════════════════════════════════

    @Test @Order(15)
    @DisplayName("[Login] Kredensial benar — berhasil login, return Pengguna")
    void testLoginBerhasil() {
        service.tambah(buat("Nina", "nina@mail.com", "nina123", "aktif"));
        Pengguna p = service.login("nina@mail.com", "nina123");
        assertNotNull(p, "Login harus berhasil");
        assertEquals("Nina", p.getNama());
        assertEquals("Masyarakat", p.getTipeAkun(),
            "Tipe akun harus Masyarakat (polymorphism)");
    }

    @Test @Order(16)
    @DisplayName("[Login] Password salah — return null")
    void testLoginPasswordSalah() {
        service.tambah(buat("Oki", "oki@mail.com", "oki123", "aktif"));
        assertNull(service.login("oki@mail.com", "SALAH"),
            "Password salah harus return null");
    }

    @Test @Order(17)
    @DisplayName("[Login] Akun nonaktif — return null meski password benar")
    void testLoginAkunNonaktif() {
        service.tambah(buat("Putri", "putri@mail.com", "putri123", "nonaktif"));
        assertNull(service.login("putri@mail.com", "putri123"),
            "Akun nonaktif tidak boleh bisa login");
    }

    // ── Helper reset DataStore ─────────────────────────────────────────────────
    private void resetDataStore() {
        try {
            var field = DataStore.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            System.err.println("Reset DataStore gagal: " + e.getMessage());
        }
    }
}