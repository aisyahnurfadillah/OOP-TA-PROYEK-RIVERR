package services;

import exceptions.InputTidakValidException;
import models.Laporan;
import models.LaporanDarurat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * BAGIAN B - Filter dan Sort LaporanDarurat
 * @author Aisyah Nur Fadillah (254311001) - Role 4: QA & Repo Master
 */
public class LaporanDaruratService {

    private final DataStore store = DataStore.getInstance();

    public void tambahLaporanDarurat(int idMasyarakat, int idTitikPantau,
                                      String jenisPencemaran, String tingkat,
                                      String deskripsi, String jenisZat,
                                      double volume)
            throws InputTidakValidException {

        validasiInput(jenisPencemaran, deskripsi, jenisZat, volume);

        int id = store.nextIdLaporan();
        LaporanDarurat laporan = new LaporanDarurat(
                id, idMasyarakat, idTitikPantau,
                jenisPencemaran, tingkat, deskripsi,
                LocalDate.now(), jenisZat, volume);

        store.getLaporans().add(laporan);
    }

    public List<LaporanDarurat> filterLaporanDarurat() {
        List<LaporanDarurat> hasil = new ArrayList<>();
        for (Laporan l : store.getLaporans()) {
            if (l instanceof LaporanDarurat) {
                hasil.add((LaporanDarurat) l);
            }
        }
        return hasil;
    }

    public List<Laporan> sortByTingkatKeparahan() {
        List<Laporan> sorted = new ArrayList<>(store.getLaporans());
        sorted.sort(Comparator.comparingInt(l -> urutTingkat(l.getTingkatPencemaran())));
        return sorted;
    }

    private int urutTingkat(String tingkat) {
        if (tingkat == null) return 99;
        switch (tingkat.toLowerCase()) {
            case "kritis": return 0;
            case "tinggi": return 1;
            case "sedang": return 2;
            case "rendah": return 3;
            default:       return 99;
        }
    }

    public void validasiInput(String jenisPencemaran, String deskripsi,
                               String jenisZat, double volume)
            throws InputTidakValidException {

        if (jenisPencemaran == null || jenisPencemaran.isBlank())
            throw new InputTidakValidException("Jenis Pencemaran", "tidak boleh kosong!");
        if (deskripsi == null || deskripsi.isBlank())
            throw new InputTidakValidException("Deskripsi", "tidak boleh kosong!");
        if (jenisZat == null || jenisZat.isBlank())
            throw new InputTidakValidException("Jenis Zat Berbahaya", "tidak boleh kosong!");
        if (volume <= 0)
            throw new InputTidakValidException("Estimasi Volume",
                    "harus lebih dari 0 liter. Nilai: " + volume);
    }
}
