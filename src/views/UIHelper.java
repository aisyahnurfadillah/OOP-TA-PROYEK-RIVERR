package views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Kumpulan method helper untuk styling UI yang konsisten.
 * Semua panel menggunakan class ini.
 *
 * @author Stipen (Role 3 - UI & Robustness Engineer)
 */
public class UIHelper {

    // Warna utama aplikasi
    public static final Color BIRU      = new Color(25, 95, 165);
    public static final Color BIRU_MUDA = new Color(236, 245, 255);
    public static final Color HIJAU     = new Color(40, 130, 60);
    public static final Color MERAH     = new Color(180, 40, 40);
    public static final Color ABU       = new Color(245, 245, 247);
    public static final Color BORDER    = new Color(210, 220, 235);

    /** Tombol dengan warna latar dan teks putih */
    public static JButton buatTombol(String teks, Color warnaLatar) {
        JButton b = new JButton(teks);
        b.setBackground(warnaLatar);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        return b;
    }

    /** Tombol outline — transparan dengan border berwarna */
    public static JButton buatTombolOutline(String teks, Color warna) {
        JButton b = new JButton(teks);
        b.setBackground(Color.WHITE);
        b.setForeground(warna);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(warna, 1, true),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        return b;
    }

    /** Label error merah — default teks spasi agar tidak mengubah layout */
    public static JLabel buatLabelError() {
        JLabel lbl = new JLabel(" ");
        lbl.setForeground(MERAH);
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        return lbl;
    }

    /** Label judul halaman */
    public static JLabel buatJudul(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(BIRU);
        return lbl;
    }

    /** Label kecil abu untuk keterangan */
    public static JLabel buatSubtitle(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(Color.GRAY);
        return lbl;
    }

    /** Input field standar */
    public static JTextField buatInput(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return tf;
    }

    /** Password field standar */
    public static JPasswordField buatPassword(int cols) {
        JPasswordField pf = new JPasswordField(cols);
        pf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return pf;
    }

    /** Terapkan styling seragam ke JTable */
    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(210, 228, 252));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 235, 240));
        table.setShowGrid(true);
        table.getTableHeader().setBackground(BIRU);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Renderer kolom Status — warna latar berbeda per nilai status.
     * menunggu=kuning | diproses=biru | selesai=hijau | ditolak=merah
     */
    public static DefaultTableCellRenderer buatRendererStatus() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(CENTER);
                if (!isSelected) {
                    String v = value != null ? value.toString() : "";
                    switch (v) {
                        case "menunggu" -> {
                            setBackground(new Color(255, 243, 205));
                            setForeground(new Color(130, 80, 0));
                        }
                        case "diproses" -> {
                            setBackground(new Color(207, 226, 255));
                            setForeground(new Color(10, 50, 120));
                        }
                        case "selesai" -> {
                            setBackground(new Color(212, 237, 218));
                            setForeground(new Color(20, 80, 30));
                        }
                        case "ditolak" -> {
                            setBackground(new Color(248, 215, 218));
                            setForeground(new Color(100, 20, 20));
                        }
                        default -> {
                            setBackground(Color.WHITE);
                            setForeground(Color.BLACK);
                        }
                    }
                }
                return this;
            }
        };
    }

    private UIHelper() {} // cegah instantiasi
}
