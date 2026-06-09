package models;

/*
    penambahan class Petugaslapangan 
 */

public class PetugasLapangan extends Pengguna {
    private String nomorSertifikat; // field eksklusif

    public PetugasLapangan() {
    }

    public PetugasLapangan(int id, String nama, String password, String statusAkun, String nomorSertifikat) {
        super(id, nama, password, statusAkun);
        this.nomorSertifikat = nomorSertifikat;
    }

    @Override
    public String getIdentifier() {
        return nomorSertifikat;
    }

    @Override
    public String getTipeAkun() {
        return "Petugas Lapangan";
    }

    public String getNomorSertifikat() {
        return nomorSertifikat;
    }

    public void setNomorSertifikat(String nomorSertifikat) {
        this.nomorSertifikat = nomorSertifikat;
    }

    @Override
    public boolean validate() throws IllegalArgumentException {
        super.validate(); // cek nama & password dari Pengguna dulu
        if (nomorSertifikat == null || nomorSertifikat.isBlank())
            throw new IllegalArgumentException("Nomor sertifikat tidak boleh kosong.");
        return true;
    }
}