package org.Procesos.servidor;

import javax.swing.*;

public class ServidorInterfaz extends JFrame {
    private JPanel contentPane;
    private JTextArea logArea;

    public ServidorInterfaz() {
        setTitle("Servidor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
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