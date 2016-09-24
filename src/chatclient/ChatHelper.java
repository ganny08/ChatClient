/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.nio.charset.Charset;
import java.util.Arrays;

/*
* Номера функций: 
* 1 - Регистрация (Sign UP)
* 2 - Вход (Login)
* 3 - Выход (Logout)
* 6 - Аутентификация (Auth)
* 10 - Сообщение
*/

public class ChatHelper {
    private ChatSocketClient client;
    public String authenticationToken;
        
    public ChatHelper(ChatSocketClient client) {
        this.client = client;
        authenticationToken = "";
    }
    
    public void createAndSendLoginPackage (String login, String pass) {
        String data = login + ":" + pass;
        byte[] string = data.getBytes(Charset.forName("cp866"));
        byte[] bytePackage = new byte[1 + string.length]; // размер пакета 1 (номер функции) + размер строки
        bytePackage[0] = 2;
        System.arraycopy(string, 0, bytePackage, 1, string.length);
        client.writeAsync(bytePackage, "");
    }
    
    public void createAndSendSignUpPackage (String login, String pass) {
        String data = login + ":" + pass;
        byte[] string = data.getBytes(Charset.forName("cp866"));
        byte[] bytePackage = new byte[1 + string.length]; // размер пакета 1 (номер функции) + размер строки
        bytePackage[0] = 1;
        System.arraycopy(string, 0, bytePackage, 1, string.length);
        client.writeAsync(bytePackage, "");
    }
    
    public void createAndSendLogoutPackage () {
        String data = authenticationToken;
        byte[] string = data.getBytes(Charset.forName("cp866"));
        byte[] bytePackage = new byte[1 + string.length]; // размер пакета 1 (номер функции) + размер токена
        bytePackage[0] = 3;
        System.arraycopy(string, 0, bytePackage, 1, string.length);
        client.writeAsync(bytePackage,authenticationToken);
    }
    
    public void createAndSendMsg(byte[] buffer) {
        if(authenticationToken.length() == 6) {
            String token = authenticationToken;
            byte[] string = token.getBytes();
            byte[] bytePackage = new byte[1 + string.length + buffer.length]; // размер пакета 1 (номер функции) + размер токена
            bytePackage[0] = 10;
            System.arraycopy(string, 0, bytePackage, 1, string.length);
            System.arraycopy(buffer, 0, bytePackage, 7, buffer.length);
            client.writeAsync(bytePackage,authenticationToken);
        }
        else
            System.out.println("Сообщение не отправлено. Необходима авторизация");
    }
    
    public void parsingPackage(byte[] buffer) {
        switch (buffer[0]) {
            case 6: {
                authenticationToken = new String(buffer, 1, buffer.length - 1);
                System.out.println("Авторизация прошла успешно");
                break;
            }
            case 3: {
                authenticationToken = "";
                System.out.println("Вы не авторизированы на сервере");
                break;
            }
            case 10: {
                String[] temp = getLoginAndMsg(buffer);
                System.out.println(temp[0] + ": " + temp[1]);
            }
            default: {
                //System.out.println(new String(buffer, Charset.forName("cp866")));
            }
        }
    }
    
    public String[] getLoginAndMsg(byte[] buffer) {
        String[] temp = new String[2];
        byte loginLength = buffer[1];
        byte[] login = new byte[loginLength];
        byte[] msg = new byte[buffer.length - 2- loginLength];
        System.arraycopy(buffer, 2, login, 0, loginLength);
        System.arraycopy(buffer, loginLength + 2, msg, 0, msg.length);
        temp[0] = new String(login,Charset.forName("cp866"));
        temp[1] = new String(msg,Charset.forName("cp866"));
        return temp;
    }
}

