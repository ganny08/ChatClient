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
                    switch (msg.toLowerCase()) {
                        case "login": {
                            sendLogin();
                            break;
                        }
                        case "logout": {
                            sendLoguot();
                            break;
                        }
                        case "sign up" : {
                            sendSignUp();
                            break;
                        }
                        case "help" : {
                            printMenu();
                            break;
                        }
                        default: {
                            //client.writeAsync(msg.getBytes(Charset.forName("cp866")),client.chatHelper.authenticationToken); // передаем введеное сообщение на сервер
                            client.chatHelper.createAndSendMsg(msg.getBytes(Charset.forName("cp866")));
                            break;
                        }
                    }
                }
            }
        }).start();
    }
    
    public static void printMenu() {
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
        client.chatHelper.createAndSendLoginPackage(login, pass);
    }
    
    private void sendSignUp() {
        String login, pass;
        System.out.print("Введите логин -> ");
        login = scanner.nextLine();
        System.out.print("Введите пароль -> ");
        pass = scanner.nextLine();
        client.chatHelper.createAndSendSignUpPackage(login, pass);
    }
    
    private void sendLoguot() {
        client.chatHelper.createAndSendLogoutPackage();
    }
}
