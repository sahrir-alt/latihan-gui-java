import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Latihan GUI Java Swing - To-Do List
 *
 * Komponen yang digunakan:
 * 1. Top-level Container      : JFrame
 * 2. Intermediate Container   : JPanel, JScrollPane
 * 3. Atomic Component         : JLabel, JTextField, JButton, JList, JCheckBox
 * 4. Layout Classes           : BorderLayout, FlowLayout, GridLayout
 * 5. Event Listener           : ActionListener (interface),
 *                               KeyAdapter, MouseAdapter, WindowAdapter (adapter class)
 */
public class TodoListGUI extends JFrame {

    private DefaultListModel<String> model;
    private JList<String> listTugas;
    private JTextField txtTugas;
    private JButton btnTambah, btnHapus, btnSelesai, btnBersihkan;
    private JCheckBox chkKonfirmasi;
    private JLabel lblStatus;

    public TodoListGUI() {
        // ===== 1. Top-level Container =====
        setTitle("To-Do List");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== 2. Intermediate Container + 4. Layout =====
        JPanel panelUtama = new JPanel(new BorderLayout(8, 8));          // BorderLayout
        panelUtama.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel input (FlowLayout)
        JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        txtTugas = new JTextField(20);
        btnTambah = new JButton("Tambah");
        panelInput.add(new JLabel("Tugas:"));
        panelInput.add(txtTugas);
        panelInput.add(btnTambah);

        // Daftar tugas
        model = new DefaultListModel<>();
        listTugas = new JList<>(model);
        listTugas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(listTugas);

        // Panel tombol aksi (GridLayout 2x2)
        JPanel panelAksi = new JPanel(new GridLayout(2, 2, 5, 5));       // GridLayout
        btnSelesai = new JButton("Tandai Selesai");
        btnHapus = new JButton("Hapus");
        btnBersihkan = new JButton("Bersihkan Semua");
        chkKonfirmasi = new JCheckBox("Konfirmasi sebelum hapus", true);
        panelAksi.add(btnSelesai);
        panelAksi.add(btnHapus);
        panelAksi.add(btnBersihkan);
        panelAksi.add(chkKonfirmasi);

        // Panel bawah: aksi + status
        lblStatus = new JLabel("Total tugas: 0");
        JPanel panelBawah = new JPanel(new BorderLayout(5, 5));
        panelBawah.add(panelAksi, BorderLayout.CENTER);
        panelBawah.add(lblStatus, BorderLayout.SOUTH);

        panelUtama.add(panelInput, BorderLayout.NORTH);
        panelUtama.add(scroll, BorderLayout.CENTER);
        panelUtama.add(panelBawah, BorderLayout.SOUTH);
        add(panelUtama);

        // ===== 5. Event Listener =====

        // (a) ActionListener (interface) - override actionPerformed
        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahTugas();
            }
        });

        btnSelesai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = listTugas.getSelectedIndex();
                if (i >= 0 && !model.get(i).startsWith("[x] ")) {
                    model.set(i, "[x] " + model.get(i));
                }
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = listTugas.getSelectedIndex();
                if (i < 0) {
                    JOptionPane.showMessageDialog(TodoListGUI.this, "Pilih tugas dulu!");
                    return;
                }
                if (chkKonfirmasi.isSelected()) {
                    int ok = JOptionPane.showConfirmDialog(TodoListGUI.this,
                            "Hapus tugas ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (ok != JOptionPane.YES_OPTION) return;
                }
                model.remove(i);
                updateStatus();
            }
        });

        btnBersihkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
                updateStatus();
            }
        });

        // (b) KeyAdapter - tekan Enter untuk menambah tugas
        txtTugas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    tambahTugas();
                }
            }
        });

        // (c) MouseAdapter - double click pada item untuk menandai selesai
        listTugas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    btnSelesai.doClick();
                }
            }
        });

        // (d) WindowAdapter - konfirmasi saat menutup jendela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int ok = JOptionPane.showConfirmDialog(TodoListGUI.this,
                        "Keluar dari aplikasi?", "Keluar", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });
    }

    private void tambahTugas() {
        String teks = txtTugas.getText().trim();
        if (!teks.isEmpty()) {
            model.addElement(teks);
            txtTugas.setText("");
            updateStatus();
        }
    }

    private void updateStatus() {
        lblStatus.setText("Total tugas: " + model.size());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoListGUI().setVisible(true));
    }
}
