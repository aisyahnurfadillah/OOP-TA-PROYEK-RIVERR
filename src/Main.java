/**
 * Entry point aplikasi RiverR.
 *
 * Tugas file ini hanya dua:
 *  1. Inisialisasi DataStore (Singleton) sebelum UI tampil
 *  2. Menjalankan MainFrame di Event Dispatch Thread (EDT)
 *
 * Dipisah dari MainFrame agar MainFrame murni mengurus UI,
 * dan Main murni mengurus startup.
 *
 * @author Dila (Role 4 - QA & Repo Master)
 */
public class Main {

    public static void main(String[] args) {

        // 1. Inisialisasi DataStore SEBELUM UI dibuat
        //    getInstance() otomatis memanggil seedData() di constructor
        services.DataStore.getInstance();

        // 2. Jalankan UI di Event Dispatch Thread (EDT)
        //    Swing tidak thread-safe — wajib pakai invokeLater
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {}

            new views.MainFrame().setVisible(true);
        });
    }
}