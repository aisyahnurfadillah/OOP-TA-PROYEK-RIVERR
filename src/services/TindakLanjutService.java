package services;

import interfaces.IManageable;
import models.Laporan;
import models.TindakLanjut;

import java.util.ArrayList;
import java.util.List;

/**
 * Service tindak lanjut — hanya bisa untuk laporan berstatus "diproses".
 *
 * ATURAN BISNIS:
 *  1. Laporan harus ada
 *  2. Status laporan harus "diproses" (sudah divalidasi valid)
 *  3. Setelah tersimpan → status laporan otomatis jadi "selesai"
 *
 * @author Silfina (Role 2 - Data & Logic Engineer)
 */
public class TindakLanjutService implements IManageable<TindakLanjut, Integer> {

    private final DataStore      store      = DataStore.getInstance();
    private final LaporanService laporanSvc = new LaporanService();

    @Override
    public void tambah(TindakLanjut t) {
        t.validate();

        // ATURAN BISNIS 1
        Laporan l = laporanSvc.cariById(t.getIdLaporan());
        if (l == null)
            throw new IllegalArgumentException("Laporan tidak ditemukan.");

        // ATURAN BISNIS 2
        if (!Laporan.STATUS_DIPROSES.equals(l.getStatusLaporan()))
            throw new IllegalArgumentException(
                "Laporan harus berstatus 'diproses'. Status saat ini: "
                + l.getStatusLaporan());

        t.setIdTindakLanjut(store.nextIdTindak());
        store.getTindakLanjuts().add(t);

        // ATURAN BISNIS 3 — tandai laporan selesai
        laporanSvc.ubahStatus(t.getIdLaporan(), Laporan.STATUS_SELESAI);
    }

    @Override
    public TindakLanjut cariById(Integer id) {
        return store.getTindakLanjuts().stream()
            .filter(t -> t.getIdTindakLanjut() == id)
            .findFirst().orElse(null);
    }

    @Override
    public List<TindakLanjut> cariSemua() {
        return new ArrayList<>(store.getTindakLanjuts());
    }

    @Override
    public boolean ubah(TindakLanjut t) {
        List<TindakLanjut> list = store.getTindakLanjuts();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdTindakLanjut() == t.getIdTindakLanjut()) {
                list.set(i, t); return true;
            }
        }
        return false;
    }

    @Override
    public boolean hapus(Integer id) {
        return store.getTindakLanjuts().removeIf(t -> t.getIdTindakLanjut() == id);
    }
}
