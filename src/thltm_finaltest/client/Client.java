/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thltm_finaltest.client;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import thltm_finaltest.model.MessageType;
import thltm_finaltest.model.Room;

/**
 *
 * @author thinhle
 */
public class Client extends javax.swing.JFrame {
    Socket socket = null;
    int port = 0;
    String host = "";
    ObjectOutputStream sender = null;
    ObjectInputStream receiver = null;
    Thread listener;
    boolean isRunning;
    FileBrowser fileBrowser;
    /**
     * Creates new form Client
     */
    public Client() {
        initComponents();
        this.getRootPane().setDefaultButton(this.connectButton);
    }
    
    private void start() {
        this.isRunning = true;
        this.listener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        MessageType type = (MessageType) receiver.readObject();
                        System.out.println("type: " + type);
                        switch (type) {
                            case string:
                                System.out.println("client: 1");
                                String messageFromServer = (String) receiver.readObject();
                                messageLabel.setText(messageFromServer);
                                if (fileBrowser != null) {
                                    fileBrowser.setMessage(messageFromServer);
                                }
                                break;
                            case roomList:
                                System.out.println("client: 2");
                                ArrayList<Room> rooms = (ArrayList<Room>) receiver.readObject();
                                fileBrowser.writeFile(rooms);
                                break;
                            default:
                                System.out.println("Can not cast data from socket to expect message type!");
                                break;
                        }
                    } catch (IOException ex) {
                        System.out.println("IO Exception: From client: " + ex.getMessage());
                        System.out.println("caused by: " + ex.getCause().toString());
                    } catch (ClassNotFoundException ex) {
                        System.out.println("Class Not Found Exception: From client");
                        ex.printStackTrace();
                    }
                }
            }
        });
        this.listener.start();
        this.fileBrowser = new FileBrowser();
        this.fileBrowser.setConnection(socket, sender, receiver);
        fileBrowser.setVisible(true);
        this.setVisible(false);
    }

    public <T> void sendMessageToServer(MessageType type, T data) {
        try {
            sender.writeObject(type);
            sender.writeObject(data);
        } catch (Exception e) {
            System.out.print("Send data Error: Client");
            System.out.println(e.toString());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ipTextField = new javax.swing.JTextField();
        portTextField = new javax.swing.JTextField();
        messageLabel = new javax.swing.JLabel();
        connectButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("IP:");

        jLabel2.setText("Port:");

        ipTextField.setText("localhost");
        ipTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ipTextFieldActionPerformed(evt);
            }
        });

        portTextField.setText("5555");

        messageLabel.setForeground(new java.awt.Color(0, 0, 255));
        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageLabel.setText("Client");

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(211, 211, 211)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(messageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(351, 351, 351)
                        .addComponent(connectButton)))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addComponent(messageLabel)
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(connectButton)
                .addContainerGap(151, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ipTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ipTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ipTextFieldActionPerformed

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        String portString = this.portTextField.getText();
        String host = this.ipTextField.getText();
        try {
            int port = Integer.parseInt(portString);
            this.socket = new Socket(host, port);
            this.messageLabel.setForeground(Color.blue);
            this.messageLabel.setText("Connected to server!");
            this.sender = new ObjectOutputStream(this.socket.getOutputStream());
            this.receiver = new ObjectInputStream(this.socket.getInputStream());
            this.connectButton.setEnabled(false);
            this.host = host;
            this.port = port;
            this.start();
        } catch (Exception e) {
            System.out.println(e);
            this.messageLabel.setText("Failed to connect to server: " + host + " with port: " + portString);
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JTextField ipTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextField portTextField;
    // End of variables declaration//GEN-END:variables
}
