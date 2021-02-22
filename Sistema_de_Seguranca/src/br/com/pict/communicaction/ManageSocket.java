package br.com.pict.communicaction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Iarley
 */
public class ManageSocket implements Runnable {

    Socket socket;
    DataOutputStream dataOutputS;
    boolean sendRecive = true;
    int id;
    byte[] image;
    String whoIs = "APP_Manage";
    

    public ManageSocket() {
        try {
            socket = new Socket("10.10.10.1", 3322);
            dataOutputS = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ManageSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ManageSocket(byte[] image) {
        try {
            socket = new Socket("10.10.10.1", 3322);
            dataOutputS = new DataOutputStream(socket.getOutputStream());
            this.image = image;
        } catch (IOException ex) {
            Logger.getLogger(ManageSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                dataOutputS.writeUTF(whoIs);
                dataOutputS.flush();
                break;
            } catch (IOException ex) {
                Logger.getLogger(ManageSocket.class.getName()).log(Level.SEVERE, null, ex);
                close();
            }finally{
                close();
            }
        }
    }

    private void close() {
        try {
            dataOutputS.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ManageSocket.class.getName()).log(Level.SEVERE, null, ex);
            close();
        }
    }
}
