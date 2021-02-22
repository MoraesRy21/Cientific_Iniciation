package br.com.pict.communication;

import Presentation.MainView.ControllerIA;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Iarley
 */
public class ManageServer {

    ServerSocket serverSocket;
    ExecutorService executorService = Executors.newFixedThreadPool(3);

    private ControllerIA controllerIA;

    public ManageServer() {
        try {
            serverSocket = new ServerSocket(3322);
            
        } catch (IOException ex) {
            ex.printStackTrace();
            closeServerSocket();
        }
    }

    public ManageServer(ControllerIA controllerIA) {
        try {
            serverSocket = new ServerSocket(3322);
            this.controllerIA = controllerIA;

        } catch (IOException ex) {
            ex.printStackTrace();
            closeServerSocket();
        }
    }
    
    public void makeConnection(){
        executorService.execute(new ManageSocketServer(serverSocket, controllerIA));
        executorService.execute(new ManageSocketServer(serverSocket, controllerIA));
        executorService.execute(new ManageSocketServer(serverSocket, controllerIA));
        executorService.execute(new ManageSocketServer(serverSocket, controllerIA));
    }
    
    public void shutDown(){
        executorService.shutdown();
    }

    public void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
