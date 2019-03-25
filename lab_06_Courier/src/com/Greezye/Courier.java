package com.Greezye;


import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Courier {
    public static void main(String[] args){
        try(Socket socket = new Socket("localhost", 5000)) {
            System.out.println("Client port is " + socket.getLocalPort());
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true
            );

            output.println("Courier");
            System.out.println("connected");
            Scanner reader = new Scanner(System.in);
            String msg = "default";
            String response;
            int op=0;
            do {
                if(op != 0){
                    response = input.readLine();
                    System.out.println(response);
                }
                System.out.println("1 - ready");
                System.out.println("2 - delivered");
                System.out.println("3 - not ready");
                System.out.println("0 - exit");

                op = reader.nextInt();
                while(input.ready()){
                    response = input.readLine();
                    System.out.println(response);
                    System.out.println("1 - ready");
                    System.out.println("2 - delivered");
                    System.out.println("3 - not ready");
                    System.out.println("0 - exit");
                    op = reader.nextInt();
                }
                switch (op){
                    case 1:{
                        System.out.println("Choose available sizes (small,medium,big):");
                        String sizes = reader.next();
                        msg = "ready " + sizes + " " +socket.getLocalPort();
                        output.println(msg);
                        break;
                    }
                    case 2:{
                        output.println("delivered");
                        break;
                    }
                    case 3: {
                        output.println("notready");
                        break;
                    }
                    case 0:{
                        output.println("exit");
                        msg = "exit";
                        break;
                    }
                    default:{
                        output.println("default");
                        break;
                    }
                }

            } while (!msg.equalsIgnoreCase("exit"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}


