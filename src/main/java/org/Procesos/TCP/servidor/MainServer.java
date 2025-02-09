// MainServer.java
package org.Procesos.TCP.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainServer {

    public static final ServidorInterfaz interfaz = new ServidorInterfaz();

    public static void main(String[] args) {
        interfaz.setVisible(true);
        int puerto = 12345;
        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(puerto);
            interfaz.logMessage("Servidor iniciado en " + servidor.getLocalPort());
            while (true) {
                Socket cliente = servidor.accept();
                interfaz.logMessage("Cliente conectado: " + cliente.getInetAddress());
                Thread thread = new Thread(new Manejador(cliente));
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static class Manejador implements Runnable {
        private static final ArrayList<String> historial = new ArrayList<>();
        private static final ArrayList<DataOutputStream> clientes = new ArrayList<>();
        private final Socket cliente;
        private final Set<String> usuarios = new HashSet<>();
        private DataInputStream in;
        private DataOutputStream out;
        private String usuario;

        public Manejador(Socket cliente) {
            this.cliente = cliente;
            try {
                in = new DataInputStream(cliente.getInputStream());
                out = new DataOutputStream(cliente.getOutputStream());
                synchronized (clientes) {
                    clientes.add(out);
                    if (historial.isEmpty()) {
                        historial.addFirst("INICIO DEL CHAT");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = in.readUTF();
                    if (message.startsWith("USUARIO:")) {
                        usuario = message.split(":")[1].trim();
                        if (usuarios.contains(usuario)) {
                            out.writeUTF("El nombre de usuario ya está en uso.");
                            return;
                        } else {
                            usuarios.add(usuario);
                            interfaz.logMessage("Usuario conectado: " + usuario);
                            out.writeUTF(usuario + ", bienvenid@");
                            synchronized (clientes) {
                                for (DataOutputStream textClientes : clientes) {
                                    if (textClientes == out) {
                                        for (String mensaje : historial) {
                                            textClientes.writeUTF(mensaje);
                                        }
                                    }
                                    textClientes.writeUTF("***" + usuario + " se ha unido al chat***");
                                }
                            }
                        }
                    } else {
                        synchronized (clientes) {
                            for (DataOutputStream textClientes : clientes) {
                                textClientes.writeUTF(usuario + ": " + message);
                                historial.add(usuario + ": " + message);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (usuario != null) {
                    usuarios.remove(usuario);
                    interfaz.logMessage("Usuario desconectado: " + usuario);
                }
                try {
                    cliente.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

