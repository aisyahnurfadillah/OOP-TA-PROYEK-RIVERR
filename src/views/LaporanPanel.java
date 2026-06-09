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
    private final JTextField txtSla = views.UIHelper.buatInput(10); // Input baru untuk UAS

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
        g.gridx = 0; g.gridy = 7; g.gridwidth = 2;
        add(lblErr, g);

        // Input SLA untuk Laporan Prioritas (UAS)
        g.gridx = 0; g.gridy = 6; add(new JLabel("Batas Waktu SLA (Jam):"), g);
        txtSla.setToolTipText("Wajib diisi angka jika tingkat pencemaran BERAT");
        g.gridx = 1; add(txtSla, g);


               // Tombol
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        JButton btnBatal = UIHelper.buatTombolOutline("Batal", Color.GRAY);
        btnBatal.addActionListener(e -> frame.showPanel(MainFrame.DASHBOARD_M));
        JButton btnKirim = UIHelper.buatTombol("Kirim Laporan", UIHelper.BIRU);
        btnKirim.addActionListener(e -> doKirim());
        btnPanel.add(btnBatal);
        btnPanel.add(btnKirim);
        g.gridy = 8; add(btnPanel, g);
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
        if (listTitik == null || listTitik.isEmpty())
            throw new IllegalArgumentException("Pilih sungai dan titik pantau terlebih dahulu.");

        String desk = txtDeskripsi.getText().trim();
        if (desk.isEmpty())
            throw new IllegalArgumentException("Deskripsi tidak boleh kosong.");

        Masyarakat user = services.SessionManager.getMasyarakat();
        if (user == null)
            throw new IllegalStateException("Sesi login tidak valid. Silakan login ulang.");

        TitikPantau titik = listTitik.get(cmbTitik.getSelectedIndex());
        String tingkat = (String) cmbTingkat.getSelectedItem();
        
        Laporan l; // Polimorfisme: Deklarasi tipe Superclass

        // --- TAMBAHAN UAS: Validasi Try-Catch Custom Exception ---
        if ("berat".equalsIgnoreCase(tingkat)) {
            String slaText = txtSla.getText().trim();
            if (slaText.isEmpty()) {
                throw new models.InputTidakValidExceptions("Karena status BERAT, SLA wajib diisi!");
            }
            int slaJam;
            try {
                slaJam = Integer.parseInt(slaText);
                if (slaJam <= 0) throw new models.InputTidakValidExceptions("SLA harus lebih besar dari 0.");
            } catch (NumberFormatException e) {
                throw new models.InputTidakValidExceptions("Input SLA gagal! Harus berupa angka (contoh: 24).");
            }
            
            // Instansiasi Subclass
            l = new models.LaporanPrioritas(0, user.getId(), titik.getIdTitikPantau(),
                    (String) cmbJenis.getSelectedItem(), tingkat, desk, LocalDate.now(), slaJam);
        } else {
            // Instansiasi Superclass normal
            l = new models.Laporan(0, user.getId(), titik.getIdTitikPantau(),
                    (String) cmbJenis.getSelectedItem(), tingkat, desk, LocalDate.now());
        }

        new services.LaporanService().tambah(l);

        JOptionPane.showMessageDialog(this,
            "✅ Laporan berhasil dikirim!\nStatus: Menunggu validasi admin.",
            "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        frame.showPanel(views.MainFrame.DASHBOARD_M);

    } catch (models.InputTidakValidExceptions ex) {
        // Menangkap error dari Custom Exception buatan sendiri
        lblErr.setText("Error Prioritas: " + ex.getMessage());
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
