package models;

import interfaces.IValidatable;
import java.time.LocalDate;

/**
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */

public class TindakLanjut implements IValidatable {

    private int idTindakLanjut;
    private int idLaporan;
    private int idAdmin;
    private String detail;
    private LocalDate tanggal;

    public TindakLanjut() {
    }

    public TindakLanjut(int idTindakLanjut, int idLaporan,
            int idAdmin, String detail, LocalDate tanggal) {
        this.idTindakLanjut = idTindakLanjut;
        this.idLaporan = idLaporan;
        this.idAdmin = idAdmin;
        this.detail = detail;
        this.tanggal = tanggal;
    }

    public int getIdTindakLanjut() {
        return idTindakLanjut;
    }

    public void setIdTindakLanjut(int id) {
        this.idTindakLanjut = id;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String d) {
        this.detail = d;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate t) {
        this.tanggal = t;
    }

    @Override
    public boolean validate() throws IllegalArgumentException {
        if (idLaporan <= 0)
            throw new IllegalArgumentException("ID Laporan tidak valid.");
        if (detail == null || detail.isBlank())
            throw new IllegalArgumentException("Detail tindak lanjut tidak boleh kosong.");
        return true;
    }
}