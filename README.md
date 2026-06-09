# RiverR - Sistem Pelaporan Pencemaran Sungai

## Update Versi 2.0 - Fitur Laporan Darurat

Versi 2.0 menambahkan fitur **Laporan Darurat** sebagai kategori prioritas pada sistem
pelaporan pencemaran sungai RiverR. Fitur ini diimplementasikan melalui subclass
`LaporanDarurat` yang mewarisi class `Laporan`, dilengkapi atribut unik `jenisZatBerbahaya`
dan `estimasiVolumeLiter` beserta Constructor, Getter, Setter, dan Override method
`validate()` dan `toString()` sebagai bentuk Polimorfisme.

Pembaruan ini juga mencakup `LaporanDaruratService` untuk menyimpan objek darurat ke
ArrayList yang sama dengan Laporan biasa, fitur filter khusus laporan darurat, dan sorting
berdasarkan tingkat keparahan. Validasi input diamankan menggunakan Custom Exception
`InputTidakValidException` dengan blok try-catch sehingga aplikasi tidak crash saat input
kosong atau tipe data salah. Panel `LaporanDaruratPanel` terintegrasi langsung ke dashboard
masyarakat sebagai menu interaktif baru.

## Tentang Proyek

RiverR adalah aplikasi berbasis Java Swing untuk melaporkan pencemaran sungai secara digital.
Masyarakat dapat membuat laporan pencemaran, dan admin dapat memvalidasi serta menindaklanjuti
laporan yang masuk.

## Teknologi

- Java (OOP, Collections, Exception Handling)
- Java Swing (GUI)
- JUnit (Testing)
- Git & GitHub (Version Control)

## Cara Menjalankan

```bash
javac -cp src src/models/*.java src/interfaces/*.java src/exceptions/*.java src/services/*.java src/views/*.java src/Main.java
java -cp src Main
```
