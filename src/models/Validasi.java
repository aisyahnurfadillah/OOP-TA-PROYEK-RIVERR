package models;

import interfaces.IValidatable;
import java.time.LocalDate;

/**
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */

public class Validasi implements IValidatable {

    public static final String HASIL_VALID = "valid";
    public static final String HASIL_TIDAK_VALID = "tidak_valid";

    private int idValidasi;
    private int idLaporan;
    private int idAdmin;
    private String hasilValidasi;
    private LocalDate tanggalValidasi;
    private String catatan;

    public Validasi() {
    }

    public Validasi(int idValidasi, int idLaporan, int idAdmin,
            String hasilValidasi, LocalDate tanggalValidasi, String catatan) {
        this.idValidasi = idValidasi;
        this.idLaporan = idLaporan;
        this.idAdmin = idAdmin;
        this.hasilValidasi = hasilValidasi;
        this.tanggalValidasi = tanggalValidasi;
        this.catatan = catatan;
    }

    public int getIdValidasi() {
        return idValidasi;
    }

    public void setIdValidasi(int id) {
        this.idValidasi = id;
    }

    public int getIdLaporan() {
        return idLaporan;
    }

    public void setIdLaporan(int id) {
        this.idLaporan = id;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int id) {
        this.idAdmin = id;
    }

    public String getHasilValidasi() {
        return hasilValidasi;
    }

    public void setHasilValidasi(String h) {
        this.hasilValidasi = h;
    }

    public LocalDate getTanggalValidasi() {
        return tanggalValidasi;
    }

    public void setTanggalValidasi(LocalDate d) {
        this.tanggalValidasi = d;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String c) {
        this.catatan = c;
    }

    @Override
    public boolean validate() throws IllegalArgumentException {
        if (idLaporan <= 0)
            throw new IllegalArgumentException("ID Laporan tidak valid.");
        if (hasilValidasi == null || hasilValidasi.isBlank())
            throw new IllegalArgumentException("Hasil validasi tidak boleh kosong.");
        return true;
    }
}