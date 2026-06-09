package views;

/**
 * Interface untuk panel yang me-refresh datanya setiap kali ditampilkan.
 * Diimplementasi oleh semua panel Swing.
 *
 * KONSEP OOP: Interface — kontrak wajib dipenuhi implementor
 *
 * @author Stipen (Role 3 - UI & Robustness Engineer)
 */
public interface Refreshable {
    /**
     * Dipanggil otomatis oleh MainFrame.showPanel() sebelum panel tampil.
     * Gunakan untuk: reload data, reset form, update label.
     */
    void onShow();
}
