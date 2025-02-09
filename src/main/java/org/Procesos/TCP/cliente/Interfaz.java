package org.Procesos.TCP.cliente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Interfaz extends JFrame {
    private JPanel contentPane;
    private JLabel labelMensajes;
    private JLabel labelUsuario;
    private JLabel labelRegistro;
    private JTextField usernameField;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;

    public Interfaz(String username, Socket sCliente) {
        this.socket = sCliente;
        setTitle("Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        setSize(200, 200);
        pack();
        setLocationRelativeTo(null);

        usernameField.setText(username);
        usernameField.setEditable(false);


        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("USUARIO:" + username);
            GetMensajes mensajes = new GetMensajes();
            new Thread(mensajes).start();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    try {
                        out.writeUTF(message);
                        messageField.setText("");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });
    }

    public void logMessage(String message) {
        messageArea.append(message + "\n");
    }

    public String getMessages() {
        return messageField.getText();
    }

    private class GetMensajes implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    messageArea.append(in.readUTF() + "\n");
                }
            } catch (IOException e) {
                System.out.println("Conexión cerrada: " + socket.getInetAddress());
            }
        }
    }
}
