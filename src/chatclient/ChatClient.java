/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 *
 * @author Kirill
 */
public class ChatClient {

    public static void main(String[] args) {
        ChatSocketClient client = new ChatSocketClient();
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Введите адрес сервера -> ");
            String ip = scanner.nextLine(); // читаем введеную строку
            String[] octetArray = ip.split("[.]"); // разделяем на октеты
            byte[] address = new byte[octetArray.length]; // выделяем байт аррей
            for(int i=0; i< octetArray.length; i++) {
                address[i] = (byte)Integer.parseUnsignedInt(octetArray[i]); // копируем остеты, переведенные в байты, в массив
            }
            System.out.print("Введите порт сервера -> ");
            int port = scanner.nextInt(); // читаем введный порт
            if (!client.connect(address, port)) {
                System.out.println("Попробуйте еще раз");
            }
            else {
                break;
            }
        }
        scanner = null;
        readFromConsole(client); // запуск потока на чтение с консоли
    }
    
    public static void readFromConsole(ChatSocketClient client) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in, "cp866");
                while(true) {
                    String msg = scanner.nextLine();
                    client.writeAsync(msg.getBytes(Charset.forName("cp866"))); // передаем введеное сообщение на сервер
                }
            }
        }).start();
    }
    
}
