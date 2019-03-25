package com.Greezye;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DispatcherThread extends Thread{

    private final int serverSocket;
    public final List requestList = new ArrayList();
    public final List<ClientCourierThread> courierList = new ArrayList<>();
    public final List<ClientCourierThread> clientList = new ArrayList<>();
    public final List<ClientCourierThread> readyCourierList = new ArrayList<>();
    public final List<Package> storageList = new ArrayList<>();

    public DispatcherThread(int serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(this.serverSocket);
            while (true){
                Socket clientSocked = serverSocket.accept();
                System.out.println("Connection accepted");
                ClientCourierThread clientCourierThread = new ClientCourierThread(this,clientSocked);
                clientCourierThread.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getServerSocket() {
        return serverSocket;
    }

    public List getRequestList() {
        return requestList;
    }

    public List<ClientCourierThread> getCourierList() {
        return courierList;
    }

    public List<ClientCourierThread> getClientList() {
        return clientList;
    }

    public List<ClientCourierThread> getReadyCourierList() {
        return readyCourierList;
    }
}
