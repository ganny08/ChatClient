/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.nio.charset.Charset;
import javax.xml.bind.annotation.XmlElement;

/*
* Номера функций: 
* 1 - Регистрация (Sign UP)
* 2 - Вход (Login)
* 3 - Выход (Logout)
* 6 - Аутентификация (Auth)
*/

public class ChatHelper {
    private ChatSocketClient client;
    public String authenticationToken;
        
    public ChatHelper(ChatSocketClient client) {
        this.client = client;
        authenticationToken = null;
    }
    
    public void createAndSendLoginPackage (String login, String pass) {
        String data = login + ":" + pass;
        byte[] string = data.getBytes(Charset.forName("cp866"));
        byte[] bytePackage = new byte[1 + string.length]; // размер пакета 1 (номер функции) + размер строки
        bytePackage[0] = 2;
        client.writeAsync(bytePackage);
    }
    
    public void createAndSendSignUpPackage (String login, String pass) {
        String data = login + ":" + pass;
        byte[] string = data.getBytes(Charset.forName("cp866"));
        byte[] bytePackage = new byte[1 + string.length]; // размер пакета 1 (номер функции) + размер строки
        bytePackage[0] = 1;
        client.writeAsync(bytePackage);
    }
    
    public void createAndSendLogoutPackage () {
        String data = authenticationToken;
        byte[] string = data.getBytes(Charset.forName("cp866"));
        byte[] bytePackage = new byte[1 + string.length]; // размер пакета 1 (номер функции) + размер токена
        bytePackage[0] = 3;
        client.writeAsync(bytePackage);
    }
    
    public void parsingPackage(byte[] buffer) {
        switch (buffer[0]) {
            case 6: {
                authenticationToken = new String(buffer, 1, buffer.length - 1);
                break;
            }
            default: {
                System.out.println(new String(buffer, Charset.forName("cp866")));
            }
        }
    }
}

