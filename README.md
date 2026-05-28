# 🌊 River - Sistem Pelaporan Pencemaran Sungai

Aplikasi manajemen dan pelaporan pencemaran sungai berbasis *Object-Oriented Programming* (OOP) menggunakan penyimpanan data *Java Collections Framework* di dalam memori (*in-memory*).

---

## 👥 Profil & Pembagian Peran Kelompok (Kelompok 7)

Berikut adalah rincian kontribusi, tanggung jawab, dan bukti pengerjaan objektif dari setiap anggota tim pengembang:

| NIM & Nama | Peran Utama | Rincian Tugas & Fokus | Bukti Objektif di Git |
| :--- | :--- | :--- | :--- |
| 254311027<br>Bangkit Cahya Linuwih | Role 1: Class Architect<br>*(Fokus: Struktur OOP)* | • Membuat Class Diagram di draw.io.<br>• Mendefinisikan *Superclass*, *Subclass*, *Interface* (IManageable, IAuthenticatable), dan *Abstract Class*.<br>• Memastikan implementasi *Encapsulation* (Getter/Setter) diterapkan dengan benar pada model. | Pembuat file model / entitas utama.<br>*(Contoh: Pengguna.java, Laporan.java, Masyarakat.java, Admin.java)* |
| 254311007<br>SILFINA NUR FADILAH | ⚙️ Role 2: Data & Logic Engineer<br>*(Fokus: Collections & CRUD)* | • Mengimplementasikan *ArrayList* atau *List* sebagai media penyimpanan data laporan dan pengguna di memori.<br>• Membuat fungsi logika *Create, Read, Update, Delete* (CRUD) untuk memanipulasi objek pelaporan. | Pembuat file Controller atau Service.<br>*(Contoh: PelaporanService.java, MasyarakatService.java)* |
| 254311015<br>ABDUL AZIZ MUSHTHOFA | 🛡️ Role 3: UI & Robustness Engineer<br>*(Fokus: Debugging & Form)* | • Merancang menu interaksi aplikasi CLI (Console / *Scanner*) untuk pelapor dan admin.<br>• Mengimplementasikan blok *try-catch* pada setiap input user.<br>• Melakukan validasi data input agar aplikasi tidak mudah *crash*. | Pembuat file View / Main Menu dan penanggung jawab blok *Exception Handling* (Main.java). |
| 254311001<br>AISYAH NUR FADILLAH | 🧪 Role 4: Quality Assurance & Repo Master<br>*(Fokus: Testing & Git)* | • Mengelola *merging branch* ke develop di Git dan menyelesaikan jika terjadi *code conflict*.<br>• Menulis skenario pengujian menggunakan *Unit Testing* (JUnit).<br>• Menyusun dokumentasi teknis dan konfigurasi akhir README.md. | Pembuat file Test *(Contoh: PelaporanTest.java)*, grafik aktivitas *merge* di GitHub, dan penulis utama README.md. |

---

## 📊 Class Diagram Sistem

Berikut adalah rancangan struktur kelas aplikasi River yang didesain menggunakan draw.io:

![Class Diagram River](docs/class-diagram.png)

---

## 🛠️ Arsitektur & Teknologi

* Bahasa Pemrograman: Java
* Paradigma: Object-Oriented Programming (OOP)
* Penyimpanan Data: Java Collections Framework (*List / ArrayList*)
* Pengujian: JUnit Testing
* Version Control: Git & GitHub

---