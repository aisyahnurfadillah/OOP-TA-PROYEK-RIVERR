package models;

import java.time.LocalDate;

/**
 * BAGIAN A - Subclass LaporanDarurat extends Laporan
 * @author Aisyah Nur Fadillah (254311001) - Role 4: QA & Repo Master
 */
public class LaporanDarurat extends Laporan {

    private String jenisZatBerbahaya;
    private double estimasiVolumeLiter;
    private boolean sudahDilaporkanKeDinas;

    public LaporanDarurat(int idLaporan, int idMasyarakat, int idTitikPantau,
                          String jenisPencemaran, String tingkatPencemaran,
                          String deskripsi, LocalDate tanggalLaporan,
                          String jenisZatBerbahaya, double estimasiVolumeLiter) {
        super(idLaporan, idMasyarakat, idTitikPantau,
              jenisPencemaran, tingkatPencemaran, deskripsi, tanggalLaporan);
        this.jenisZatBerbahaya = jenisZatBerbahaya;
        this.estimasiVolumeLiter = estimasiVolumeLiter;
        this.sudahDilaporkanKeDinas = false;
    }

    public String getJenisZatBerbahaya() { return jenisZatBerbahaya; }
    public void setJenisZatBerbahaya(String j) { this.jenisZatBerbahaya = j; }

    public double getEstimasiVolumeLiter() { return estimasiVolumeLiter; }
    public void setEstimasiVolumeLiter(double v) { this.estimasiVolumeLiter = v; }

    public boolean isSudahDilaporkanKeDinas() { return sudahDilaporkanKeDinas; }
    public void setSudahDilaporkanKeDinas(boolean s) { this.sudahDilaporkanKeDinas = s; }

    @Override
    public boolean validate() throws IllegalArgumentException {
        super.validate();
        if (jenisZatBerbahaya == null || jenisZatBerbahaya.isBlank())
            throw new IllegalArgumentException("Jenis zat berbahaya tidak boleh kosong.");
        if (estimasiVolumeLiter <= 0)
            throw new IllegalArgumentException("Estimasi volume harus lebih dari 0 liter.");
        return true;
    }

    @Override
    public String toString() {
        return String.format("[DARURAT #%d] %s | Zat: %s | Vol: %.1f L | %s | %s",
                getIdLaporan(), getJenisPencemaran(),
                jenisZatBerbahaya, estimasiVolumeLiter,
                getStatusLaporan(),
                sudahDilaporkanKeDinas ? "Sudah lapor dinas" : "BELUM lapor dinas");
    }
}
