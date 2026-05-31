package views;

import models.Admin;
import models.Laporan;
import services.LaporanService;
import services.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dashboard Admin — tampilkan semua laporan dari semua masyarakat.
 * Admin pilih baris, lalu klik tombol Validasi atau Tindak Lanjut.
 *
 * @author Stipen (Role 3 - UI & Robustness Engineer)
 */
public class DashboardAdminPanel extends JPanel implements Refreshable {

    private final MainFrame frame;
    private final JLabel lblSapa = new JLabel();

    // Statistik
    private final JLabel lblTotal    = new JLabel("0");
    private final JLabel lblMenunggu = new JLabel("0");
    private final JLabel lblDiproses = new JLabel("0");
    private final JLabel lblSelesai  = new JLabel("0");

    // Tabel
    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"ID", "Pelapor ID", "Jenis Pencemaran", "Tingkat", "Status", "Tanggal"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    // Simpan ID laporan yang dipilih — dikirim ke panel berikutnya
    private int idLaporanTerpilih = -1;

    public DashboardAdminPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(18, 22, 18, 22));
        setBackground(UIHelper.ABU);
        buildUI();
    }

    private void buildUI() {
        // ── HEADER ────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        lblSapa.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblSapa.setForeground(UIHelper.BIRU);
        header.add(lblSapa, BorderLayout.WEST);
        JButton btnLogout = UIHelper.buatTombolOutline("Logout", UIHelper.MERAH);
        btnLogout.addActionListener(e -> { SessionManager.logout(); frame.showPanel(MainFrame.LOGIN); });
        header.add(btnLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── STATISTIK ─────────────────────────────────────────
        JPanel statPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        statPanel.setOpaque(false);
        statPanel.add(buatKartu("Total Laporan",    lblTotal,    Color.DARK_GRAY));
        statPanel.add(buatKartu("Menunggu",         lblMenunggu, new Color(160, 100, 0)));
        statPanel.add(buatKartu("Diproses",         lblDiproses, UIHelper.BIRU));
        statPanel.add(buatKartu("Selesai",          lblSelesai,  UIHelper.HIJAU));

        // ── CENTER ────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(statPanel, BorderLayout.NORTH);

        // Styling tabel
        UIHelper.styleTable(table);
        table.getColumnModel().getColumn(4).setCellRenderer(UIHelper.buatRendererStatus());
        table.getColumnModel().getColumn(0).setPreferredWidth(45);
        table.getColumnModel().getColumn(1).setPreferredWidth(75);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        // Event: simpan ID laporan saat baris dipilih
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                String idStr = tableModel.getValueAt(table.getSelectedRow(), 0).toString();
                idLaporanTerpilih = Integer.parseInt(idStr.replace("#", ""));
            }
        });

        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // ── FOOTER ────────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        JLabel hint = UIHelper.buatSubtitle("← Pilih baris laporan, lalu klik aksi");
        footer.add(hint, BorderLayout.WEST);

        JPanel aksi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        aksi.setOpaque(false);
        JButton btnVal = UIHelper.buatTombol("✔ Validasi", UIHelper.BIRU);
        JButton btnTL  = UIHelper.buatTombol("⚡ Tindak Lanjut", UIHelper.HIJAU);

        btnVal.addActionListener(e -> pindahKePanel(MainFrame.VALIDASI));
        btnTL.addActionListener(e  -> pindahKePanel(MainFrame.TINDAK));

        aksi.add(btnVal); aksi.add(btnTL);
        footer.add(aksi, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);
    }

    private void pindahKePanel(String namaPanel) {
        try {
            if (idLaporanTerpilih == -1)
                throw new IllegalStateException("Pilih laporan dari tabel terlebih dahulu.");
            // Kirim ID ke panel tujuan lewat static setter
            if (namaPanel.equals(MainFrame.VALIDASI))
                ValidasiPanel.setIdLaporan(idLaporanTerpilih);
            else
                TindakLanjutPanel.setIdLaporan(idLaporanTerpilih);
            frame.showPanel(namaPanel);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JPanel buatKartu(String judul, JLabel lblAngka, Color warnaAngka) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));
        JLabel lJudul = new JLabel(judul);
        lJudul.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lJudul.setForeground(Color.GRAY);
        lJudul.setAlignmentX(LEFT_ALIGNMENT);
        lblAngka.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblAngka.setForeground(warnaAngka);
        lblAngka.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lJudul);
        p.add(Box.createVerticalStrut(4));
        p.add(lblAngka);
        return p;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        idLaporanTerpilih = -1;
        try {
            List<Laporan> list = new LaporanService().cariSemua();
            long menunggu = list.stream().filter(l -> Laporan.STATUS_MENUNGGU.equals(l.getStatusLaporan())).count();
            long diproses = list.stream().filter(l -> Laporan.STATUS_DIPROSES.equals(l.getStatusLaporan())).count();
            long selesai  = list.stream().filter(l -> Laporan.STATUS_SELESAI.equals(l.getStatusLaporan())).count();

            lblTotal.setText(String.valueOf(list.size()));
            lblMenunggu.setText(String.valueOf(menunggu));
            lblDiproses.setText(String.valueOf(diproses));
            lblSelesai.setText(String.valueOf(selesai));

            for (Laporan l : list) {
                tableModel.addRow(new Object[]{
                    "#" + l.getIdLaporan(),
                    "User #" + l.getIdMasyarakat(),
                    l.getJenisPencemaran(),
                    l.getTingkatPencemaran(),
                    l.getStatusLaporan(),
                    l.getTanggalLaporan()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onShow() {
        Admin a = SessionManager.getAdmin();
        if (a != null) lblSapa.setText("🛡️ Admin: " + a.getNama());
        loadData();
    }
}
