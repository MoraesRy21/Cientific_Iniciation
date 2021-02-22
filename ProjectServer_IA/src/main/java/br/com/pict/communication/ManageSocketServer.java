package br.com.pict.communication;

import Presentation.MainView.ControllerIA;
import Presentation.MainView.Recoganize;
import Presentation.MainView.Training;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Iarley
 */
public class ManageSocketServer implements Runnable {
     
     private ServerSocket ss;
     private Socket socket;
     private String whoIs;
     
     private DataInputStream inputStream;
     private BufferedInputStream bufferedInputStream;
     
     private DataOutputStream outputStream;
     
     private ControllerIA controllerIA;
     
     public ManageSocketServer(ServerSocket ss, ControllerIA controllerIA) {
          this.ss = ss;
          this.controllerIA = controllerIA;
     }
     
     @Override
     public void run() {
          while (true) {
               try {
                    System.out.println("======== BEFORE ACCEPT ========");
                    socket = ss.accept();
                    System.out.println("========= AFTER ACCEPT ========");
                    
                    inputStream = new DataInputStream(socket.getInputStream());
                    whoIs = inputStream.readUTF();
                    
                    if (whoIs.equals("APP_Validation")) {
                         int imageLength = inputStream.readInt();
                         while(inputStream.available() < imageLength){ }
                         System.out.println("Tamanho do InpStream fora do while: "+inputStream.available());
                         ByteArrayOutputStream baos = new ByteArrayOutputStream();
                         byte buffer[] = new byte[1024 * 128];
                         baos.write(buffer, 0, inputStream.read(buffer));
                         byte[] image = baos.toByteArray();
                         
                         Callable<Integer> c = new Recoganize(controllerIA, image);
                         
                         Future<Integer> future = ControllerIA.executorServiceRecoganize.submit(c);
                         Integer id = future.get(); //Bloqueante
                         System.out.println("Id Recoganize = " + id);
                         
                         outputStream = new DataOutputStream(socket.getOutputStream());
                         outputStream.writeInt(id);
                         
                    } else if (whoIs.equals("APP_Manage")) {
                         ControllerIA.executorServiceTrainig.execute(new Training(controllerIA));

                    } else {
                         outputStream = new DataOutputStream(socket.getOutputStream());
                         outputStream.writeInt(-2);
                    }
               } catch (IOException ex) {
                    Logger.getLogger(ManageSocketServer.class.getName()).log(Level.SEVERE, null, ex);
               } catch (InterruptedException ex) {
                    Logger.getLogger(ManageSocketServer.class.getName()).log(Level.SEVERE, null, ex);
               } catch (ExecutionException e) {
                    e.printStackTrace();
               } finally {
                    stop();
               }
          }
     }
     
     public void stop() {
          try {
               if(!whoIs.equals("APP_Manage"))
                    outputStream.close();
               inputStream.close();
               socket.close();
          } catch (IOException ex) {
               ex.printStackTrace();
          }
     }
}
