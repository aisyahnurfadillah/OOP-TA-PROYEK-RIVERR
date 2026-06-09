package views;
import models.*;
import services.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * Panel Form Tindak Lanjut oleh Admin.
 * Hanya bisa dipakai untuk laporan berstatus "diproses".
 *
 * KONSEP OOP:
 *  - extends JPanel, implements Refreshable
 *  - static idLaporan : kirim data antar panel
 *  - Exception Handling: tangkap business rule dari TindakLanjutService
 *
 * @author Stipen (Role 3 - UI & Robustness Engineer)
 */
public class TindakLanjutPanel extends JPanel implements Refreshable {

    private final MainFrame frame;

    private static int idLaporanTerpilih = -1;

    private final JLabel    lblInfo  = new JLabel();
    private final JTextArea txtDetail = new JTextArea(5, 30);
    private final JLabel    lblErr   = UIHelper.buatLabelError();

    public static void setIdLaporan(int id) { idLaporanTerpilih = id; }

    public TindakLanjutPanel(MainFrame frame) {
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
        JLabel judul = UIHelper.buatJudul("⚡ Form Tindak Lanjut");
        judul.setForeground(UIHelper.HIJAU);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        add(judul, g);

        // Info laporan
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lblInfo.setForeground(Color.DARK_GRAY);
        g.gridy = 1; add(lblInfo, g);

        JSeparator sep = new JSeparator();
        g.gridy = 2; g.insets = new Insets(2, 6, 10, 6);
        add(sep, g);

        // Detail tindakan
        g.insets = new Insets(6, 6, 6, 6);
        g.gridy = 3; g.gridwidth = 1; g.gridx = 0;
        add(new JLabel("Detail Tindakan:"), g);
        txtDetail.setLineWrap(true);
        txtDetail.setWrapStyleWord(true);
        txtDetail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDetail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        g.gridx = 1; add(new JScrollPane(txtDetail), g);

        // Error
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        g.insets = new Insets(2, 6, 2, 6);
        add(lblErr, g);

        // Tombol
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);
        JButton btnBatal = UIHelper.buatTombolOutline("Batal", Color.GRAY);
        btnBatal.addActionListener(e -> frame.showPanel(MainFrame.DASHBOARD_A));
        JButton btnSimpan = UIHelper.buatTombol("Simpan Tindak Lanjut", UIHelper.HIJAU);
        btnSimpan.addActionListener(e -> doSimpan());
        btns.add(btnBatal); btns.add(btnSimpan);
        g.gridy = 5; g.insets = new Insets(10, 6, 6, 6);
        add(btns, g);
    }

    private void doSimpan() {
        try {
            String detail = txtDetail.getText().trim();
            if (detail.isEmpty())
                throw new IllegalArgumentException("Detail tindakan tidak boleh kosong.");

            if (idLaporanTerpilih == -1)
                throw new IllegalStateException("Tidak ada laporan yang dipilih.");

            Admin admin = SessionManager.getAdmin();
            if (admin == null)
                throw new IllegalStateException("Sesi admin tidak valid. Silakan login ulang.");

            TindakLanjut t = new TindakLanjut(
                0,
                idLaporanTerpilih,
                admin.getId(),
                detail,
                LocalDate.now()
            );

            // Service cek: laporan harus ada dan berstatus "diproses"
            new TindakLanjutService().tambah(t);

            JOptionPane.showMessageDialog(this,
                "✅ Tindak lanjut disimpan.\nLaporan telah ditandai SELESAI.",
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
        txtDetail.setText("");
        lblErr.setText(" ");
        if (idLaporanTerpilih != -1) {
            try {
                Laporan l = new LaporanService().cariById(idLaporanTerpilih);
                if (l != null) {
                    lblInfo.setText("Laporan #" + l.getIdLaporan()
                        + "  |  " + l.getJenisPencemaran()
                        + "  |  Status: " + l.getStatusLaporan()
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
