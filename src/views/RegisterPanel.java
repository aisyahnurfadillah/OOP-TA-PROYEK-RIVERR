package views;

import services.MasyarakatService;
import models.Masyarakat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel Registrasi akun Masyarakat.
 *
 * KONSEP OOP:
 * - extends JPanel, implements Refreshable
 * - Exception Handling: validasi form + catch duplikat dari Service
 *
 * @author Stipen (Role 3 - UI & Robustness Engineer)
 */
public class RegisterPanel extends JPanel implements Refreshable {

    private final MainFrame frame;
    private final JTextField txtNama = UIHelper.buatInput(22);
    private final JTextField txtEmail = UIHelper.buatInput(22);
    private final JTextField txtAlamat = UIHelper.buatInput(22);
    private final JPasswordField txtPw = UIHelper.buatPassword(22);
    private final JPasswordField txtPw2 = UIHelper.buatPassword(22);
    private final JLabel lblErr = UIHelper.buatLabelError();

    public RegisterPanel(MainFrame frame) {
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
                new EmptyBorder(28, 44, 28, 44)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 4, 5, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridwidth = 2;

        JLabel judul = UIHelper.buatJudul("Daftar Akun Masyarakat");
        judul.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridx = 0;
        g.gridy = 0;
        g.insets = new Insets(0, 4, 16, 4);
        card.add(judul, g);

        // Pasangan label + input
        String[] labels = { "Nama Lengkap:", "Email:", "Alamat:", "Password:", "Konfirmasi Password:" };
        JComponent[] fields = { txtNama, txtEmail, txtAlamat, txtPw, txtPw2 };

        g.insets = new Insets(4, 4, 4, 4);
        for (int i = 0; i < labels.length; i++) {
            g.gridy = i + 1;
            g.gridwidth = 1;
            g.gridx = 0;
            card.add(new JLabel(labels[i]), g);
            g.gridx = 1;
            card.add(fields[i], g);
        }

        // Error
        g.gridx = 0;
        g.gridy = 6;
        g.gridwidth = 2;
        g.insets = new Insets(2, 4, 2, 4);
        card.add(lblErr, g);

        // Tombol daftar
        JButton btnDaftar = UIHelper.buatTombol("Daftar", UIHelper.HIJAU);
        btnDaftar.setPreferredSize(new Dimension(0, 38));
        btnDaftar.addActionListener(e -> doRegister());
        g.gridy = 7;
        g.insets = new Insets(8, 4, 4, 4);
        card.add(btnDaftar, g);

        // Link login
        JButton btnLogin = new JButton("Sudah punya akun? Login");
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setForeground(UIHelper.BIRU);
        btnLogin.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> frame.showPanel(MainFrame.LOGIN));
        g.gridy = 8;
        g.insets = new Insets(2, 4, 4, 4);
        card.add(btnLogin, g);

        add(card);
    }

    private void doRegister() {
        try {
            String nama = txtNama.getText().trim();
            String email = txtEmail.getText().trim();
            String alamat = txtAlamat.getText().trim();
            String pw = new String(txtPw.getPassword());
            String pw2 = new String(txtPw2.getPassword());

            // Validasi di View sebelum kirim ke Service
            if (nama.isEmpty() || email.isEmpty() || alamat.isEmpty() || pw.isEmpty())
                throw new IllegalArgumentException("Semua field wajib diisi.");
            if (!pw.equals(pw2))
                throw new IllegalArgumentException("Password dan konfirmasi tidak cocok.");

            // Service akan validate() dan cek duplikat email
            Masyarakat m = new Masyarakat(0, nama, email, alamat, pw, "aktif");
            new MasyarakatService().tambah(m);

            JOptionPane.showMessageDialog(this,
                    "Registrasi berhasil! Silakan login.", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.showPanel(MainFrame.LOGIN);

        } catch (IllegalArgumentException ex) {
            lblErr.setText(ex.getMessage());
        } catch (Exception ex) {
            lblErr.setText("Error tidak terduga: " + ex.getMessage());
        }
    }

    @Override
    public void onShow() {
        txtNama.setText("");
        txtEmail.setText("");
        txtAlamat.setText("");
        txtPw.setText("");
        txtPw2.setText("");
        lblErr.setText(" ");
    }
}
