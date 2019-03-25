package com.Greezye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    try(Socket socket = new Socket("localhost", 5000)) {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true
            );

            output.println("Client");
            System.out.println("connected");
            Scanner reader = new Scanner(System.in);
            String msg;
            String response;
            int op=0;
            do{
                if(op != 0){
                    response = input.readLine();
                    System.out.println(response);
                }
                System.out.println("1 - Zamow Paczke");
                System.out.println("0 - Wyjdz");

                op = reader.nextInt();
                while(input.ready()){
                    response = input.readLine();
                    System.out.println(response);
                    System.out.println("1 - Zamow Paczke");
                    System.out.println("0 - Wyjdz");
                    op = reader.nextInt();
                }
                switch (op){
                    case 1:{
                        System.out.println("choose size");
                        String size = reader.next();
                        msg = "requestPackage " + size + " " + socket.getLocalPort();
                        output.println(msg);
                        break;
                    }
                    case 0:{
                        msg = "exit";
                        output.println(msg);
                        break;
                    }
                    default:{
                        msg="default";
                        break;
                    }
                }

            }while (!msg.equals("exit"));
        }
        catch (IOException e){
            System.out.println("Client error: " + e.getMessage());
        }

    }
}
