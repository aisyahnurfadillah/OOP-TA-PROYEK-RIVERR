package services;

import interfaces.IManageable;
import interfaces.IAuthenticatable;
import models.PetugasLapangan;
import models.Pengguna;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PetugasService implements IManageable<PetugasLapangan, Integer>, IAuthenticatable {

    private final DataStore store = DataStore.getInstance();

    @Override
    public void tambah(PetugasLapangan pl) {
        pl.validate(); // validasi internal bawaan dari class PetugasLapangan Anda

        boolean dup = store.getPenggunas().stream()
                .filter(x -> x instanceof PetugasLapangan)
                .map(x -> (PetugasLapangan) x)
                .anyMatch(x -> x.getNomorSertifikat().equalsIgnoreCase(pl.getNomorSertifikat()));

        if (dup)
            throw new IllegalArgumentException("Nomor sertifikat sudah terdaftar.");

        pl.setId(store.nextIdPengguna());
        store.getPenggunas().add(pl);
    }

    @Override
    public PetugasLapangan cariById(Integer id) {
        return store.getPenggunas().stream()
                .filter(p -> p instanceof PetugasLapangan && p.getId() == id)
                .map(p -> (PetugasLapangan) p)
                .findFirst().orElse(null);
    }

    @Override
    public List<PetugasLapangan> cariSemua() {
        return store.getPenggunas().stream()
                .filter(p -> p instanceof PetugasLapangan)
                .map(p -> (PetugasLapangan) p)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // filter
    public List<PetugasLapangan> filterPetugasBerdasarkanSertifikat(String keyword) {
        return store.getPenggunas().stream()
                .filter(p -> p instanceof PetugasLapangan)
                .map(p -> (PetugasLapangan) p)
                .filter(pl -> pl.getNomorSertifikat() != null && pl.getNomorSertifikat().contains(keyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public PetugasLapangan cariByNomorSertifikat(String nomorSertifikat) {
        return store.getPenggunas().stream()
                .filter(p -> p instanceof PetugasLapangan)
                .map(p -> (PetugasLapangan) p)
                .filter(pl -> pl.getNomorSertifikat().equalsIgnoreCase(nomorSertifikat))
                .findFirst().orElse(null);
    }

    @Override
    public boolean ubah(PetugasLapangan baru) {
        List<Pengguna> list = store.getPenggunas();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == baru.getId()) {
                list.set(i, baru);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hapus(Integer id) {
        return store.getPenggunas().removeIf(p -> p instanceof PetugasLapangan && p.getId() == id);
    }

    // Login menggunakan nomor sertifikat (Identifier unik milik Petugas Lapangan)
    @Override
    public Pengguna login(String nomorSertifikat, String password) {
        PetugasLapangan pl = cariByNomorSertifikat(nomorSertifikat);
        if (pl != null && pl.getPassword().equals(password) && isAktif(pl))
            return pl;
        return null;
    }

    @Override
    public boolean isAktif(Pengguna p) {
        return "aktif".equals(p.getStatusAkun());
    }
}