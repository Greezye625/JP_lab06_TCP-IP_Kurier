package com.Greezye;



public class Main {


    public static void main(String[] args) {
        int port = 5000;
        DispatcherThread dispatcher = new DispatcherThread(port);
        dispatcher.start();
    }
}
