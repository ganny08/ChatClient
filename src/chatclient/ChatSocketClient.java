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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
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
    ChatHelper chatHelper;
    
    public ChatSocketClient() {
        socket = new Socket();
        chatHelper = new ChatHelper(this);
    }
    
    public boolean connect(byte[] ip, int port) {
        SocketAddress endPoint = null;
        try {
            endPoint = new InetSocketAddress(InetAddress.getByAddress(ip),port);
            socket.connect(endPoint);
            try {    
                inStream = socket.getInputStream(); // создаем поток на чтение
                outStream = socket.getOutputStream(); // создаем поток на запись
            } catch (IOException ex) {
                Logger.getLogger(ChatSocketClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            readAsync();
        } catch (Exception ex) {
            System.out.println("Сервер не доступен или введен неверный адрес");
        }
        return socket.isConnected();
    }
    
    public void readAsync() {
        ConsoleHelper.printMenu();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int countReadByte = 0;
                byte[] tempBuffer = new byte[4096]; // буффер для чтения с сокета
                while (true) {
                    byte[] buffer; // конечный буффер прочитанных байт нужной размерности
                    try {
                        countReadByte = inStream.read(tempBuffer, 0, tempBuffer.length);
                        buffer = Arrays.copyOf(tempBuffer, countReadByte);
                        chatHelper.parsingPackage(buffer);
                    } catch (Exception ex) {
                        System.out.println("Ошибка чтения с сокета: " + ex);
                        break;
                    }
                }
                try {
                    inStream.close(); // закрываем поток на чтение
                } catch (Exception ex) {
                    System.out.println("Ошибка");
                }
            }
        }).start();
    }
    
    public void writeAsync(byte[] buffer, String authToken) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    if (authToken != null) {
                        outStream.write(buffer, 0, buffer.length);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ChatSocketClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
}
