package views;

import interfaces.InputTidakValidException;
import models.PetugasLapangan;
import services.PetugasService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PetugasPanel extends JPanel {

    private final PetugasService petugasService = new PetugasService();
    private MainFrame frame;

    private JTextField txtNama, txtPassword, txtSertifikat, txtFilterKeyword;
    private JButton btnTambah, btnFilter, btnReset;
    private JTable tablePetugas;
    private DefaultTableModel tableModel;

    // cunstrucktor
    public PetugasPanel(MainFrame frame) {
        this.frame = frame; // Simpan referensi dari MainFrame ke variabel global

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        loadTableData(petugasService.cariSemua());
    }

    private void initComponents() {
        // --- PANEL INPUT (FORM) ---
        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Form Pendaftaran Petugas Lapangan"));

        panelInput.add(new JLabel("Nama Petugas:"));
        txtNama = new JTextField();
        panelInput.add(txtNama);

        panelInput.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panelInput.add(txtPassword);

        panelInput.add(new JLabel("Nomor Sertifikat:"));
        txtSertifikat = new JTextField();
        panelInput.add(txtSertifikat);

        btnTambah = new JButton("Daftarkan Petugas");
        panelInput.add(new JLabel(""));
        panelInput.add(btnTambah);

        // --- PANEL FILTER (PENCARIAN) ---
        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFilter.setBorder(BorderFactory.createTitledBorder("Fitur Filter Koleksi (ArrayList)"));

        panelFilter.add(new JLabel("Cari No. Sertifikat:"));
        txtFilterKeyword = new JTextField(15);
        panelFilter.add(txtFilterKeyword);

        btnFilter = new JButton("Filter Data");
        btnReset = new JButton("Reset");
        panelFilter.add(btnFilter);
        panelFilter.add(btnReset);

        // <-- 3. TAMBAHKAN TOMBOL KEMBALI KE DASHBOARD DI SINI:
        JButton btnKembali = new JButton("⬅ Kembali");
        panelFilter.add(btnKembali);
        btnKembali.addActionListener(e -> frame.showPanel(MainFrame.DASHBOARD_A));

        // Gabungkan Input Form & Filter Panel
        JPanel panelAtas = new JPanel(new BorderLayout(5, 5));
        panelAtas.add(panelInput, BorderLayout.CENTER);
        panelAtas.add(panelFilter, BorderLayout.SOUTH);
        add(panelAtas, BorderLayout.NORTH);

        // --- PANEL TABEL DATA ---
        String[] columns = { "ID Pengguna", "Nama Petugas", "Tipe Akun", "No. Sertifikat" };
        tableModel = new DefaultTableModel(columns, 0);
        tablePetugas = new JTable(tableModel);
        add(new JScrollPane(tablePetugas), BorderLayout.CENTER);

        // EVENT LISTENER
        btnTambah.addActionListener((ActionEvent e) -> actionTambahPetugas());
        btnFilter.addActionListener((ActionEvent e) -> actionFilterPetugas());
        btnReset.addActionListener((ActionEvent e) -> loadTableData(petugasService.cariSemua()));
    }

    private void actionTambahPetugas() {
        try {
            String nama = txtNama.getText().trim();
            String password = txtPassword.getText().trim();
            String sertifikat = txtSertifikat.getText().trim();

            if (nama.isEmpty() || password.isEmpty()) {
                throw new InputTidakValidException("Kolom Nama dan Password wajib diisi!");
            }

            PetugasLapangan pl = new PetugasLapangan(0, nama, password, "aktif", sertifikat);
            pl.validate();

            petugasService.tambah(pl);

            loadTableData(petugasService.cariSemua());
            clearFields();
            JOptionPane.showMessageDialog(this, "Petugas Lapangan berhasil didaftarkan!", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (InputTidakValidException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Kesalahan Input UI", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Aturan Bisnis Dilanggar:\n" + ex.getMessage(), "Gagal Validasi Model",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionFilterPetugas() {
        String keyword = txtFilterKeyword.getText().trim();
        List<PetugasLapangan> hasilSaring = petugasService.filterPetugasBerdasarkanSertifikat(keyword);

        tableModel.setRowCount(0);
        for (PetugasLapangan pl : hasilSaring) {
            Object[] row = { pl.getId(), pl.getNama(), pl.getTipeAkun(), pl.getIdentifier() };
            tableModel.addRow(row);
        }

        if (hasilSaring.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan untuk keyword: " + keyword, "Info Filter",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadTableData(List<?> listData) {
        tableModel.setRowCount(0);
        for (Object obj : listData) {
            if (obj instanceof PetugasLapangan) {
                PetugasLapangan pl = (PetugasLapangan) obj;
                Object[] row = { pl.getId(), pl.getNama(), pl.getTipeAkun(), pl.getIdentifier() };
                tableModel.addRow(row);
            }
        }
    }

    private void clearFields() {
        txtNama.setText("");
        txtPassword.setText("");
        txtSertifikat.setText("");
    }
}