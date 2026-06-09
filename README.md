# 🌊 Riverr - Sistem Pelaporan Pencemaran Sungai

## 🆕 Update Versi 2.0 — oleh Silfina Nur Fadilah (254311007)

Versi ini menambahkan fitur **Laporan Urgent** sebagai penanganan laporan pencemaran yang bersifat darurat dan membutuhkan penanganan prioritas tinggi. Fitur ini diimplementasikan melalui subclass `LaporanUrgent` yang mewarisi class `Laporan`, dilengkapi atribut `tingkatUrgensi` (TINGGI/KRITIS) dan override method `validate()` serta `toString()` sehingga output laporan urgent berbeda dari laporan biasa.

Selain itu, ditambahkan method `cariLaporanUrgent()` di `LaporanService` untuk memfilter laporan urgent dari ArrayList, serta `InputTidakValidException` sebagai custom exception untuk validasi input pada form laporan urgent agar aplikasi tidak crash saat pengguna memasukkan data yang tidak valid.

---