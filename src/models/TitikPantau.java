package models;

import interfaces.IValidatable;

/**
 * @author [Bangkitscl] (Role 1 - Class Architect)
 */
public class TitikPantau implements IValidatable {

    private int idTitikPantau;
    private int idSungai;
    private double koordinatLat;
    private double koordinatLong;

    public TitikPantau() {
    }

    public TitikPantau(int idTitikPantau, int idSungai, double lat, double lng) {
        this.idTitikPantau = idTitikPantau;
        this.idSungai = idSungai;
        this.koordinatLat = lat;
        this.koordinatLong = lng;
    }

    public int getIdTitikPantau() {
        return idTitikPantau;
    }

    public void setIdTitikPantau(int id) {
        this.idTitikPantau = id;
    }

    public int getIdSungai() {
        return idSungai;
    }

    public void setIdSungai(int id) {
        this.idSungai = id;
    }

    public double getKoordinatLat() {
        return koordinatLat;
    }

    public void setKoordinatLat(double lat) {
        this.koordinatLat = lat;
    }

    public double getKoordinatLong() {
        return koordinatLong;
    }

    public void setKoordinatLong(double lng) {
        this.koordinatLong = lng;
    }

    @Override
    public boolean validate() throws IllegalArgumentException {
        if (idSungai <= 0) {
            throw new IllegalArgumentException("ID Sungai tidak valid.");
        }
        if (koordinatLat < -90.0 || koordinatLat > 90.0) {
            throw new IllegalArgumentException("Koordinat Latitude harus berada di antara -90 dan 90.");
        }
        if (koordinatLong < -180.0 || koordinatLong > 180.0) {
            throw new IllegalArgumentException("Koordinat Longitude harus berada di antara -180 dan 180.");
        }
        return true;
    }

    @Override
    public String toString() {
        return "TitikPantau{id=" + idTitikPantau
                + ", lat=" + koordinatLat + ", long=" + koordinatLong + "}";
    }
}