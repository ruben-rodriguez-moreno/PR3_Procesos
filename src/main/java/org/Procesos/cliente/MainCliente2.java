package org.Procesos.cliente;

import org.Procesos.entities.UserEntity;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class MainCliente2 {
    private static final Scanner sc = new Scanner(System.in);
    private static final InterfazUsu interfaz2 = new InterfazUsu();
    public static void main(String[] args) {
        boolean empezamos = empezar();
        while (empezamos) {
            Interfaz interfaz = new Interfaz(interfaz2.getUser());
            interfaz.setVisible(true);
            UserEntity user = new UserEntity();
            user.setUsername(interfaz2.getUser());
            int puerto = 12345;
            Socket sCliente = null;
            while (true) {
                try {
                    sCliente = new Socket(InetAddress.getLocalHost(), puerto);
                    // Streams
                    InputStream inaux = sCliente.getInputStream();
                    DataInputStream flujo_entrada = new DataInputStream(inaux);
                    OutputStream outaux = sCliente.getOutputStream();
                    DataOutputStream flujo_salida = new DataOutputStream(outaux);
                    flujo_salida.writeUTF(user.getUsername());
                    flujo_salida.writeUTF("Conectado en el puerto: " + puerto + " con el usuario: " + user.getUsername());
                    String mensaje = flujo_entrada.readUTF();
                    interfaz.logMessage(mensaje);
                    String respuesta;
                    if (mensaje.equals("Nombre de usuario ya en uso. Desconectando...")) {
                        sCliente.close();
                    } else {
                        // Dialogar
                        do {
                            mensaje = interfaz.getMessages();
                            flujo_salida.writeUTF(user.getUsername() + ": " + mensaje);
                            respuesta = flujo_entrada.readUTF();
                            interfaz.logMessage(respuesta);
                        } while (!respuesta.equals("desconectado"));
                    }
                    // Cerrar
                    sCliente.close();
                    break; // Salir del bucle si la conexión fue exitosa
                } catch (IOException e) {
                    interfaz.logMessage("Error de conexión: " + e.getMessage());
                    interfaz.logMessage("Reintentando conexión en 5 segundos...");
                    try {
                        Thread.sleep(5000); // Esperar 5 segundos antes de reintentar
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } finally {
                    if (sCliente != null && !sCliente.isClosed()) {
                        try {
                            sCliente.close();
                        } catch (IOException e) {
                            interfaz.logMessage("Error al cerrar el socket: " + e.getMessage());
                        }
                    }
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
        return true;
    }
}