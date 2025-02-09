package org.Procesos.UDP.cliente;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainCliente {
    private static final InterfazUsu interfaz2 = new InterfazUsu();
    private static final int puerto = 12345;

    public static void main(String[] args) {
        boolean empezamos = empezar();
        if (empezamos) {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
                InetAddress address = InetAddress.getLocalHost();
                Interfaz interfaz = new Interfaz(interfaz2.getUser(), socket, address, puerto);
                interfaz.setVisible(true);
            } catch (IOException e) {
                System.err.println("Error de conexión: " + e.getMessage());
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        }
    }

    private static boolean empezar() {
        interfaz2.setVisible(true);
        while (!interfaz2.isOpen()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        interfaz2.setVisible(false);
        return true;
    }
}