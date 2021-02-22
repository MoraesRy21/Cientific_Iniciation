package br.com.pict.communication;

import br.com.pict.util.ConfigFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 *
 * @author Iarley
 */
public class ManageSocket implements Callable<Integer> {

    private String whoIs = "APP_Validation";
    private boolean sendRecive = true;
    private Socket socket;

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private byte[] imageByteArray;

    public ManageSocket(byte[] image) throws IOException {
        socket = new Socket(ConfigFactory.connectionVPS.get("vps_2_directAccses_ip"), 3322);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
        this.imageByteArray = image;
    }

    @Override
    public Integer call() throws Exception {
        Integer id = null;
        try {
            while (true) {
                if (sendRecive) {
                    dataOutputStream.writeUTF(whoIs);
                    dataOutputStream.flush();
                    System.out.println("Tamanho da imagem: "+ imageByteArray.length);
                    dataOutputStream.writeInt(imageByteArray.length);
                    dataOutputStream.flush();
                    dataOutputStream.write(imageByteArray);
//                    dataOutputStream.flush();
                    sendRecive = false;
                } else {
                    if (dataInputStream.available() != 0) {
                        id = dataInputStream.readInt();
                        System.out.println("Respostar do Servidor: id = " + id);
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    private void close() {
        try {
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
