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

## 📊 Class Diagram & Arsitektur

Berikut adalah rancangan struktur kelas aplikasi Riverr yang didesain menggunakan draw.io:

![Class Diagram River](docs/class-diagram.png)

### Diagram Relasi Class
```text
«interface»          «abstract»
IValidatable ────► Pengguna ◄──── IAuthenticatable (via Service)
                      │
              extends │ extends
              ┌───────┴───────┐
              ▼               ▼
            Admin         Masyarakat
         (username)      (email, alamat)

DataStore (Singleton)
  ├── List<Admin>
  ├── List<Masyarakat>
  ├── List<Sungai>
  ├── List<TitikPantau>
  ├── List<Laporan>
  ├── List<Validasi>
  └── List<TindakLanjut>

Service (implements IManageable)
  ├── AdminService      → getAdmins()
  ├── MasyarakatService → getMasyarakats()
  ├── LaporanService    → getLaporans()
  ├── ValidasiService   → getValidasis() + LaporanService
  ├── TindakLanjutService → getTindakLanjuts() + LaporanService
  └── SungaiService     → getSungais() + getTitikPantaus()

View (CardLayout)
  MainFrame
    ├── LoginPanel        → AdminService + MasyarakatService
    ├── RegisterPanel     → MasyarakatService
    ├── DashboardMasyarakat → LaporanService + SessionManager
    ├── LaporanPanel      → LaporanService + SungaiService
    ├── DashboardAdmin    → LaporanService + SessionManager
    ├── ValidasiPanel     → ValidasiService + LaporanService
    └── TindakLanjutPanel → TindakLanjutService + LaporanService
🏛️ Konsep OOP yang Diterapkan
Abstract Class & Inheritance (Superclass ➔ Subclass)

Pengguna bertindak sebagai SUPERCLASS (abstract, tidak dapat di-instansiasi langsung).

Diturunkan ke SUBCLASS: Admin (login via username) dan Masyarakat (login via email).

Admin dan Masyarakat mewarisi field: id, nama, password, dan statusAkun dari Pengguna.

Interface

IManageable<T,ID>: Diimplementasikan pada semua Service sebagai kontrak CRUD generik.

IAuthenticatable: Diimplementasikan pada AdminService dan MasyarakatService sebagai kontrak sistem login.

IValidatable: Diimplementasikan di semua Model untuk kontrak validasi mandiri.

Refreshable: Diimplementasikan pada semua Panel View untuk kontrak pembaruan data UI secara otomatis.

Encapsulation

Semua fields pada kelas model diset sebagai private atau protected, dan hanya dapat diakses secara aman melalui metode Getter dan Setter.

Polymorphism

getIdentifier(): Mengembalikan username pada objek Admin, dan email pada objek Masyarakat.

getTipeAkun(): Mengembalikan nilai "Admin" atau "Masyarakat" sesuai tipe objek.

SessionManager.login(Pengguna p): Menerima argumen bertipe superclass Pengguna untuk menyimpan sesi aktif secara dinamis.

Collections (ArrayList)

Seluruh manajemen data disimpan sementara di dalam memori menggunakan objek ArrayList<T> yang dipusatkan pada DataStore.

Exception Handling

Semua aksi pengguna di layer View dibungkus dengan blok try-catch.

Layer Service bertugas melempar IllegalArgumentException apabila ditemukan input yang tidak valid atau melanggar aturan bisnis, kemudian ditangkap oleh View untuk ditampilkan dalam bentuk pesan error yang informatif.

Singleton Pattern

Kelas DataStore mengadopsi pola Singleton untuk menjamin hanya ada satu instance media penyimpanan di seluruh siklus hidup aplikasi agar data tetap konsisten.

📁 Struktur Folder
Plaintext
riverr-oop/
├── src/
│   ├── Main.java                    ← Entry point aplikasi
│   ├── interfaces/
│   │   ├── IManageable.java
│   │   ├── IAuthenticatable.java
│   │   └── IValidatable.java
│   ├── models/
│   │   ├── Pengguna.java            ← Abstract superclass
│   │   ├── Admin.java               ← Subclass Pengguna
│   │   ├── Masyarakat.java          ← Subclass Pengguna
│   │   ├── Laporan.java
│   │   ├── Validasi.java
│   │   ├── TindakLanjut.java
│   │   ├── Sungai.java
│   │   └── TitikPantau.java
│   ├── services/
│   │   ├── DataStore.java           ← Singleton + 7 ArrayList
│   │   ├── SessionManager.java
│   │   ├── MasyarakatService.java
│   │   ├── AdminService.java
│   │   ├── LaporanService.java
│   │   ├── ValidasiService.java
│   │   ├── TindakLanjutService.java
│   │   └── SungaiService.java
│   ├── view/
│   │   ├── Refreshable.java
│   │   ├── UIHelper.java
│   │   ├── MainFrame.java
│   │   ├── LoginPanel.java
│   │   ├── RegisterPanel.java
│   │   ├── DashboardMasyarakatPanel.java
│   │   ├── LaporanPanel.java
│   │   ├── DashboardAdminPanel.java
│   │   ├── ValidasiPanel.java
│   │   └── TindakLanjutPanel.java
│   └── test/
│       ├── PenggunaModelTest.java   ← 18 test case
│       ├── LaporanServiceTest.java  ← 25 test case
│       └── MasyarakatServiceTest.java ← 17 test case
├── docs/
│   ├── class-diagram.png
│   ├── TAHAP_1_ROLE1_CLASS_ARCHITECT.md
│   ├── TAHAP_2_ROLE2_DATA_ENGINEER.md
│   ├── TAHAP_3_ROLE3_UI_ENGINEER.md
│   └── TAHAP_4_ROLE4_QA_REPO_MASTER.md
└── lib/
    └── junit-platform-console-standalone-1.10.x.jar
🔄 Alur Status Laporan
Plaintext
[Masyarakat buat laporan]
         │
         ▼
   ┌─ MENUNGGU ─┐
   │            │
   ▼ (Valid)    ▼ (Tidak Valid)
DIPROSES     DITOLAK
   │
   ▼ (Tindak Lanjut)
SELESAI
🛠️ Arsitektur & Teknologi
Bahasa Pemrograman: Java (JDK 17 atau lebih baru)

Antarmuka Grafis: Java Swing (GUI)

Paradigma: Object-Oriented Programming (OOP)

Penyimpanan Data: Java Collections Framework (List / ArrayList - In-Memory)

Pengujian: JUnit Testing v5

Version Control: Git & GitHub

🚀 Cara Menjalankan Aplikasi
Langkah Menggunakan IDE / Text Editor
Klon repositori ini:

Bash
git clone [https://github.com/USERNAME/riverr-oop.git](https://github.com/USERNAME/riverr-oop.git)
Buka proyek melalui IDE pilihan Anda.

Atur source root ke direktori folder src/.

Klik kanan pada file src/Main.java ➔ Pilih Run 'Main.main()'.

Langkah Menggunakan Terminal
Bash
cd src
javac -cp . Main.java interfaces/*.java models/*.java services/*.java view/*.java
java Main
Akun Bawaan (Default Accounts)
Aplikasi menyediakan akun bawaan yang siap digunakan saat pertama kali dijalankan:

Role	Identifier	Password
Admin	admin	admin123
Masyarakat	budi@email.com	budi123
🧪 Cara Menjalankan Unit Test
Prasyarat Pengujian
Pastikan Anda memiliki JUnit 5 standalone JAR di dalam folder lib/:

lib/junit-platform-console-standalone-1.10.x.jar

Jika belum tersedia, unduh melalui Maven Central Repository.

Eksekusi via IDE / Text Editor
VS Code (Visual Studio Code):

Pastikan ekstensi Extension Pack for Java dan Test Runner for Java sudah terinstal.

Buka bagian Java Projects di sidebar kiri bawah panel Explorer.

Cari submenu Referenced Libraries, klik tanda + (plus), lalu masukkan file JAR dari folder lib/.

Buka file pengujian di folder test/ (misal: PenggunaModelTest.java), lalu klik tombol Run Test yang muncul di atas deklarasi class atau method.

IntelliJ IDEA: Masuk ke File ➔ Project Structure ➔ Libraries ➔ klik + (Java). Pilih berkas JAR dari folder lib/. Jika sudah, klik kanan pada folder test/ ➔ Run Tests.

NetBeans: Klik kanan pada proyek ➔ Properties ➔ Libraries ➔ Pilih Add JAR/Folder. Cari berkas JAR di folder lib/. Jika sudah, klik kanan folder test/ ➔ Test Package.

Eksekusi via Terminal
Bash
cd src
javac -cp ../lib/junit-platform-console-standalone-1.10.x.jar interfaces/*.java models/*.java services/*.java test/*.java
java -jar ../lib/junit-platform-console-standalone-1.10.x.jar --class-path . --select-package test
Ringkasan Test Cases
File Test	Jumlah Test	Komponen yang Diuji
PenggunaModelTest	18	Inheritance, Polymorphism, Encapsulation, Validasi
LaporanServiceTest	25	CRUD Laporan, Lifecycle Status, Aturan Bisnis (Business Rules)
MasyarakatServiceTest	17	CRUD Masyarakat, Login (4 Skenario berbeda), Copy protection
Total Kasus Uji	60	Stabil & Lolos Pengujian
📝 Catatan Teknis
⚠️ In-Memory Storage: Data bersifat sementara (in-memory). Semua modifikasi data akan di-reset kembali seperti semula setiap kali aplikasi dijalankan ulang.

DataStore menggunakan pola Singleton untuk menjamin seluruh instance Service mengakses data yang sinkron dan seragam.

SessionManager memanfaatkan static fields untuk melacak status autentikasi pengguna secara global.

Seluruh bentuk eksepsi (Exception) yang dilempar oleh layer Service ditangani dengan baik oleh layer View menggunakan pendekatan try-catch bertingkat untuk menjaga pengalaman pengguna.
