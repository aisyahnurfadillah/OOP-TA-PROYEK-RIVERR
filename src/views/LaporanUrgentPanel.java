package views;

import exceptions.InputTidakValidException;
import models.*;
import services.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Panel Form Laporan Urgent — fitur prioritas/darurat.
 *
 * KONSEP OOP:
 *  - extends JPanel, implements Refreshable
 *  - try-catch InputTidakValidException (Custom Exception)
 *
 * @author Silfina (254311007)
 */
public class LaporanUrgentPanel extends JPanel implements Refreshable {

    private final MainFrame frame;

    private final JComboBox<String> cmbSungai  = new JComboBox<>();
    private final JComboBox<String> cmbTitik   = new JComboBox<>();
    private final JComboBox<String> cmbJenis   = new JComboBox<>(new String[]{
        "Limbah Industri", "Sampah Domestik", "Minyak/BBM", "Limbah Pertanian", "Lainnya"
    });
    private final JComboBox<String> cmbTingkat = new JComboBox<>(new String[]{
        "ringan", "sedang", "berat"
    });
    private final JComboBox<String> cmbUrgensi = new JComboBox<>(new String[]{
        "TINGGI", "KRITIS"
    });
    private final JTextArea txtDeskripsi = new JTextArea(4, 30);
    private final JLabel    lblErr       = UIHelper.buatLabelError();

    private List<Sungai>      listSungai;
    private List<TitikPantau> listTitik;

    public LaporanUrgentPanel(MainFrame frame) {
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

        JLabel judul = UIHelper.buatJudul("!! Form Laporan URGENT");
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        add(judul, g);

        g.gridy = 1; g.gridwidth = 1; g.gridx = 0;
        add(new JLabel("Tingkat Urgensi:"), g);
        g.gridx = 1; add(cmbUrgensi, g);

        g.gridx = 0; g.gridy = 2; add(new JLabel("Sungai:"), g);
        g.gridx = 1; add(cmbSungai, g);
        cmbSungai.addActionListener(e -> loadTitikPantau());

        g.gridx = 0; g.gridy = 3; add(new JLabel("Titik Pantau:"), g);
        g.gridx = 1; add(cmbTitik, g);

        g.gridx = 0; g.gridy = 4; add(new JLabel("Jenis Pencemaran:"), g);
        g.gridx = 1; add(cmbJenis, g);

        g.gridx = 0; g.gridy = 5; add(new JLabel("Tingkat Keparahan:"), g);
        g.gridx = 1; add(cmbTingkat, g);

        g.gridx = 0; g.gridy = 6; add(new JLabel("Deskripsi:"), g);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        g.gridx = 1; add(new JScrollPane(txtDeskripsi), g);

        g.gridx = 0; g.gridy = 7; g.gridwidth = 2;
        add(lblErr, g);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        JButton btnBatal = UIHelper.buatTombolOutline("Batal", Color.GRAY);
        btnBatal.addActionListener(e -> frame.showPanel(MainFrame.DASHBOARD_M));
        JButton btnKirim = UIHelper.buatTombol("Kirim Laporan Urgent", Color.RED.darker());
        btnKirim.addActionListener(e -> doKirim());
        btnPanel.add(btnBatal);
        btnPanel.add(btnKirim);
        g.gridy = 8; add(btnPanel, g);
    }

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
                throw new InputTidakValidException("Pilih sungai dan titik pantau terlebih dahulu.");

            String desk = txtDeskripsi.getText().trim();
            if (desk.isEmpty())
                throw new InputTidakValidException("Deskripsi tidak boleh kosong.");

            Masyarakat user = SessionManager.getMasyarakat();
            if (user == null)
                throw new InputTidakValidException("Sesi login tidak valid. Silakan login ulang.");

            TitikPantau titik = listTitik.get(cmbTitik.getSelectedIndex());

            LaporanUrgent lu = new LaporanUrgent(
                user.getId(),
                titik.getIdTitikPantau(),
                (String) cmbJenis.getSelectedItem(),
                (String) cmbTingkat.getSelectedItem(),
                desk,
                (String) cmbUrgensi.getSelectedItem()
            );

            new LaporanService().tambahUrgent(lu);

            JOptionPane.showMessageDialog(this,
                "Laporan URGENT berhasil dikirim!\nStatus: Menunggu validasi admin.",
                "Berhasil", JOptionPane.WARNING_MESSAGE);
            frame.showPanel(MainFrame.DASHBOARD_M);

        } catch (InputTidakValidException ex) {
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