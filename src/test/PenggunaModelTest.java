package test;

import models.Admin;
import models.Masyarakat;
import models.Pengguna;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk model Pengguna, Admin, Masyarakat.
 *
 * YANG DIUJI:
 * - Inheritance : Admin dan Masyarakat adalah instance Pengguna
 * - Polymorphism : getIdentifier() dan getTipeAkun() berbeda per subclass
 * - Encapsulation : getter/setter berfungsi, field tidak bisa diakses langsung
 * - Validasi : validate() menolak input tidak valid dengan pesan tepat
 *
 * Total: 18 test case
 *
 * @author Dila (Role 4 - QA & Repo Master)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PenggunaModelTest {

    // ═══════════════════════════════════════════════════════
    // BAGIAN 1: INHERITANCE
    // ═══════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("[Inheritance] Admin adalah instance dari Pengguna")
    void testAdminIsInstanceOfPengguna() {
        Admin a = new Admin(1, "Budi", "budi_admin", "pass123", "aktif");
        assertInstanceOf(Pengguna.class, a,
                "Admin harus instanceof Pengguna karena extends Pengguna");
    }

    @Test
    @Order(2)
    @DisplayName("[Inheritance] Masyarakat adalah instance dari Pengguna")
    void testMasyarakatIsInstanceOfPengguna() {
        Masyarakat m = new Masyarakat(1, "Ani", "ani@mail.com", "Jl.A", "pass123", "aktif");
        assertInstanceOf(Pengguna.class, m,
                "Masyarakat harus instanceof Pengguna karena extends Pengguna");
    }

    @Test
    @Order(3)
    @DisplayName("[Inheritance] Admin mewarisi field nama dari Pengguna")
    void testAdminWarisNamaDariPengguna() {
        Admin a = new Admin(1, "Cici", "cici_admin", "pass123", "aktif");
        assertEquals("Cici", a.getNama(),
                "Admin harus bisa akses nama yang diwarisi dari Pengguna");
    }

    @Test
    @Order(4)
    @DisplayName("[Inheritance] Masyarakat mewarisi field statusAkun dari Pengguna")
    void testMasyarakatWarisStatusAkunDariPengguna() {
        Masyarakat m = new Masyarakat(1, "Dedi", "dedi@mail.com", "Jl.D", "pass123", "nonaktif");
        assertEquals("nonaktif", m.getStatusAkun(),
                "Masyarakat harus bisa akses statusAkun yang diwarisi dari Pengguna");
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 2: POLYMORPHISM
    // ═══════════════════════════════════════════════════════

    @Test
    @Order(5)
    @DisplayName("[Polymorphism] getTipeAkun() — Admin return 'Admin'")
    void testAdminGetTipeAkun() {
        Pengguna p = new Admin(1, "Eko", "eko_admin", "pass123", "aktif");
        assertEquals("Admin", p.getTipeAkun());
    }

    @Test
    @Order(6)
    @DisplayName("[Polymorphism] getTipeAkun() — Masyarakat return 'Masyarakat'")
    void testMasyarakatGetTipeAkun() {
        Pengguna p = new Masyarakat(1, "Fitri", "fitri@mail.com", "Jl.F", "pass123", "aktif");
        assertEquals("Masyarakat", p.getTipeAkun());
    }

    @Test
    @Order(7)
    @DisplayName("[Polymorphism] getTipeAkun() — Admin dan Masyarakat berbeda")
    void testTipeAkunBerbeda() {
        Pengguna admin = new Admin(1, "Gani", "gani_admin", "pass123", "aktif");
        Pengguna masy = new Masyarakat(2, "Hana", "hana@mail.com", "Jl.H", "pass123", "aktif");
        assertNotEquals(admin.getTipeAkun(), masy.getTipeAkun(),
                "Tipe akun Admin dan Masyarakat tidak boleh sama");
    }

    @Test
    @Order(8)
    @DisplayName("[Polymorphism] getIdentifier() — Admin return username")
    void testAdminIdentifierIsUsername() {
        Admin a = new Admin(1, "Indah", "indah_admin", "pass123", "aktif");
        assertEquals("indah_admin", a.getIdentifier(),
                "Admin harus login pakai username");
    }

    @Test
    @Order(9)
    @DisplayName("[Polymorphism] getIdentifier() — Masyarakat return email")
    void testMasyarakatIdentifierIsEmail() {
        Masyarakat m = new Masyarakat(1, "Joko", "joko@mail.com", "Jl.J", "pass123", "aktif");
        assertEquals("joko@mail.com", m.getIdentifier(),
                "Masyarakat harus login pakai email");
    }

    @Test
    @Order(10)
    @DisplayName("[Polymorphism] toString() memanggil getTipeAkun() milik subclass")
    void testToStringPolimorfik() {
        Pengguna a = new Admin(1, "Kiki", "kiki_admin", "pass123", "aktif");
        Pengguna m = new Masyarakat(2, "Lina", "lina@mail.com", "Jl.L", "pass123", "aktif");
        assertTrue(a.toString().startsWith("Admin"),
                "toString Admin harus diawali 'Admin'");
        assertTrue(m.toString().startsWith("Masyarakat"),
                "toString Masyarakat harus diawali 'Masyarakat'");
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 3: ENCAPSULATION
    // ═══════════════════════════════════════════════════════

    @Test
    @Order(11)
    @DisplayName("[Encapsulation] Getter/Setter Admin berfungsi dengan benar")
    void testAdminGetterSetter() {
        Admin a = new Admin();
        a.setId(99);
        a.setNama("Mega");
        a.setUsername("mega_admin");
        a.setPassword("megapass");
        a.setStatusAkun("nonaktif");

        assertAll("Admin getter/setter",
                () -> assertEquals(99, a.getId()),
                () -> assertEquals("Mega", a.getNama()),
                () -> assertEquals("mega_admin", a.getUsername()),
                () -> assertEquals("megapass", a.getPassword()),
                () -> assertEquals("nonaktif", a.getStatusAkun()));
    }

    @Test
    @Order(12)
    @DisplayName("[Encapsulation] Getter/Setter Masyarakat berfungsi dengan benar")
    void testMasyarakatGetterSetter() {
        Masyarakat m = new Masyarakat();
        m.setId(55);
        m.setNama("Nana");
        m.setEmail("nana@mail.com");
        m.setAlamat("Jl. Nanas No.7");
        m.setPassword("nanapass");
        m.setStatusAkun("aktif");

        assertAll("Masyarakat getter/setter",
                () -> assertEquals(55, m.getId()),
                () -> assertEquals("Nana", m.getNama()),
                () -> assertEquals("nana@mail.com", m.getEmail()),
                () -> assertEquals("Jl. Nanas No.7", m.getAlamat()),
                () -> assertEquals("nanapass", m.getPassword()),
                () -> assertEquals("aktif", m.getStatusAkun()));
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 4: VALIDASI (IValidatable)
    // ═══════════════════════════════════════════════════════

    @Test
    @Order(13)
    @DisplayName("[Validasi] Admin valid — tidak lempar exception")
    void testAdminValidTidakThrow() {
        Admin a = new Admin(1, "Omar", "omar_admin", "pass123", "aktif");
        assertDoesNotThrow(a::validate,
                "Admin dengan data lengkap tidak boleh throw exception");
    }

    @Test
    @Order(14)
    @DisplayName("[Validasi] Masyarakat valid — tidak lempar exception")
    void testMasyarakatValidTidakThrow() {
        Masyarakat m = new Masyarakat(1, "Putri", "putri@mail.com", "Jl.P", "pass123", "aktif");
        assertDoesNotThrow(m::validate);
    }

    @Test
    @Order(15)
    @DisplayName("[Validasi] Nama kosong — lempar IllegalArgumentException")
    void testValidateNamaKosong() {
        Admin a = new Admin(1, "", "rudi_admin", "pass123", "aktif");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, a::validate);
        assertTrue(ex.getMessage().toLowerCase().contains("nama"),
                "Pesan error harus menyebut 'nama'");
    }

    @Test
    @Order(16)
    @DisplayName("[Validasi] Password kurang dari 6 karakter — lempar exception")
    void testValidatePasswordPendek() {
        Masyarakat m = new Masyarakat(1, "Sari", "sari@mail.com", "Jl.S", "123", "aktif");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, m::validate);
        assertTrue(ex.getMessage().contains("6"),
                "Pesan error harus menyebut angka 6");
    }

    @Test
    @Order(17)
    @DisplayName("[Validasi] Email tanpa '@' — lempar exception")
    void testValidateEmailTidakValid() {
        Masyarakat m = new Masyarakat(1, "Tono", "tono-tanpa-at", "Jl.T", "pass123", "aktif");
        assertThrows(IllegalArgumentException.class, m::validate,
                "Email tanpa @ harus ditolak");
    }

    @Test
    @Order(18)
    @DisplayName("[Validasi] Username Admin kosong — lempar exception")
    void testValidateUsernameAdminKosong() {
        Admin a = new Admin(1, "Umar", "", "pass123", "aktif");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, a::validate);
        assertTrue(ex.getMessage().toLowerCase().contains("username"),
                "Pesan error harus menyebut 'username'");
    }
}