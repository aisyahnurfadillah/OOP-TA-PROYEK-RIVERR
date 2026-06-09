package services;

import interfaces.IManageable;
import interfaces.IAuthenticatable;
import models.Admin;
import models.Pengguna;

import java.util.ArrayList;
import java.util.List;

/**
 * CRUD Admin + Login via username.
 * Pola identik dengan MasyarakatService, beda di field identifier.
 *
 * @author Silfina (Role 2 - Data & Logic Engineer)
 */
public class AdminService implements IManageable<Admin, Integer>, IAuthenticatable {

    private final DataStore store = DataStore.getInstance();

    @Override
    public void tambah(Admin a) {
        a.validate();
        boolean dup = store.getAdmins().stream()
            .anyMatch(x -> x.getUsername().equalsIgnoreCase(a.getUsername()));
        if (dup) throw new IllegalArgumentException("Username sudah dipakai.");
        a.setId(store.nextIdAdmin());
        store.getAdmins().add(a);
    }

    @Override
    public Admin cariById(Integer id) {
        return store.getAdmins().stream()
            .filter(a -> a.getId() == id)
            .findFirst().orElse(null);
    }

    @Override
    public List<Admin> cariSemua() {
        return new ArrayList<>(store.getAdmins());
    }

    public Admin cariByUsername(String username) {
        return store.getAdmins().stream()
            .filter(a -> a.getUsername().equalsIgnoreCase(username))
            .findFirst().orElse(null);
    }

    @Override
    public boolean ubah(Admin baru) {
        List<Admin> list = store.getAdmins();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == baru.getId()) {
                list.set(i, baru); return true;
            }
        }
        return false;
    }

    @Override
    public boolean hapus(Integer id) {
        return store.getAdmins().removeIf(a -> a.getId() == id);
    }

    // Login pakai username — beda dari Masyarakat yang pakai email
    @Override
    public Pengguna login(String username, String password) {
        Admin a = cariByUsername(username);
        if (a != null && a.getPassword().equals(password) && isAktif(a))
            return a;
        return null;
    }

    @Override
    public boolean isAktif(Pengguna p) {
        return "aktif".equals(p.getStatusAkun());
    }
}

