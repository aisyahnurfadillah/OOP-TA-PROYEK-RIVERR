package views;
import models.*;
import services.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;


/**
 * Panel Form Buat Laporan Pencemaran.
 * Dropdown sungai → pilih sungai → dropdown titik pantau otomatis diisi.
 *
 * KONSEP OOP:
 *  - extends JPanel, implements Refreshable
 *  - Exception Handling: validasi input + cek sesi login
 *  - Memanggil LaporanService.tambah() yang memanggil validate() model
 *
 * @author Stipen (Role 3 - UI & Robustness Engineer)
 */
public class LaporanPanel extends JPanel implements Refreshable {

    private final MainFrame frame;

    private final JComboBox<String> cmbSungai  = new JComboBox<>();
    private final JComboBox<String> cmbTitik   = new JComboBox<>();
    private final JComboBox<String> cmbJenis   = new JComboBox<>(new String[]{
        "Limbah Industri", "Sampah Domestik", "Minyak/BBM", "Limbah Pertanian", "Lainnya"
    });
    private final JComboBox<String> cmbTingkat = new JComboBox<>(new String[]{
        "ringan", "sedang", "berat"
    });
    private final JTextArea txtDeskripsi = new JTextArea(4, 30);
    private final JLabel    lblErr       = UIHelper.buatLabelError();

    // Simpan list untuk mapping index dropdown → object
    private List<Sungai>      listSungai;
    private List<TitikPantau> listTitik;

    public LaporanPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(22, 36, 22, 36));
        setBackground(UIHelper.ABU);
        buildUI();
    }

    private void buildUI() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 6, 7, 6);
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        // Judul
        JLabel judul = UIHelper.buatJudul("Form Laporan Pencemaran");
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        add(judul, g);

        // Sungai
        g.gridy = 1; g.gridwidth = 1; g.gridx = 0;
        add(new JLabel("Sungai:"), g);
        g.gridx = 1; add(cmbSungai, g);
        cmbSungai.addActionListener(e -> loadTitikPantau()); // event: sungai ganti → update titik

        // Titik Pantau
        g.gridx = 0; g.gridy = 2; add(new JLabel("Titik Pantau:"), g);
        g.gridx = 1; add(cmbTitik, g);

        // Jenis Pencemaran
        g.gridx = 0; g.gridy = 3; add(new JLabel("Jenis Pencemaran:"), g);
        g.gridx = 1; add(cmbJenis, g);

        // Tingkat
        g.gridx = 0; g.gridy = 4; add(new JLabel("Tingkat Keparahan:"), g);
        g.gridx = 1; add(cmbTingkat, g);

        // Deskripsi
        g.gridx = 0; g.gridy = 5; add(new JLabel("Deskripsi:"), g);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        g.gridx = 1; add(new JScrollPane(txtDeskripsi), g);

        // Error
        g.gridx = 0; g.gridy = 6; g.gridwidth = 2;
        add(lblErr, g);

        // Tombol
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        JButton btnBatal = UIHelper.buatTombolOutline("Batal", Color.GRAY);
        btnBatal.addActionListener(e -> frame.showPanel(MainFrame.DASHBOARD_M));
        JButton btnKirim = UIHelper.buatTombol("Kirim Laporan", UIHelper.BIRU);
        btnKirim.addActionListener(e -> doKirim());
        btnPanel.add(btnBatal);
        btnPanel.add(btnKirim);
        g.gridy = 7; add(btnPanel, g);
    }

    /** Isi dropdown titik pantau berdasarkan sungai yang dipilih */
    private void loadTitikPantau() {
        cmbTitik.removeAllItems();
        if (listSungai == null || cmbSungai.getSelectedIndex() < 0) return;
        try {
            Sungai dipilih = listSungai.get(cmbSungai.getSelectedIndex());
            listTitik = new SungaiService().getTitikBySungai(dipilih.getIdSungai());
            for (TitikPantau t : listTitik)
                cmbTitik.addItem("Titik #" + t.getIdTitikPantau()
                    + "  (" + t.getKoordinatLat() + ", " + t.getKoordinatLong() + ")");
        } catch (Exception ex) {
            lblErr.setText("Gagal memuat titik pantau.");
        }
    }

    private void doKirim() {
        try {
            // Validasi input
            if (listTitik == null || listTitik.isEmpty())
                throw new IllegalArgumentException("Pilih sungai dan titik pantau terlebih dahulu.");

            String desk = txtDeskripsi.getText().trim();
            if (desk.isEmpty())
                throw new IllegalArgumentException("Deskripsi tidak boleh kosong.");

            // Cek sesi login
            Masyarakat user = SessionManager.getMasyarakat();
            if (user == null)
                throw new IllegalStateException("Sesi login tidak valid. Silakan login ulang.");

            TitikPantau titik = listTitik.get(cmbTitik.getSelectedIndex());

            // Buat object Laporan — id=0 karena akan di-assign Service
            Laporan l = new Laporan(
                0,
                user.getId(),
                titik.getIdTitikPantau(),
                (String) cmbJenis.getSelectedItem(),
                (String) cmbTingkat.getSelectedItem(),
                desk,
                LocalDate.now()
            );

            // Service memanggil l.validate() sebelum simpan
            new LaporanService().tambah(l);

            JOptionPane.showMessageDialog(this,
                "✅ Laporan berhasil dikirim!\nStatus: Menunggu validasi admin.",
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            frame.showPanel(MainFrame.DASHBOARD_M);

        } catch (IllegalArgumentException | IllegalStateException ex) {
            lblErr.setText(ex.getMessage());
        } catch (Exception ex) {
            lblErr.setText("Error tidak terduga: " + ex.getMessage());
        }
    }

    @Override
    public void onShow() {
        lblErr.setText(" ");
        txtDeskripsi.setText("");
        cmbSungai.removeAllItems();
        cmbTitik.removeAllItems();
        try {
            listSungai = new SungaiService().cariSemua();
            for (Sungai s : listSungai)
                cmbSungai.addItem(s.getNama() + "  –  " + s.getWilayah());
        } catch (Exception ex) {
            lblErr.setText("Gagal memuat data sungai.");
        }
    }
}
