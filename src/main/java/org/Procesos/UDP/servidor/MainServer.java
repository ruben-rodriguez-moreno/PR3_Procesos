package org.Procesos.UDP.servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainServer {
    private static final int PORT = 12345;
    private static final ArrayList<String> historial = new ArrayList<>();
    private static final Set<InfoCliente> clientes = new HashSet<>();
    private static final Set<String> usuarios = new HashSet<>();
    private static final VentanaServidor ventana = new VentanaServidor();

    public static void main(String[] args) {
        ventana.setVisible(true);
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            ventana.logMessage("UDP server iniciado en: " + PORT);
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);
                new Thread(new Manejador(packet, serverSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Manejador implements Runnable {
        private final DatagramPacket packet;
        private final DatagramSocket serverSocket;
        private String usuario;

        public Manejador(DatagramPacket packet, DatagramSocket serverSocket) {
            this.packet = packet;
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {
                String message = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                if (message.startsWith("USUARIO:")) {
                    usuario = message.split(":")[1].trim();
                    if (usuarios.contains(usuario)) {
                        mensajeCliente("El nombre de usuario ya está en uso.", address, port);
                    } else {
                        usuarios.add(usuario);
                        clientes.add(new InfoCliente(address, port));
                        ventana.logMessage("Usuario conectado: " + usuario);
                        mensajeCliente(usuario + ", bienvenid@", address, port);
                        for (String msg : historial) {
                            mensajeCliente(msg, address, port);
                        }
                        mensajeATodos("***" + usuario + " se ha unido al chat***");
                    }
                } else {
                    mensajeATodos(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void mensajeCliente(String message, InetAddress address, int port) throws IOException {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            serverSocket.send(packet);
        }

        private void mensajeATodos(String message) throws IOException {
            for (InfoCliente client : clientes) {
                mensajeCliente(message, client.getAddress(), client.getPort());
                historial.add(message);
            }
        }
    }

    private static class InfoCliente {
        private final InetAddress address;
        private final int port;

        public InfoCliente(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }
    }
}