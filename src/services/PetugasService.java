package services;

import models.PetugasLapangan;
import models.Pengguna;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Sederhana PetugasLapangan untuk Memenuhi Kriteria Kumpulan
 * (Collections) UAS.
 * Target Ujian: SCPMK0721601 (Polimorfisme Koleksi & Stream API Filter)
 */
public class PetugasService {

    private final DataStore store = DataStore.getInstance();

    public void tambah(PetugasLapangan pl) {
        pl.validate();

        pl.setId(store.nextIdPengguna());
        store.getPenggunas().add(pl);
    }

    public List<PetugasLapangan> cariSemua() {
        return store.getPenggunas().stream()
                .filter(p -> p instanceof PetugasLapangan)
                .map(p -> (PetugasLapangan) p)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<PetugasLapangan> filterPetugasBerdasarkanSertifikat(String keyword) {
        return store.getPenggunas().stream()
                .filter(p -> p instanceof PetugasLapangan)
                .map(p -> (PetugasLapangan) p)
                .filter(pl -> pl.getNomorSertifikat() != null
                        && pl.getNomorSertifikat().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}