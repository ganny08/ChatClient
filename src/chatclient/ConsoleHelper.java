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
public class ConsoleHelper {
    ChatSocketClient client;
    Scanner scanner;
    
    public ConsoleHelper(ChatSocketClient client) {
        this.client = client;
        scanner = new Scanner(System.in, "cp866");
    }
    
    public void readFromConsole() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String msg = scanner.nextLine();
                    switch (msg) {
                        case "Login": {
                            
                            break;
                        }
                        case "Logout": {
                            
                            break;
                        }
                        case "Sign up" : {
                            
                            break;
                        }
                        case "Help" : {
                            
                            break;
                        }
                    }
                    client.writeAsync(msg.getBytes(Charset.forName("cp866"))); // передаем введеное сообщение на сервер
                }
            }
        }).start();
    }
    
    private void printMenu() {
        System.out.println("1 - Sign up");
        System.out.println("2 - Login");
        System.out.println("3 - Logout");
        System.out.println("4 - Help");
    }
    
    private void sendLogin() {
        String login, pass;
        System.out.print("Введите логин -> ");
        login = scanner.nextLine();
        System.out.print("Введите пароль -> ");
        pass = scanner.nextLine();
    }
}
