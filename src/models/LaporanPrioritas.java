package models;

import java.time.LocalDate;

public class LaporanPrioritas extends Laporan {

    // Atribut unik dengan Enkapsulasi (private)
    private int batasWaktuSla;

    // Constructor yang memanggil Superclass
    public LaporanPrioritas(int idLaporan, int idMasyarakat, int idTitikPantau, 
                            String jenisPencemaran, String tingkatPencemaran, 
                            String deskripsi, LocalDate tanggalLaporan, 
                            int batasWaktuSla) {
        super(idLaporan, idMasyarakat, idTitikPantau, jenisPencemaran, tingkatPencemaran, deskripsi, tanggalLaporan);
        this.batasWaktuSla = batasWaktuSla;
    }

    // Getter dan Setter
    public int getBatasWaktuSla() {
        return batasWaktuSla;
    }

    public void setBatasWaktuSla(int batasWaktuSla) {
        this.batasWaktuSla = batasWaktuSla;
    }

    // Override Method (Polimorfisme) - Membedakan output dari laporan biasa
    @Override
    public String getJenisPencemaran() {
        return "[URGENT] " + super.getJenisPencemaran() + " (SLA: " + batasWaktuSla + " jam)";
    }
}