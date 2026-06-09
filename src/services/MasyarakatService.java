package services;

import interfaces.IManageable;
import interfaces.IAuthenticatable;
import models.Masyarakat;
import models.Pengguna;

import java.util.ArrayList;
import java.util.List;

/**
 * CRUD Masyarakat + Login via email.
 *
 * KONSEP OOP:
 *  - implements dua interface sekaligus (multi-interface)
 *  - Stream API untuk filter ArrayList
 *  - Memanggil validate() dari model sebelum simpan
 *
 * @author Silfina (Role 2 - Data & Logic Engineer)
 */
public class MasyarakatService implements IManageable<Masyarakat, Integer>, IAuthenticatable {

    private final DataStore store = DataStore.getInstance();

    // CREATE
    @Override
    public void tambah(Masyarakat m) {
        m.validate(); // lempar IllegalArgumentException jika tidak valid

        // Cek duplikat email
        boolean duplikat = store.getMasyarakats().stream()
            .anyMatch(x -> x.getEmail().equalsIgnoreCase(m.getEmail()));
        if (duplikat)
            throw new IllegalArgumentException("Email sudah terdaftar.");

        m.setId(store.nextIdMasyarakat()); // assign ID otomatis
        store.getMasyarakats().add(m);
    }

    // READ
    @Override
    public Masyarakat cariById(Integer id) {
        return store.getMasyarakats().stream()
            .filter(m -> m.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Masyarakat> cariSemua() {
        // return COPY — caller tidak bisa modifikasi ArrayList asli
        return new ArrayList<>(store.getMasyarakats());
    }

    public Masyarakat cariByEmail(String email) {
        return store.getMasyarakats().stream()
            .filter(m -> m.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }

    // UPDATE
    @Override
    public boolean ubah(Masyarakat baru) {
        List<Masyarakat> list = store.getMasyarakats();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == baru.getId()) {
                list.set(i, baru); // ganti object di posisi yang sama
                return true;
            }
        }
        return false;
    }

    // DELETE
    @Override
    public boolean hapus(Integer id) {
        return store.getMasyarakats().removeIf(m -> m.getId() == id);
    }

    // IAuthenticatable — login pakai email
    @Override
    public Pengguna login(String email, String password) {
        Masyarakat m = cariByEmail(email);
        if (m != null && m.getPassword().equals(password) && isAktif(m))
            return m; // return Pengguna — polymorphism
        return null;
    }

    @Override
    public boolean isAktif(Pengguna p) {
        return "aktif".equals(p.getStatusAkun());
    }
}
