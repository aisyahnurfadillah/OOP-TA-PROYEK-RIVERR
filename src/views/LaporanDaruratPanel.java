package views;

import exceptions.InputTidakValidException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.LaporanDarurat;
import services.LaporanDaruratService;

/**
 * BAGIAN C - UI & Exception Handling
 * @author Aisyah Nur Fadillah (254311001) - Role 4: QA & Repo Master
 */
public class LaporanDaruratPanel extends JPanel {

    private final LaporanDaruratService service = new LaporanDaruratService();
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final MainFrame frame;

    public LaporanDaruratPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel header = new JLabel("Laporan Darurat Pencemaran Sungai", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        add(header, BorderLayout.NORTH);

        // Table
        String[] kolom = {"ID", "Jenis Pencemaran", "Zat Berbahaya", "Volume (L)", "Status", "Lapor Dinas"};
        tableModel = new DefaultTableModel(kolom, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel tombol
        JPanel panelTombol = new JPanel(new FlowLayout());

        JButton btnTambah = new JButton("Tambah Laporan Darurat");
        JButton btnFilter = new JButton("Filter Darurat");
        JButton btnSort   = new JButton("Sort by Tingkat");

        btnTambah.addActionListener(e -> tambahLaporan());
        btnFilter.addActionListener(e -> tampilkanFilter());
        btnSort.addActionListener(e -> tampilkanSort());

        panelTombol.add(btnTambah);
        panelTombol.add(btnFilter);
        panelTombol.add(btnSort);
        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> frame.showPanel(MainFrame.DASHBOARD_M));
        panelTombol.add(btnKembali);
        add(panelTombol, BorderLayout.SOUTH);
    }



    private void tambahLaporan() {
        try {
            String jenis    = inputWajib("Jenis Pencemaran (misal: Limbah Pabrik):", "Jenis Pencemaran");
            String tingkat  = inputWajib("Tingkat (rendah/sedang/tinggi/kritis):", "Tingkat");
            String desk     = inputWajib("Deskripsi singkat:", "Deskripsi");
            String zat      = inputWajib("Jenis Zat Berbahaya:", "Jenis Zat Berbahaya");
            double vol      = inputVolume();

            service.tambahLaporanDarurat(1, 1, jenis, tingkat, desk, zat, vol);
            tampilkanFilter();
            JOptionPane.showMessageDialog(this, "Laporan darurat berhasil ditambahkan!");

        } catch (InputTidakValidException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String inputWajib(String prompt, String namaField) throws InputTidakValidException {
        String input = JOptionPane.showInputDialog(this, prompt);
        if (input == null || input.isBlank())
            throw new InputTidakValidException(namaField, "tidak boleh kosong!");
        return input.trim();
    }

    private double inputVolume() throws InputTidakValidException {
        String input = JOptionPane.showInputDialog(this, "Estimasi Volume Tumpahan (liter):");
        try {
            double vol = Double.parseDouble(input);
            if (vol <= 0)
                throw new InputTidakValidException("Estimasi Volume", "harus lebih dari 0 liter!");
            return vol;
        } catch (NumberFormatException e) {
            throw new InputTidakValidException("Estimasi Volume", "harus berupa angka, bukan teks!");
        }
    }

    private void tampilkanFilter() {
        tableModel.setRowCount(0);
        List<LaporanDarurat> list = service.filterLaporanDarurat();
        for (LaporanDarurat l : list) {
            tableModel.addRow(new Object[]{
                l.getIdLaporan(), l.getJenisPencemaran(),
                l.getJenisZatBerbahaya(), l.getEstimasiVolumeLiter(),
                l.getStatusLaporan(),
                l.isSudahDilaporkanKeDinas() ? "Ya" : "Belum"
            });
        }
    }

    private void tampilkanSort() {
        tableModel.setRowCount(0);
        service.sortByTingkatKeparahan().forEach(l -> {
            if (l instanceof LaporanDarurat ld) {
                tableModel.addRow(new Object[]{
                    ld.getIdLaporan(), ld.getJenisPencemaran(),
                    ld.getJenisZatBerbahaya(), ld.getEstimasiVolumeLiter(),
                    ld.getStatusLaporan(), ld.isSudahDilaporkanKeDinas() ? "Ya" : "Belum"
                });
            }
        });
    }
}
