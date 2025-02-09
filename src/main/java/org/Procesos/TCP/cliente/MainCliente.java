package org.Procesos.TCP.cliente;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainCliente {
    private static final InterfazUsu interfaz2 = new InterfazUsu();

    public static void main(String[] args) {
        boolean empezamos = empezar();
        if (empezamos) {
            int puerto = 12345;
            try {
                Socket sCliente = new Socket(InetAddress.getLocalHost(), puerto);
                Interfaz interfaz = new Interfaz(interfaz2.getUser(), sCliente);
                interfaz.setVisible(true);
            } catch (IOException e) {
                System.err.println("Error de conexión: " + e.getMessage());
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