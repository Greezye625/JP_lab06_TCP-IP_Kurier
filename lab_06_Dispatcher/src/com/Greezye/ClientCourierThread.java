package com.Greezye;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientCourierThread extends Thread {
    private final Socket clientSocket;
    private final DispatcherThread dispatcherThread;
    private OutputStream outputStream;
    private String portName;
    private String currentPackage;
    private boolean ready = false;
    private List<String> sizesList = new ArrayList<>();
    private boolean hasPackage = false;


    public ClientCourierThread(DispatcherThread dispatcherThread, Socket clientSocket){
        this.dispatcherThread = dispatcherThread;
        this.clientSocket = clientSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try {
                handleClientSocket();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        BufferedReader input = new BufferedReader(
                new InputStreamReader(inputStream)
        );


        String op = input.readLine();


        this.portName = String.valueOf(this.clientSocket.getPort());

        switch (op){
            case "Client": {
                dispatcherThread.clientList.add(this);
                System.out.println("client added");
                handleclient();
                break;
            }
            case "Courier": {
                dispatcherThread.courierList.add(this);
                System.out.println("courier added");
                handlecourier();
                break;
            }
        }

    }

    private void handlecourier() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();
        BufferedReader input = new BufferedReader(
                new InputStreamReader(inputStream)
        );

        PrintWriter output = new PrintWriter(outputStream, true);


        String op;
        do {
            String line = input.readLine();
            String[] lineParts = StringUtils.split(line);
            op = lineParts[0];
            switch (op) {
                case ("ready"): {
                    dispatcherThread.readyCourierList.add(this);
                    String[]sizesParts = lineParts[1].split(",");
                    Collections.addAll(sizesList, sizesParts);
                    ready = true;
                    output.println("you are registered as ready courier id: " + String.valueOf(portName));
                    System.out.println("courier " + portName + " registered as ready");
                    for(int i=0;i<dispatcherThread.storageList.size();i++){
                        for(int j=0; j<sizesList.size();j++){
                            if(dispatcherThread.storageList.get(i).getSize().equalsIgnoreCase(sizesList.get(j))){
                                for(int k=0; k<dispatcherThread.clientList.size(); k++){
                                    if(dispatcherThread.clientList.get(k).portName.equals(dispatcherThread.storageList.get(i).getClientnadwaca())){
                                        dispatcherThread.clientList.get(k).send("your package was given to courier: " + portName);
                                        output.println("you are holding a package: " + dispatcherThread.storageList.get(i).getClientnadwaca());
                                        System.out.println("package "+ dispatcherThread.storageList.get(i).getClientnadwaca() +
                                                " given to courier " + portName);
                                        currentPackage = String.valueOf(dispatcherThread.storageList.get(i).getClientnadwaca());
                                    }
                                }
                                dispatcherThread.storageList.remove(i);
                                dispatcherThread.readyCourierList.remove(this);
                                ready = false;
                                hasPackage = true;
                                break;
                            }
                        }
                    }
                    break;
                }
                case ("delivered"): {
                    output.println("you delivered your package");
                    for(int i=0; i<dispatcherThread.clientList.size(); i++){
                        if (currentPackage.equals(dispatcherThread.clientList.get(i).portName)){
                            dispatcherThread.clientList.get(i).send("your package was delivered");
                            System.out.println("courier " + portName + " deivered his package");
                            hasPackage = false;
                            ready = false;
                        }
                    }
                    break;
                }
                case ("notready"): {
                    if(!hasPackage){
                        dispatcherThread.readyCourierList.remove(this);
                        ready = false;
                        sizesList.clear();
                        output.println("you have been removed from list of ready couriers");
                        System.out.println("courier " + portName + " registered as not ready");
                    }
                    else{
                        output.println("you can't withdraw while holding a package");
                    }
                    break;
                }
                case("exit"): {
                    if(hasPackage){
                        output.println("you can't quit while holding a package");
                        op="default";
                    }
                    break;
                }
                default:break;
            }
        }while (!op.equalsIgnoreCase("exit"));
        clientSocket.close();
    }



    private void handleclient() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();
        BufferedReader input = new BufferedReader(
                new InputStreamReader(inputStream)
        );


        PrintWriter output = new PrintWriter(outputStream, true);

        String op;


        do{
            String line = input.readLine();
            String[] lineParts = StringUtils.split(line);
            op = lineParts[0];
            String courierid;
            switch (op){
                case("requestPackage"): {
                    int ctrl = 0;
                    for(int i=0; i<dispatcherThread.readyCourierList.size();i++){
                        for(int j=0 ; j<dispatcherThread.readyCourierList.get(i).getSizesList().size(); j++){
                            if(lineParts[1].equalsIgnoreCase(dispatcherThread.readyCourierList.get(i).getSizesList().get(j))){
                                dispatcherThread.readyCourierList.get(i).hasPackage = true;
                                courierid = dispatcherThread.readyCourierList.get(i).portName;
                                for (int k=0; k<dispatcherThread.getCourierList().size(); k++ ){
                                    if(dispatcherThread.getCourierList().get(i).portName.equals(courierid)){
                                        dispatcherThread.getCourierList().get(i).send("you are holding a package: " + clientSocket.getPort());
                                        dispatcherThread.getCourierList().get(i).currentPackage = String.valueOf(portName);
                                        dispatcherThread.getCourierList().get(i).ready = false;
                                        dispatcherThread.getCourierList().get(i).hasPackage = true;
                                    }
                                }
                                output.println("your package have been taken by courier: " + courierid);
                                System.out.println("Package from client " + portName + "given to courier" + courierid);
                                dispatcherThread.readyCourierList.remove(i);
                                ctrl =1;
                                break;
                            }
                        }
                    }
                    if (ctrl == 0){
                        if(dispatcherThread.storageList.size()<5){
                            Package parcel = new Package(portName,lineParts[1]);
                            dispatcherThread.storageList.add(parcel);
                            output.println("your package is in our storage");
                            System.out.println("package from client " + portName + "stored in storage");
                        }
                        else{
                            output.println("sorry we cannot handle your package");
                            System.out.println("unnable to accept a package");
                        }
                    }
                    break;
                }
                case("exit"):{
                    break;
                }
                default:break;
            }
        }while (!op.equalsIgnoreCase("exit"));
        clientSocket.close();
    }

    private void send(String msg) {
        PrintWriter output = new PrintWriter(outputStream, true);
        output.println(msg);
    }

    public List<String> getSizesList() {
        return sizesList;
    }
}
