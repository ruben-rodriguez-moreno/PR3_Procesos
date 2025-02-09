package org.Procesos.TCP.cliente;

import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazUsu extends JFrame {
    private JPanel contentPane;
    private JTextField textField1;
    private JLabel labelUsuario;
    private JButton buttonSend;
    private Boolean isOpen = false;
    @Getter
    private String user;

    public InterfazUsu() {
        setTitle("Nombre de Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        setSize(200, 200);
        pack();
        setLocationRelativeTo(null);

        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textField1.getText();
                if (!username.isEmpty()) {
                    user = username;
                    isOpen = true;
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Por favor, ingrese un nombre de usuario.");
                }
            }
        });
    }

    public boolean isOpen() {
        return isOpen;
    }

}