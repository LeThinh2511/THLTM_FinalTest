/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thltm_finaltest.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import thltm_finaltest.model.MessageType;
import thltm_finaltest.model.Room;
import thltm_finaltest.model.Teacher;

/**
 *
 * @author thinhle
 */
public class ClientHandler extends Thread {
    Socket socket;
    ObjectInputStream receiver;
    ObjectOutputStream sender;
    ArrayList<Teacher> teachers = new ArrayList<>();
    ArrayList<Room> rooms = new ArrayList<>();
    boolean isReceivedRoomList = false;
    boolean isReceivedTeacherList = false;
    boolean isRunning = true; 
    
    @Override
    public void run() {
        while (isRunning) {
            if (this.isReceivedRoomList && this.isReceivedTeacherList) {
                System.out.println("handle");
                this.isReceivedRoomList = false;
                this.isReceivedTeacherList = false;
                this.handleData();
            }
            
            try {
                MessageType type = (MessageType) receiver.readObject();
                System.out.println("server: " + type);
                switch (type) {
                    case teacherList:
                        System.out.println("server: 1");
                        teachers = (ArrayList<Teacher>) receiver.readObject();
                        this.isReceivedTeacherList = true;
                        break;
                    case roomList:
                        System.out.println("server: 2");
                        this.isReceivedRoomList = true;
                        rooms = (ArrayList<Room>) receiver.readObject();
                        break;
                    case isFinished:
                        System.out.println("server: 3");
                        boolean isFinished = (boolean) receiver.readObject();
                        this.isRunning = !isFinished;
                        System.out.println("End program");
                        break;
                    default:
                        System.out.println("Can not recognize data type!");
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void handleData() {
        if (this.rooms.size() * 2 > this.teachers.size()) {
            this.sendMessageToClient(MessageType.string, "Number of teacher must be at least two time more than number of rooms");
           return;
        }
        Random random = new Random();
        int index = 0;
        for (Room room : this.rooms) {
            index = random.nextInt(this.teachers.size());
            room.setFirstTeacher(this.teachers.remove(index));
            index = random.nextInt(this.teachers.size());
            room.setSecondTeacher(this.teachers.remove(index));
        }
        this.sendMessageToClient(MessageType.roomList, this.rooms);
    }

    public ClientHandler(Socket socket, ObjectInputStream receiver, ObjectOutputStream sender) {
        this.socket = socket;
        this.receiver = receiver;
        this.sender = sender;
    }
    
    public <T> void sendMessageToClient(MessageType type, T data) {
        try {
            this.sender.writeObject(type);
            this.sender.writeObject(data);
        } catch (Exception e) {
            System.out.print("Send data Error: ");
            System.out.println(e.toString());
        }
    }
}
