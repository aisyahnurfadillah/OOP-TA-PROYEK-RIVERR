package models;

import interfaces.IValidatable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */

public class Laporan implements IValidatable {

    // Status lifecycle — gunakan konstanta ini, BUKAN string literal
    public static final String STATUS_MENUNGGU = "menunggu";
    public static final String STATUS_DIPROSES = "diproses";
    public static final String STATUS_DITOLAK = "ditolak";
    public static final String STATUS_SELESAI = "selesai";

    private int idLaporan;
    private int idMasyarakat;
    private int idTitikPantau;
    private String jenisPencemaran;
    private String tingkatPencemaran;
    private String deskripsi;
    private LocalDate tanggalLaporan;
    private String statusLaporan;

    public Laporan() {
    }

    public Laporan(int idLaporan, int idMasyarakat, int idTitikPantau,
            String jenisPencemaran, String tingkatPencemaran,
            String deskripsi, LocalDate tanggalLaporan) {
        this.idLaporan = idLaporan;
        this.idMasyarakat = idMasyarakat;
        this.idTitikPantau = idTitikPantau;
        this.jenisPencemaran = jenisPencemaran;
        this.tingkatPencemaran = tingkatPencemaran;
        this.deskripsi = deskripsi;
        this.tanggalLaporan = tanggalLaporan;
        this.statusLaporan = STATUS_MENUNGGU; // default otomatis
    }

    public int getIdLaporan() {
        return idLaporan;
    }

    public void setIdLaporan(int id) {
        this.idLaporan = id;
    }

    public int getIdMasyarakat() {
        return idMasyarakat;
    }

    public void setIdMasyarakat(int id) {
        this.idMasyarakat = id;
    }

    public int getIdTitikPantau() {
        return idTitikPantau;
    }

    public void setIdTitikPantau(int id) {
        this.idTitikPantau = id;
    }

    public String getJenisPencemaran() {
        return jenisPencemaran;
    }

    public void setJenisPencemaran(String j) {
        this.jenisPencemaran = j;
    }

    public String getTingkatPencemaran() {
        return tingkatPencemaran;
    }

    public void setTingkatPencemaran(String t) {
        this.tingkatPencemaran = t;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String d) {
        this.deskripsi = d;
    }

    public LocalDate getTanggalLaporan() {
        return tanggalLaporan;
    }

    public void setTanggalLaporan(LocalDate tgl) {
        this.tanggalLaporan = tgl;
    }

    public String getStatusLaporan() {
        return statusLaporan;
    }

    public void setStatusLaporan(String s) {
        this.statusLaporan = s;
    }

    @Override
    public boolean validate() throws IllegalArgumentException {
        if (idMasyarakat <= 0)
            throw new IllegalArgumentException("ID Masyarakat tidak valid.");
        if (jenisPencemaran == null || jenisPencemaran.isBlank())
            throw new IllegalArgumentException("Jenis pencemaran tidak boleh kosong.");
        if (deskripsi == null || deskripsi.isBlank())
            throw new IllegalArgumentException("Deskripsi tidak boleh kosong.");
        if (tanggalLaporan == null)
            throw new IllegalArgumentException("Tanggal laporan tidak boleh kosong.");
        return true;
    }

    @Override
    public String toString() {
        return String.format("[#%d] %s | %s | %s | %s",
                idLaporan, jenisPencemaran, tingkatPencemaran, statusLaporan,
                tanggalLaporan != null
                        ? tanggalLaporan.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "-");
    }
}