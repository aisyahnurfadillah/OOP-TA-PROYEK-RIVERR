package services;

import interfaces.IManageable;
import models.Sungai;
import models.TitikPantau;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CRUD Sungai + pengelolaan TitikPantau.
 * TitikPantau dikelola lewat SungaiService karena selalu berkaitan dengan Sungai.
 *
 * @author Silfina (Role 2 - Data & Logic Engineer)
 */
public class SungaiService implements IManageable<Sungai, Integer> {

    private final DataStore store = DataStore.getInstance();

    @Override
    public void tambah(Sungai s) {
        s.validate();
        s.setIdSungai(store.nextIdSungai());
        store.getSungais().add(s);
    }

    @Override
    public Sungai cariById(Integer id) {
        return store.getSungais().stream()
            .filter(s -> s.getIdSungai() == id)
            .findFirst().orElse(null);
    }

    @Override
    public List<Sungai> cariSemua() {
        return new ArrayList<>(store.getSungais());
    }

    @Override
    public boolean ubah(Sungai baru) {
        List<Sungai> list = store.getSungais();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdSungai() == baru.getIdSungai()) {
                list.set(i, baru); return true;
            }
        }
        return false;
    }

    @Override
    public boolean hapus(Integer id) {
        return store.getSungais().removeIf(s -> s.getIdSungai() == id);
    }

    /** Ambil semua titik pantau milik satu sungai */
    public List<TitikPantau> getTitikBySungai(int idSungai) {
        return store.getTitikPantaus().stream()
            .filter(t -> t.getIdSungai() == idSungai)
            .collect(Collectors.toList());
    }

    /** Tambah titik pantau baru */
    public void tambahTitik(TitikPantau t) {
        t.validate();
        t.setIdTitikPantau(store.nextIdTitik());
        store.getTitikPantaus().add(t);
    }
}
