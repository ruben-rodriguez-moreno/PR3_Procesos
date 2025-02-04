// MainServer.java
package org.Procesos.servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class MainServer {
    private static final Set<String> usernames = new HashSet<>();
    private static final ServidorInterfaz interfaz = new ServidorInterfaz();
    public static void main(String[] args) {
        interfaz.setVisible(true);
        int puerto = 12345;
        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(puerto);
            interfaz.logMessage("Servidor iniciado en el puerto " + puerto);
            while (true) {
                Socket cliente = servidor.accept();
                interfaz.logMessage("Cliente conectado");
                new ClientHandler(cliente).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized boolean addUsername(String username) {
        return usernames.add(username);
    }

    public static synchronized void removeUsername(String username) {
        usernames.remove(username);
    }
}

// ClientHandler.java
class ClientHandler extends Thread {
    private Socket cliente;
    private String username;

    public ClientHandler(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try {
            InputStream inaux = cliente.getInputStream();
            DataInputStream flujo_entrada = new DataInputStream(inaux);
            OutputStream outaux = cliente.getOutputStream();
            DataOutputStream flujo_salida = new DataOutputStream(outaux);

            username = flujo_entrada.readUTF();
            if (!MainServer.addUsername(username)) {
                flujo_salida.writeUTF("Nombre de usuario ya en uso. Desconectando...");
                cliente.close();
                return;
            }

            flujo_salida.writeUTF("Bienvenido al chat");

            while (true) {
                String mensaje = flujo_entrada.readUTF();
                if (mensaje.equals("desconectado")) {
                    break;
                }
            }

            cliente.close();
        } catch (IOException e) {
            System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
        } finally {
            if (username != null) {
                MainServer.removeUsername(username);
            }
        }
    }
}