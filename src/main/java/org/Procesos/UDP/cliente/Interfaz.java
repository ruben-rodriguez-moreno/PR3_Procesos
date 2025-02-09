package org.Procesos.UDP.cliente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Interfaz extends JFrame {
    private JPanel contentPane;
    private JLabel labelMensajes;
    private JLabel labelUsuario;
    private JLabel labelRegistro;
    private JTextField usernameField;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;
    private DatagramSocket socket;
    private InetAddress address;
    private int puerto;
    private String usuario;

    public Interfaz(String usuario, DatagramSocket socket, InetAddress address, int puerto) {
        this.usuario = usuario;
        this.socket = socket;
        this.address = address;
        this.puerto = puerto;
        setTitle("Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        setSize(200, 200);
        pack();
        setLocationRelativeTo(null);

        usernameField.setText(usuario);
        usernameField.setEditable(false);
        try {
            mensajeInicial(usuario);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        new Thread(new RecibirMensajes()).start();
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    try {
                        enviarMensaje(message);
                        messageField.setText("");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    private void mensajeInicial(String usuario) throws IOException {
        String fullMessage = "USUARIO:" + usuario;
        byte[] buffer = fullMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, puerto);
        socket.send(packet);
    }

    private void enviarMensaje(String message) throws IOException {
        String fullMessage = usuario + ": " + message;
        byte[] buffer = fullMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, puerto);
        socket.send(packet);
    }

    private class RecibirMensajes implements Runnable {
        @Override
        public void run() {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    messageArea.append(message + "\n");
                }
            } catch (IOException e) {
                System.out.println("Conexión cerrada: " + socket.getLocalAddress());
            }
        }
    }
}