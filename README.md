# 🌊 RiverR – Fitur Manajemen & Validasi Petugas Lapangan (Modul UAS)

Dokumentasi ini disusun untuk memenuhi kriteria penilaian Ujian Akhir Semester (UAS) pada mata kuliah Pemrograman Berorientasi Objek (OOP). Modul ini menambahkan fitur manajemen **Petugas Lapangan** ke dalam sistem pelaporan pencemaran sungai RiverR berbasis Java Swing GUI dan Maven.

---

## 🎯 Target Kelulusan & Capaian Pembelajaran (CPMK)

Modul tambahan ini dirancang secara khusus untuk memenuhi kriteria penilaian berikut:

### 1. Struktur Data & Logika (Collections) – Target: SCPMK0721601 (15%)

- **Polimorfisme Koleksi:** Objek bertipe subclass (`PetugasLapangan`) disimpan secara seragam ke dalam `List<Pengguna>` (Superclass) yang dikelola oleh objek Singleton `DataStore`.
- **Fitur Saringan (Filtering):** Mengimplementasikan pencarian data dinamis pada `ArrayList` menggunakan **Stream API** untuk memfilter petugas berdasarkan kesesuaian Nomor Sertifikat.

### 2. Antarmuka Sederhana & Jaringan Pengaman – Target: SCPMK0721603 (15%)

- **Graphical User Interface (GUI):** Menyediakan form input terisolasi (`PetugasPanel`) berbasis Java Swing yang terintegrasi ke dalam sistem navigasi `CardLayout` pada `MainFrame`.
- **Robustness (Exception Handling):** Menerapkan mekanisme dua lapis _Exception Handling_ menggunakan _Custom Checked Exception_ (`InputTidakValidException`) pada layer UI dan `IllegalArgumentException` pada layer model untuk mencegah aplikasi mengalami _force close_ (crash) akibat galat input runtime.

---

## 🛠️ Struktur Berkas Baru & Modifikasi

Berikut adalah komponen berkas yang telah dikonfigurasi ke dalam arsitektur RiverR:

```text
src/
├── interfaces/
│   └── InputTidakValidException.java   <-- [BARU] Custom Checked Exception untuk UI
├── models/
│   └── PetugasLapangan.java            <-- Subclass implementer IValidatable
├── services/
│   ├── DataStore.java                  <-- [MODIFIKASI] Penambahan koleksi List superclass
│   └── PetugasService.java             <-- [BARU] Service dengan implementasi Stream API Filter
└── views/
    ├── MainFrame.java                  <-- [MODIFIKASI] Registrasi slide card PETUGAS_UAS
    ├── DashboardAdminPanel.java        <-- [MODIFIKASI] Penambahan tombol navigasi manajemen
    └── PetugasPanel.java               <-- [BARU] Panel form input & tabel data Java Swing
```
