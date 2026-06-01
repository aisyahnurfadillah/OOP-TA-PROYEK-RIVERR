package views;

import services.AdminService;
import services.MasyarakatService;
import services.SessionManager;
import models.Pengguna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel implements Refreshable {

    private final MainFrame frame;
    private final JTextField txtId = UIHelper.buatInput(18);
    private final JPasswordField txtPw = UIHelper.buatPassword(18);
    private final JLabel lblErr = UIHelper.buatLabelError();

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
                new EmptyBorder(32, 40, 32, 40)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Judul (2 Kolom)
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        JLabel lblJudul = new JLabel("🌊 RiverR", SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblJudul.setForeground(UIHelper.BIRU);
        card.add(lblJudul, g);

        // Subtitle (2 Kolom)
        g.gridy = 1;
        g.insets = new Insets(0, 6, 20, 6);
        JLabel lblSub = new JLabel("Sistem Pelaporan Pencemaran Sungai", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        card.add(lblSub, g);

        // Reset ke 1 kolom untuk Form Input
        g.gridwidth = 1;
        g.insets = new Insets(6, 6, 6, 6);

        // Baris Email
        g.gridx = 0;
        g.gridy = 2;
        g.weightx = 0.1;
        card.add(UIHelper.buatLabel("Email / Username:"), g);
        g.gridx = 1;
        g.weightx = 0.9;
        card.add(txtId, g);

        // Baris Password
        g.gridx = 0;
        g.gridy = 3;
        g.weightx = 0.1;
        card.add(UIHelper.buatLabel("Password:"), g);
        g.gridx = 1;
        g.weightx = 0.9;
        card.add(txtPw, g);

        // Reset untuk komponen bawah (2 Kolom)
        g.weightx = 0.0;
        g.gridwidth = 2;

        // Error Label
        g.gridx = 0;
        g.gridy = 4;
        g.insets = new Insets(2, 6, 4, 6);
        card.add(lblErr, g);

        // Tombol Login
        g.gridy = 5;
        g.insets = new Insets(8, 6, 6, 6);
        JButton btnLogin = UIHelper.buatTombol("Login", UIHelper.BIRU);
        btnLogin.setPreferredSize(new Dimension(0, 38));
        btnLogin.addActionListener(e -> doLogin());
        card.add(btnLogin, g);

        // Link Daftar
        g.gridy = 6;
        g.insets = new Insets(4, 6, 6, 6);
        JButton btnDaftar = new JButton("Belum punya akun? Daftar di sini");
        btnDaftar.setBorderPainted(false);
        btnDaftar.setContentAreaFilled(false);
        btnDaftar.setForeground(UIHelper.BIRU);
        btnDaftar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnDaftar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDaftar.addActionListener(e -> frame.showPanel(MainFrame.REGISTER));
        card.add(btnDaftar, g);

        add(card);
    }

    private void doLogin() {
        try {
            String id = txtId.getText().trim();
            String pw = new String(txtPw.getPassword());

            if (id.isEmpty() || pw.isEmpty())
                throw new IllegalArgumentException("Email/username dan password wajib diisi.");

            Pengguna p = new AdminService().login(id, pw);
            if (p == null)
                p = new MasyarakatService().login(id, pw);

            if (p == null)
                throw new IllegalArgumentException("Kredensial salah atau akun tidak aktif.");

            SessionManager.login(p);
            lblErr.setText(" ");
            frame.navigateAfterLogin();

        } catch (IllegalArgumentException ex) {
            lblErr.setText(ex.getMessage());
        } catch (Exception ex) {
            lblErr.setText("Terjadi error: " + ex.getMessage());
        }
    }

    @Override
    public void onShow() {
        txtId.setText("");
        txtPw.setText("");
        lblErr.setText(" ");
        txtId.requestFocusInWindow();
    }
}