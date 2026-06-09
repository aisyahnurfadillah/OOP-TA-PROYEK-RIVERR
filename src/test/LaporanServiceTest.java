package test;

import models.Laporan;
import models.Validasi;
import models.TindakLanjut;
import services.DataStore;
import services.LaporanService;
import services.ValidasiService;
import services.TindakLanjutService;

import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk LaporanService, ValidasiService, TindakLanjutService.
 *
 * YANG DIUJI:
 *  - CRUD Laporan lengkap
 *  - Filter cariByMasyarakat dan cariByStatus
 *  - Lifecycle status: menunggu → diproses/ditolak → selesai
 *  - Business rules ValidasiService (laporan exist, status menunggu)
 *  - Business rules TindakLanjutService (laporan berstatus diproses)
 *  - Auto-update status laporan oleh ValidasiService dan TindakLanjutService
 *
 * Total: 25 test case
 *
 * @author Dila (Role 4 - QA & Repo Master)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LaporanServiceTest {

    private LaporanService     laporanSvc;
    private ValidasiService    validasiSvc;
    private TindakLanjutService tindakSvc;

    @BeforeEach
    void setUp() {
        resetDataStore();
        laporanSvc  = new LaporanService();
        validasiSvc = new ValidasiService();
        tindakSvc   = new TindakLanjutService();
    }

    // Helper: buat laporan valid siap pakai
    private Laporan buatLaporan(int idMasyarakat, String jenis, String desk) {
        return new Laporan(0, idMasyarakat, 1, jenis, "sedang", desk, LocalDate.now());
    }

    // Helper: buat validasi
    private Validasi buatValidasi(int idLaporan, String hasil) {
        return new Validasi(0, idLaporan, 1, hasil, LocalDate.now(), "catatan test");
    }

    // Helper: buat tindak lanjut
    private TindakLanjut buatTindak(int idLaporan) {
        return new TindakLanjut(0, idLaporan, 1, "Detail tindakan test", LocalDate.now());
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 1: CREATE
    // ═══════════════════════════════════════════════════════

    @Test @Order(1)
    @DisplayName("[Create] Laporan valid berhasil ditambahkan")
    void testTambahLaporanValid() {
        laporanSvc.tambah(buatLaporan(1, "Limbah Industri", "Air berwarna hitam."));
        assertEquals(1, laporanSvc.cariSemua().size());
    }

    @Test @Order(2)
    @DisplayName("[Create] Status awal laporan harus 'menunggu'")
    void testStatusAwalMenunggu() {
        laporanSvc.tambah(buatLaporan(1, "Sampah", "Sampah menumpuk."));
        Laporan l = laporanSvc.cariSemua().get(0);
        assertEquals(Laporan.STATUS_MENUNGGU, l.getStatusLaporan(),
            "Laporan baru harus berstatus menunggu");
    }

    @Test @Order(3)
    @DisplayName("[Create] Tanggal laporan diisi otomatis oleh Service")
    void testTanggalDiisiOtomatis() {
        laporanSvc.tambah(buatLaporan(1, "Minyak", "Tumpahan minyak."));
        Laporan l = laporanSvc.cariSemua().get(0);
        assertNotNull(l.getTanggalLaporan(), "Tanggal harus diisi otomatis");
        assertEquals(LocalDate.now(), l.getTanggalLaporan());
    }

    @Test @Order(4)
    @DisplayName("[Create] ID laporan di-assign otomatis (bukan 0)")
    void testIdAutoAssign() {
        laporanSvc.tambah(buatLaporan(1, "Limbah", "Deskripsi."));
        Laporan l = laporanSvc.cariSemua().get(0);
        assertTrue(l.getIdLaporan() > 0, "ID harus lebih dari 0 setelah disimpan");
    }

    @Test @Order(5)
    @DisplayName("[Create] Dua laporan mendapat ID berbeda (auto-increment)")
    void testDuaLaporanIdBerbeda() {
        laporanSvc.tambah(buatLaporan(1, "Minyak", "Laporan 1."));
        laporanSvc.tambah(buatLaporan(1, "Sampah", "Laporan 2."));
        List<Laporan> list = laporanSvc.cariSemua();
        assertNotEquals(list.get(0).getIdLaporan(), list.get(1).getIdLaporan());
    }

    @Test @Order(6)
    @DisplayName("[Create] Deskripsi kosong harus lempar IllegalArgumentException")
    void testTambahDeskripsiKosong() {
        assertThrows(IllegalArgumentException.class,
            () -> laporanSvc.tambah(buatLaporan(1, "Sampah", "")));
    }

    @Test @Order(7)
    @DisplayName("[Create] Jenis pencemaran kosong harus lempar exception")
    void testTambahJenisKosong() {
        assertThrows(IllegalArgumentException.class,
            () -> laporanSvc.tambah(buatLaporan(1, "", "Deskripsi ada.")));
    }

    @Test @Order(8)
    @DisplayName("[Create] ID masyarakat 0 atau negatif harus lempar exception")
    void testTambahIdMasyarakatTidakValid() {
        assertThrows(IllegalArgumentException.class,
            () -> laporanSvc.tambah(buatLaporan(0, "Sampah", "Deskripsi ada.")));
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 2: READ
    // ═══════════════════════════════════════════════════════

    @Test @Order(9)
    @DisplayName("[Read] cariById() — ditemukan jika ID ada")
    void testCariByIdDitemukan() {
        laporanSvc.tambah(buatLaporan(1, "Minyak", "Deskripsi."));
        int id = laporanSvc.cariSemua().get(0).getIdLaporan();
        Laporan hasil = laporanSvc.cariById(id);
        assertNotNull(hasil);
        assertEquals("Minyak", hasil.getJenisPencemaran());
    }

    @Test @Order(10)
    @DisplayName("[Read] cariById() — return null jika ID tidak ada")
    void testCariByIdTidakDitemukan() {
        assertNull(laporanSvc.cariById(9999));
    }

    @Test @Order(11)
    @DisplayName("[Read] cariSemua() — return copy, bukan referensi langsung")
    void testCariSemuaReturnCopy() {
        laporanSvc.tambah(buatLaporan(1, "Sampah", "Deskripsi."));
        int before = laporanSvc.cariSemua().size();
        laporanSvc.cariSemua().clear(); // clear copy
        assertEquals(before, laporanSvc.cariSemua().size(),
            "DataStore tidak boleh terpengaruh jika copy di-clear");
    }

    @Test @Order(12)
    @DisplayName("[Read] cariByMasyarakat() — filter laporan per user")
    void testCariByMasyarakat() {
        laporanSvc.tambah(buatLaporan(1, "Minyak", "Laporan user 1 — A."));
        laporanSvc.tambah(buatLaporan(2, "Sampah", "Laporan user 2."));
        laporanSvc.tambah(buatLaporan(1, "Limbah", "Laporan user 1 — B."));

        assertEquals(2, laporanSvc.cariByMasyarakat(1).size(),
            "User 1 harus punya 2 laporan");
        assertEquals(1, laporanSvc.cariByMasyarakat(2).size(),
            "User 2 harus punya 1 laporan");
        assertEquals(0, laporanSvc.cariByMasyarakat(99).size(),
            "User 99 tidak punya laporan");
    }

    @Test @Order(13)
    @DisplayName("[Read] cariByStatus() — filter berdasarkan status")
    void testCariByStatus() {
        laporanSvc.tambah(buatLaporan(1, "Minyak", "Laporan A."));
        laporanSvc.tambah(buatLaporan(1, "Sampah", "Laporan B."));
        assertEquals(2, laporanSvc.cariByStatus("menunggu").size());
        assertEquals(0, laporanSvc.cariByStatus("selesai").size());
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 3: UPDATE
    // ═══════════════════════════════════════════════════════

    @Test @Order(14)
    @DisplayName("[Update] ubah() — berhasil mengubah deskripsi laporan")
    void testUbahLaporan() {
        laporanSvc.tambah(buatLaporan(1, "Minyak", "Deskripsi awal."));
        Laporan l = laporanSvc.cariSemua().get(0);
        l.setDeskripsi("Deskripsi sudah diubah.");
        assertTrue(laporanSvc.ubah(l));
        assertEquals("Deskripsi sudah diubah.",
            laporanSvc.cariById(l.getIdLaporan()).getDeskripsi());
    }

    @Test @Order(15)
    @DisplayName("[Update] ubah() — return false jika ID tidak ada")
    void testUbahTidakDitemukan() {
        Laporan l = new Laporan(9999, 1, 1, "Minyak", "sedang", "test", LocalDate.now());
        assertFalse(laporanSvc.ubah(l));
    }

    @Test @Order(16)
    @DisplayName("[Update] ubahStatus() — status berubah sesuai nilai baru")
    void testUbahStatus() {
        laporanSvc.tambah(buatLaporan(1, "Minyak", "Test."));
        int id = laporanSvc.cariSemua().get(0).getIdLaporan();
        assertTrue(laporanSvc.ubahStatus(id, Laporan.STATUS_DIPROSES));
        assertEquals(Laporan.STATUS_DIPROSES, laporanSvc.cariById(id).getStatusLaporan());
    }

    @Test @Order(17)
    @DisplayName("[Update] ubahStatus() — return false jika laporan tidak ada")
    void testUbahStatusTidakDitemukan() {
        assertFalse(laporanSvc.ubahStatus(9999, "selesai"));
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 4: DELETE
    // ═══════════════════════════════════════════════════════

    @Test @Order(18)
    @DisplayName("[Delete] hapus() — laporan berhasil dihapus")
    void testHapusLaporan() {
        laporanSvc.tambah(buatLaporan(1, "Sampah", "Akan dihapus."));
        int id = laporanSvc.cariSemua().get(0).getIdLaporan();
        assertTrue(laporanSvc.hapus(id));
        assertNull(laporanSvc.cariById(id));
        assertEquals(0, laporanSvc.cariSemua().size());
    }

    @Test @Order(19)
    @DisplayName("[Delete] hapus() — return false jika ID tidak ada")
    void testHapusTidakDitemukan() {
        assertFalse(laporanSvc.hapus(9999));
    }

    // ═══════════════════════════════════════════════════════
    // BAGIAN 5: LIFECYCLE STATUS (Validasi + TindakLanjut)
    // ═══════════════════════════════════════════════════════

    @Test @Order(20)
    @DisplayName("[Lifecycle] Validasi 'valid' → status laporan jadi 'diproses'")
    void testValidasiValidUpdateStatusDiproses() {
        laporanSvc.tambah(buatLaporan(1, "Minyak", "Pencemaran berat."));
        int id = laporanSvc.cariSemua().get(0).getIdLaporan();

        validasiSvc.tambah(buatValidasi(id, Validasi.HASIL_VALID));

        assertEquals(Laporan.STATUS_DIPROSES, laporanSvc.cariById(id).getStatusLaporan(),
            "Setelah validasi valid, status harus 'diproses'");
    }

    @Test @Order(21)
    @DisplayName("[Lifecycle] Validasi 'tidak_valid' → status laporan jadi 'ditolak'")
    void testValidasiTidakValidUpdateStatusDitolak() {
        laporanSvc.tambah(buatLaporan(1, "Sampah", "Deskripsi."));
        int id = laporanSvc.cariSemua().get(0).getIdLaporan();

        validasiSvc.tambah(buatValidasi(id, Validasi.HASIL_TIDAK_VALID));

        assertEquals(Laporan.STATUS_DITOLAK, laporanSvc.cariById(id).getStatusLaporan(),
            "Setelah validasi tidak_valid, status harus 'ditolak'");
    }

    @Test @Order(22)
    @DisplayName("[Lifecycle] TindakLanjut → status laporan jadi 'selesai'")
    void testTindakLanjutUpdateStatusSelesai() {
        laporanSvc.tambah(buatLaporan(1, "Limbah", "Deskripsi."));
        int id = laporanSvc.cariSemua().get(0).getIdLaporan();

        // Validasi dulu
        validasiSvc.tambah(buatValidasi(id, Validasi.HASIL_VALID));
        // Tindak lanjut
        tindakSvc.tambah(buatTindak(id));

        assertEquals(Laporan.STATUS_SELESAI, laporanSvc.cariById(id).getStatusLaporan(),
            "Setelah tindak lanjut, status harus 'selesai'");
    }

    @Test @Order(23)
    @DisplayName("[BusinessRule] Validasi laporan yang tidak ada harus throw exception")
    void testValidasiLaporanTidakAda() {
        Validasi v = buatValidasi(9999, Validasi.HASIL_VALID);
        assertThrows(IllegalArgumentException.class,
            () -> validasiSvc.tambah(v),
            "Validasi laporan tidak ada harus throw IllegalArgumentException");
    }

    @Test @Order(24)
    @DisplayName("[BusinessRule] Laporan tidak bisa divalidasi dua kali")
    void testValidasiDuaKali() {
        laporanSvc.tambah(buatLaporan(1, "Minyak", "Deskripsi."));
        int id = laporanSvc.cariSemua().get(0).getIdLaporan();
        validasiSvc.tambah(buatValidasi(id, Validasi.HASIL_VALID));

        // Validasi kedua harus ditolak karena status sudah bukan 'menunggu'
        assertThrows(IllegalArgumentException.class,
            () -> validasiSvc.tambah(buatValidasi(id, Validasi.HASIL_VALID)),
            "Laporan sudah divalidasi tidak boleh divalidasi lagi");
    }

    @Test @Order(25)
    @DisplayName("[BusinessRule] TindakLanjut pada laporan 'menunggu' harus throw exception")
    void testTindakLanjutTanpaValidasi() {
        laporanSvc.tambah(buatLaporan(1, "Limbah", "Deskripsi."));
        int id = laporanSvc.cariSemua().get(0).getIdLaporan();

        // Langsung tindak lanjut tanpa validasi dulu
        assertThrows(IllegalArgumentException.class,
            () -> tindakSvc.tambah(buatTindak(id)),
            "TindakLanjut pada laporan 'menunggu' harus throw exception");
    }

    // ── Helper reset DataStore ────────────────────────────────────────────────
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