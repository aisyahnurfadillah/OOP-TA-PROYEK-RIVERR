package interfaces;

import java.util.List;

/**
 * Kontrak CRUD generik berbasis Collections (in-memory).
 * Diimplementasi oleh semua Service class (dikerjakan Role 2 - Silpik).
 *
 * KONSEP OOP: Interface, Generics
 *
 * @param <T>  tipe entitas — misal Laporan, Masyarakat
 * @param <ID> tipe primary key — misal Integer
 *
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */
public interface IManageable<T, ID> {
    void tambah(T entity);

    T cariById(ID id);

    List<T> cariSemua();

    boolean ubah(T entity);

    boolean hapus(ID id);
}