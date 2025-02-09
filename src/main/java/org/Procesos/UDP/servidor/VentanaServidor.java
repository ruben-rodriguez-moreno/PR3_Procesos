package org.Procesos.UDP.servidor;

import javax.swing.*;

public class VentanaServidor extends JFrame {
    private JPanel contentPane;
    private JTextArea logArea;

    public VentanaServidor() {
        setTitle("Servidor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        setSize(400, 400);
        setPreferredSize(getSize());
        setAlwaysOnTop(true);
        pack();
        setLocationRelativeTo(null);
    }

    public void logMessage(String message) {
        logArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaServidor().setVisible(true);
            }
        });
    }
}
