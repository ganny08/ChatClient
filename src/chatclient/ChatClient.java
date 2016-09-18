/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.util.Scanner;

/**
 *
 * @author Kirill
 */
public class ChatClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    }
    
    public static void readFromConsole() {
        Scanner scanner = new Scanner(System.in);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String command = scanner.next();
                    System.out.println("Введено: " + command);
                }
            }
        }).start();
    }
    
}
