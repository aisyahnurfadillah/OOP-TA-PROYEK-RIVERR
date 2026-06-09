package views;

import services.SessionManager;
import javax.swing.*;
import java.awt.*;

/**
 * Window utama aplikasi RiverR.
 *
 * KONSEP OOP:
 * - Inheritance : extends JFrame
 * - Polymorphism : instanceof Refreshable pada loop Component
 * - Static const : nama panel sebagai konstanta
 *
 * @author Stipwn (Role 3 - UI & Robustness Engineer)
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String DASHBOARD_M = "DASHBOARD_MASYARAKAT";
    public static final String DASHBOARD_A = "DASHBOARD_ADMIN";
    public static final String LAPORAN = "LAPORAN";
    public static final String VALIDASI = "VALIDASI";
    public static final String TINDAK = "TINDAK_LANJUT";
    public static final String PETUGAS = "PETUGAS_UAS"; // <-- Tambahkan ini

    public MainFrame() {
        setTitle("RiverR – Sistem Pelaporan Pencemaran Sungai");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 640);
        setLocationRelativeTo(null);
        setResizable(false);

        container.add(new LoginPanel(this), LOGIN);
        container.add(new RegisterPanel(this), REGISTER);
        container.add(new DashboardMasyarakatPanel(this), DASHBOARD_M);
        container.add(new DashboardAdminPanel(this), DASHBOARD_A);
        container.add(new LaporanPanel(this), LAPORAN);
        container.add(new ValidasiPanel(this), VALIDASI);
        container.add(new TindakLanjutPanel(this), TINDAK);
        container.add(new PetugasPanel(this), PETUGAS);

        add(container);
        showPanel(LOGIN);
    }

    public void showPanel(String name) {
        // POLYMORPHISM: cek instanceof Refreshable sebelum panggil onShow()
        for (Component c : container.getComponents()) {
            if (c instanceof Refreshable r)
                r.onShow();
        }
        cardLayout.show(container, name);
    }

    public void navigateAfterLogin() {
        showPanel(SessionManager.isAdmin() ? DASHBOARD_A : DASHBOARD_M);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new MainFrame().setVisible(true);
        });
    }
}
