package views;

import services.AdminService;
import services.MasyarakatService;
import services.SessionManager;
import models.Pengguna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel implements Refreshable {

    private final MainFrame      frame;
    private final JTextField     txtId  = UIHelper.buatInput(22);
    private final JPasswordField txtPw  = UIHelper.buatPassword(22);
    private final JLabel         lblErr = UIHelper.buatLabelError();

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(UIHelper.BIRU_MUDA);
        buildUI();
    }

    private void buildUI() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(32, 48, 32, 48)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.gridwidth = 2;

        // Judul
        JLabel lblJudul = new JLabel("🌊 RiverR", SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblJudul.setForeground(UIHelper.BIRU);
        g.gridx = 0; g.gridy = 0;
        card.add(lblJudul, g);

        // Subtitle
        JLabel lblSub = new JLabel(
            "Sistem Pelaporan Pencemaran Sungai", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        g.gridy = 1; g.insets = new Insets(0, 4, 18, 4);
        card.add(lblSub, g);

        // Fields
        g.insets = new Insets(6, 4, 2, 4);
        g.gridy = 2; g.gridwidth = 1; g.gridx = 0;
        card.add(new JLabel("Email / Username:"), g);
        g.gridx = 1; card.add(txtId, g);

        g.gridx = 0; g.gridy = 3;
        card.add(new JLabel("Password:"), g);
        g.gridx = 1; card.add(txtPw, g);

        // Error
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        g.insets = new Insets(2, 4, 2, 4);
        card.add(lblErr, g);

        // Tombol login
        JButton btnLogin = UIHelper.buatTombol("Login", UIHelper.BIRU);
        btnLogin.setPreferredSize(new Dimension(0, 38));
        btnLogin.addActionListener(e -> doLogin());
        g.gridy = 5; g.insets = new Insets(8, 4, 4, 4);
        card.add(btnLogin, g);

        // Link daftar
        JButton btnDaftar = new JButton("Belum punya akun? Daftar di sini");
        btnDaftar.setBorderPainted(false);
        btnDaftar.setContentAreaFilled(false);
        btnDaftar.setForeground(UIHelper.BIRU);
        btnDaftar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnDaftar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDaftar.addActionListener(e -> frame.showPanel(MainFrame.REGISTER));
        g.gridy = 6; g.insets = new Insets(2, 4, 4, 4);
        card.add(btnDaftar, g);

        add(card);
    }

    private void doLogin() {
        // ══════════════════════════════════════════════════════════
        // EXCEPTION HANDLING — ini yang ditunjukkan ke dosen
        // ══════════════════════════════════════════════════════════
        try {
            String id = txtId.getText().trim();
            String pw = new String(txtPw.getPassword());

            // Validasi input kosong — throw manual
            if (id.isEmpty() || pw.isEmpty())
                throw new IllegalArgumentException(
                    "Email/username dan password wajib diisi.");

            // Coba Admin dulu, lalu Masyarakat
            // Polymorphism: return type Pengguna, actual bisa Admin atau Masyarakat
            Pengguna p = new AdminService().login(id, pw);
            if (p == null) p = new MasyarakatService().login(id, pw);

            if (p == null)
                throw new IllegalArgumentException(
                    "Kredensial salah atau akun tidak aktif.");

            SessionManager.login(p);
            lblErr.setText(" ");
            frame.navigateAfterLogin();

        } catch (IllegalArgumentException ex) {
            // Error yang diantisipasi — tampilkan pesan di label, bukan popup
            lblErr.setText(ex.getMessage());
        } catch (Exception ex) {
            // Safety net — error tak terduga agar aplikasi tidak crash
            lblErr.setText("Terjadi error: " + ex.getMessage());
        }
    }

    @Override
    public void onShow() {
        // Reset form setiap kali panel ditampilkan (misal setelah logout)
        txtId.setText("");
        txtPw.setText("");
        lblErr.setText(" ");
        txtId.requestFocusInWindow(); // cursor langsung ke field pertama
    }
}
