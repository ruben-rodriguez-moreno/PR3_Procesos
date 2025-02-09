package org.Procesos.TCP.servidor;

import javax.swing.*;

public class ServidorInterfaz extends JFrame {
    private JPanel contentPane;
    private JTextArea logArea;

    public ServidorInterfaz() {
        setTitle("Servidor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        setSize(500, 500);
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
                new ServidorInterfaz().setVisible(true);
            }
        });
    }
}