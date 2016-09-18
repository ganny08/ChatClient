/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Kirill
 */
public class ChatSocketClient {
    Socket socket;
    InputStream inStream;
    OutputStream outStream;
    
    public ChatSocketClient(byte[] ip, int port) {
        try {
            socket = new Socket(InetAddress.getByAddress(ip),port);
        } catch (IOException ex) {
            Logger.getLogger(ChatSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void readAsync() {
        ChatSocketClient localClient = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int countReadByte = 0;
                byte[] tempBuffer = new byte[4096]; // буффер для чтения с сокета
                while (true) {
                    if (inStream != null) {
                        byte[] buffer; // конечный буффер прочитанных байт нужной размерности
                        try {
                            countReadByte = inStream.read(tempBuffer, 0, tempBuffer.length);
                            buffer = Arrays.copyOf(tempBuffer, countReadByte);
                            System.out.println(new String(buffer));
                            
                        } catch (IOException ex) {
                            Logger.getLogger(ChatSocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else {
                        break;
                    }
                }
            }
        }).start();
    }
    
    public void writeAsync(byte[] buffer) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    outStream.write(buffer, 0, buffer.length);
                } catch (IOException ex) {
                    Logger.getLogger(ChatSocketClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
}
