package services;

import interfaces.IManageable;
import models.Laporan;
import models.Validasi;

import java.util.ArrayList;
import java.util.List;

/**
 * Service validasi laporan oleh admin.
 *
 * ATURAN BISNIS (business rules):
 *  1. Laporan harus ada sebelum bisa divalidasi
 *  2. Laporan harus berstatus "menunggu" — tidak bisa divalidasi dua kali
 *  3. hasil "valid"       → status laporan jadi "diproses"
 *  4. hasil "tidak_valid" → status laporan jadi "ditolak"
 *
 * @author Silfina (Role 2 - Data & Logic Engineer)
 */
public class ValidasiService implements IManageable<Validasi, Integer> {

    private final DataStore      store      = DataStore.getInstance();
    private final LaporanService laporanSvc = new LaporanService();

    @Override
    public void tambah(Validasi v) {
        v.validate(); // cek field Validasi sendiri dulu

        // ATURAN BISNIS 1: laporan harus ada
        Laporan l = laporanSvc.cariById(v.getIdLaporan());
        if (l == null)
            throw new IllegalArgumentException(
                "Laporan #" + v.getIdLaporan() + " tidak ditemukan.");

        // ATURAN BISNIS 2: status harus "menunggu"
        if (!Laporan.STATUS_MENUNGGU.equals(l.getStatusLaporan()))
            throw new IllegalArgumentException(
                "Laporan sudah pernah diproses. Status saat ini: " + l.getStatusLaporan());

        v.setIdValidasi(store.nextIdValidasi());
        store.getValidasis().add(v);

        // ATURAN BISNIS 3 & 4: update status laporan otomatis
        String statusBaru = Validasi.HASIL_VALID.equals(v.getHasilValidasi())
            ? Laporan.STATUS_DIPROSES
            : Laporan.STATUS_DITOLAK;
        laporanSvc.ubahStatus(v.getIdLaporan(), statusBaru);
    }

    @Override
    public Validasi cariById(Integer id) {
        return store.getValidasis().stream()
            .filter(v -> v.getIdValidasi() == id)
            .findFirst().orElse(null);
    }

    /** Cari validasi milik satu laporan tertentu */
    public Validasi cariByLaporan(int idLaporan) {
        return store.getValidasis().stream()
            .filter(v -> v.getIdLaporan() == idLaporan)
            .findFirst().orElse(null);
    }

    @Override
    public List<Validasi> cariSemua() {
        return new ArrayList<>(store.getValidasis());
    }

    @Override
    public boolean ubah(Validasi v) {
        List<Validasi> list = store.getValidasis();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdValidasi() == v.getIdValidasi()) {
                list.set(i, v); return true;
            }
        }
        return false;
    }

    @Override
    public boolean hapus(Integer id) {
        return store.getValidasis().removeIf(v -> v.getIdValidasi() == id);
    }
}
