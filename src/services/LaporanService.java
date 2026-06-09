package services;

import interfaces.IManageable;
import models.Laporan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exceptions.InputTidakValidException;
import models.LaporanUrgent;
/**
 * CRUD Laporan dengan filter dan perubahan status lifecycle.
 *
 * ALUR STATUS LAPORAN:
 *   menunggu ──(validasi valid)──► diproses ──(tindak lanjut)──► selesai
 *   menunggu ──(validasi tidak_valid)──► ditolak
 *
 * @author Silfina (Role 2 - Data & Logic Engineer)
 */
public class LaporanService implements IManageable<Laporan, Integer> {

    private final DataStore store = DataStore.getInstance();

    @Override
    public void tambah(Laporan l) {
        l.validate();
        l.setIdLaporan(store.nextIdLaporan());
        l.setStatusLaporan(Laporan.STATUS_MENUNGGU); // status awal selalu menunggu
        l.setTanggalLaporan(LocalDate.now());         // tanggal diisi otomatis
        store.getLaporans().add(l);
    }

    @Override
    public Laporan cariById(Integer id) {
        return store.getLaporans().stream()
            .filter(l -> l.getIdLaporan() == id)
            .findFirst().orElse(null);
    }

    @Override
    public List<Laporan> cariSemua() {
        return new ArrayList<>(store.getLaporans());
    }

    /** Hanya laporan milik satu masyarakat — untuk dashboard masyarakat */
    public List<Laporan> cariByMasyarakat(int idMasyarakat) {
        return store.getLaporans().stream()
            .filter(l -> l.getIdMasyarakat() == idMasyarakat)
            .collect(Collectors.toList());
    }

    /** Filter berdasarkan status — untuk tab admin */
    public List<Laporan> cariByStatus(String status) {
        return store.getLaporans().stream()
            .filter(l -> l.getStatusLaporan().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }

    @Override
    public boolean ubah(Laporan baru) {
        List<Laporan> list = store.getLaporans();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdLaporan() == baru.getIdLaporan()) {
                list.set(i, baru); return true;
            }
        }
        return false;
    }

    @Override
    public boolean hapus(Integer id) {
        return store.getLaporans().removeIf(l -> l.getIdLaporan() == id);
    }

    /**
     * Ubah status laporan.
     * HANYA dipanggil oleh ValidasiService dan TindakLanjutService.
     * View tidak boleh memanggil method ini langsung.
     */
    public boolean ubahStatus(int idLaporan, String statusBaru) {
        Laporan l = cariById(idLaporan);
        if (l == null) return false;
        l.setStatusLaporan(statusBaru);
        return true;
    }
    /** Filter — hanya LaporanUrgent. Bagian B: Collections. */
    public List<Laporan> cariLaporanUrgent() {
        return store.getLaporans().stream()
            .filter(l -> l instanceof LaporanUrgent)
            .collect(Collectors.toList());
    }

    /** Tambah laporan urgent dengan Custom Exception. Bagian C. */
    public void tambahUrgent(LaporanUrgent lu) throws InputTidakValidException {
        try {
            lu.validate();
            lu.setIdLaporan(store.nextIdLaporan());
            lu.setStatusLaporan(Laporan.STATUS_MENUNGGU);
            lu.setTanggalLaporan(java.time.LocalDate.now());
            store.getLaporans().add(lu);
        } catch (IllegalArgumentException e) {
            throw new InputTidakValidException(e.getMessage());
        }
    }
}
