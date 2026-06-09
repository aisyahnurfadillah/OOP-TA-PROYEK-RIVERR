package models;

import interfaces.IValidatable;

/**
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */

public class Sungai implements IValidatable {

    private int idSungai;
    private String nama;
    private String wilayah;

    public Sungai() {
    }

    public Sungai(int idSungai, String nama, String wilayah) {
        this.idSungai = idSungai;
        this.nama = nama;
        this.wilayah = wilayah;
    }

    public int getIdSungai() {
        return idSungai;
    }

    public void setIdSungai(int id) {
        this.idSungai = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String n) {
        this.nama = n;
    }

    public String getWilayah() {
        return wilayah;
    }

    public void setWilayah(String w) {
        this.wilayah = w;
    }

    @Override
    public boolean validate() throws IllegalArgumentException {
        if (nama == null || nama.isBlank())
            throw new IllegalArgumentException("Nama sungai tidak boleh kosong.");
        if (wilayah == null || wilayah.isBlank())
            throw new IllegalArgumentException("Wilayah tidak boleh kosong.");
        return true;
    }

    @Override
    public String toString() {
        return "Sungai{id=" + idSungai + ", nama=" + nama + ", wilayah=" + wilayah + "}";
    }
}