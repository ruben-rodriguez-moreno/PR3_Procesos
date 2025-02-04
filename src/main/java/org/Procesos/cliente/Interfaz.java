package org.Procesos.cliente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame {
    private JPanel contentPane;
    private JLabel labelMensajes;
    private JLabel labelUsuario;
    private JLabel labelRegistro;
    private JTextField usernameField;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;

    public Interfaz(String username) {
        setTitle("Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);

        usernameField.setText(username);
        usernameField.setEditable(false);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    messageArea.append(username + ": " + message + "\n");
                    messageField.setText("");
                    // Aquí puedes agregar el código para enviar el mensaje al servidor
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interfaz("Usuario").setVisible(true);
            }
        });
    }

    public void logMessage(String s) {
        messageArea.append(s + "\n");
    }

    public String getMessages() {
        return messageArea.getText();
    }
}