package views;

import models.Laporan;
import models.Masyarakat;
import services.LaporanService;
import services.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dashboard Masyarakat — tampilkan statistik dan daftar laporan milik user.
 *
 * KONSEP OOP:
 * - extends JPanel, implements Refreshable
 * - DefaultTableModel : model data JTable (Override isCellEditable)
 * - SessionManager : ambil data user yang login (polymorphism)
 * - Exception Handling: try-catch pada loadData()
 *
 * @author Stipen (Role 3 - UI & Robustness Engineer)
 */
public class DashboardMasyarakatPanel extends JPanel implements Refreshable {

    private final MainFrame frame;

    // Header
    private final JLabel lblSapa = new JLabel();

    // Kartu statistik
    private final JLabel lblTotal = new JLabel("0");
    private final JLabel lblMenunggu = new JLabel("0");
    private final JLabel lblDiproses = new JLabel("0");
    private final JLabel lblSelesai = new JLabel("0");

    // PERBAIKAN: Menambahkan kolom "Deskripsi" ke dalam struktur Model Tabel
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[] { "ID", "Jenis Pencemaran", "Tingkat", "Deskripsi", "Status", "Tanggal" }, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        } // read-only
    };
    private final JTable table = new JTable(tableModel);

    public DashboardMasyarakatPanel(MainFrame frame) {
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
        btnLogout.addActionListener(e -> {
            SessionManager.logout();
            frame.showPanel(MainFrame.LOGIN);
        });
        header.add(btnLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── KARTU STATISTIK ───────────────────────────────────
        JPanel statPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        statPanel.setOpaque(false);

        statPanel.add(buatKartu("Total Laporan", lblTotal, Color.DARK_GRAY));
        statPanel.add(buatKartu("Menunggu", lblMenunggu, new Color(160, 100, 0)));
        statPanel.add(buatKartu("Diproses", lblDiproses, UIHelper.BIRU));
        statPanel.add(buatKartu("Selesai", lblSelesai, UIHelper.HIJAU));

        // ── CENTER: stat + tabel ──────────────────────────────
        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(statPanel, BorderLayout.NORTH);

        UIHelper.styleTable(table);

        // PERBAIKAN: Index kolom Status bergeser ke-4 karena ada Deskripsi di indeks 3
        table.getColumnModel().getColumn(4)
                .setCellRenderer(UIHelper.buatRendererStatus());

        // PERBAIKAN: Penyesuaian proporsi lebar kolom untuk menampung deskripsi yang
        // panjang
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Jenis Pencemaran
        table.getColumnModel().getColumn(2).setPreferredWidth(80); // Tingkat
        table.getColumnModel().getColumn(3).setPreferredWidth(220); // Deskripsi (diberi porsi luas)
        table.getColumnModel().getColumn(4).setPreferredWidth(90); // Status
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Tanggal

        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // ── FOOTER ────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        JButton btnBuat = UIHelper.buatTombol("+ Buat Laporan Baru", UIHelper.BIRU);
        btnBuat.addActionListener(e -> frame.showPanel(MainFrame.LAPORAN));
        footer.add(btnBuat);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel buatKartu(String judul, JLabel lblAngka, Color warnaAngka) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.BORDER, 1, true),
                new EmptyBorder(12, 16, 12, 16)));
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
        tableModel.setRowCount(0); // bersihkan tabel dulu
        try {
            Masyarakat m = SessionManager.getMasyarakat();
            if (m == null)
                return;

            List<Laporan> list = new LaporanService().cariByMasyarakat(m.getId());
            // Hitung statistik
            long menunggu = list.stream().filter(l -> Laporan.STATUS_MENUNGGU.equals(l.getStatusLaporan())).count();
            long diproses = list.stream().filter(l -> Laporan.STATUS_DIPROSES.equals(l.getStatusLaporan())).count();
            long selesai = list.stream().filter(l -> Laporan.STATUS_SELESAI.equals(l.getStatusLaporan())).count();

            lblTotal.setText(String.valueOf(list.size()));
            lblMenunggu.setText(String.valueOf(menunggu));
            lblDiproses.setText(String.valueOf(diproses));
            lblSelesai.setText(String.valueOf(selesai));

            // Isi tabel (Urutan array ini sekarang pas dengan jumlah kolom model: 6 kolom)
            for (Laporan l : list) {
                tableModel.addRow(new Object[] {
                        "#" + l.getIdLaporan(),
                        l.getJenisPencemaran(),
                        l.getTingkatPencemaran(),
                        l.getDeskripsi(),
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
        Masyarakat m = SessionManager.getMasyarakat();
        if (m != null)
            lblSapa.setText("👋 Selamat datang, " + m.getNama());
        loadData();
    }
}