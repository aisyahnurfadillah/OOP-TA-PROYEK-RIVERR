package views;
import models.*;
import services.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * Panel Form Validasi Laporan oleh Admin.
 *
 * KONSEP OOP:
 *  - extends JPanel, implements Refreshable
 *  - static idLaporan : cara kirim data antar panel tanpa coupling langsung
 *  - Exception Handling: validasi sesi + tangkap business rule dari Service
 *
 * @author Stipen (Role 3 - UI & Robustness Engineer)
 */
public class ValidasiPanel extends JPanel implements Refreshable {

    private final MainFrame frame;

    // ID laporan yang dikirim dari DashboardAdminPanel
    private static int idLaporanTerpilih = -1;

    private final JLabel         lblInfo  = new JLabel();
    private final JComboBox<String> cmbHasil = new JComboBox<>(new String[]{
        Validasi.HASIL_VALID, Validasi.HASIL_TIDAK_VALID
    });
    private final JTextArea txtCatatan = new JTextArea(4, 30);
    private final JLabel    lblErr     = UIHelper.buatLabelError();

    /** Dipanggil DashboardAdminPanel sebelum pindah ke panel ini */
    public static void setIdLaporan(int id) { idLaporanTerpilih = id; }

    public ValidasiPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(28, 40, 28, 40));
        setBackground(UIHelper.ABU);
        buildUI();
    }

    private void buildUI() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 6, 8, 6);
        g.fill   = GridBagConstraints.HORIZONTAL;

        // Judul
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        add(UIHelper.buatJudul("✔ Form Validasi Laporan"), g);

        // Info laporan
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lblInfo.setForeground(Color.DARK_GRAY);
        g.gridy = 1;
        add(lblInfo, g);

        // Separator tipis
        JSeparator sep = new JSeparator();
        g.gridy = 2; g.insets = new Insets(2, 6, 10, 6);
        add(sep, g);

        // Hasil Validasi
        g.insets = new Insets(6, 6, 6, 6);
        g.gridy = 3; g.gridwidth = 1; g.gridx = 0;
        add(new JLabel("Hasil Validasi:"), g);
        g.gridx = 1; add(cmbHasil, g);

        // Catatan
        g.gridx = 0; g.gridy = 4;
        add(new JLabel("Catatan (opsional):"), g);
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        txtCatatan.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtCatatan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        g.gridx = 1; add(new JScrollPane(txtCatatan), g);

        // Error
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        g.insets = new Insets(2, 6, 2, 6);
        add(lblErr, g);

        // Tombol
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);
        JButton btnBatal = UIHelper.buatTombolOutline("Batal", Color.GRAY);
        btnBatal.addActionListener(e -> frame.showPanel(MainFrame.DASHBOARD_A));
        JButton btnSimpan = UIHelper.buatTombol("Simpan Validasi", UIHelper.BIRU);
        btnSimpan.addActionListener(e -> doSimpan());
        btns.add(btnBatal); btns.add(btnSimpan);
        g.gridy = 6; g.insets = new Insets(10, 6, 6, 6);
        add(btns, g);
    }

    private void doSimpan() {
        try {
            if (idLaporanTerpilih == -1)
                throw new IllegalStateException("Tidak ada laporan yang dipilih.");

            Admin admin = SessionManager.getAdmin();
            if (admin == null)
                throw new IllegalStateException("Sesi admin tidak valid. Silakan login ulang.");

            Validasi v = new Validasi(
                0,
                idLaporanTerpilih,
                admin.getId(),
                (String) cmbHasil.getSelectedItem(),
                LocalDate.now(),
                txtCatatan.getText().trim()
            );

            // Service akan cek business rules:
            // laporan harus ada & berstatus "menunggu"
            new ValidasiService().tambah(v);

            JOptionPane.showMessageDialog(this,
                "✅ Validasi berhasil disimpan.\nStatus laporan telah diperbarui.",
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            frame.showPanel(MainFrame.DASHBOARD_A);

        } catch (IllegalArgumentException | IllegalStateException ex) {
            lblErr.setText(ex.getMessage());
        } catch (Exception ex) {
            lblErr.setText("Error tidak terduga: " + ex.getMessage());
        }
    }

    @Override
    public void onShow() {
        txtCatatan.setText("");
        lblErr.setText(" ");
        cmbHasil.setSelectedIndex(0);
        if (idLaporanTerpilih != -1) {
            // Coba tampilkan info laporan
            try {
                Laporan l = new LaporanService().cariById(idLaporanTerpilih);
                if (l != null) {
                    lblInfo.setText("Laporan #" + l.getIdLaporan()
                        + "  |  " + l.getJenisPencemaran()
                        + "  |  " + l.getTingkatPencemaran()
                        + "  |  " + l.getTanggalLaporan());
                }
            } catch (Exception ex) {
                lblInfo.setText("Laporan ID: #" + idLaporanTerpilih);
            }
        } else {
            lblInfo.setText("Belum ada laporan dipilih.");
        }
    }
}
