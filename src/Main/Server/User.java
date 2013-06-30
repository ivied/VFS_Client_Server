import VirtualFileSystem.FileSystemController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 27.06.13
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class User  extends Thread {

        Server server = null;

        // Сокет пользователя
        private Socket socket = null;
        // Входной канал
        public BufferedReader bufferedReader = null;
        // Выходной канал
        public PrintStream printStream = null;
        // Имя пользователя
        public String userName = "";
        // IP пользователя
        public String userIp = "";


        // Конструктор
        User(Server chatServer, Socket socket) throws IOException {
            super("User Thread");
            this.server = chatServer;
            this.socket = socket;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            printStream = new PrintStream(socket.getOutputStream(), true, "UTF-8");
            start();
        }

        // Обработка потока
        @Override
        public void run() {
            try {
                // Предлагаем пользователю ввести свое имя

                // IP пользователя
                userIp = socket.getInetAddress().getHostAddress();
                // Получаем логин пользователя
                userName = bufferedReader.readLine(); // блокируется пока не получит строки!
                // Нотификация о подключении нового пользователя
                server.onUserConnected(this);
                // Помещаем пользователя в список пользователей
               // server.userList.add(this);
                // Отправляем всем сообщение
               // server.sendChatMessage(null, "Подключен пользователь: "+userName);
                printStream.println("Hello " + userName + "!");
                // основной цикл обработки
                while(true) {
                    try {
                        // Читаем новое сообщение от пользователя
                        String messageReceived = bufferedReader.readLine(); // блокируется пока не получит строки или null!
                        if(messageReceived==null) {
                            // Невозможно прочитать данные, пользователь отключился от сервера
                            closeSocket();
                            // Останавливаем бесконечный цикл
                            break;
                        }
                        else if(!messageReceived.isEmpty()) {
                            FileSystemController fileSystemController = new FileSystemController();
                            List<String> answer = fileSystemController.doCommand(messageReceived);
                            for(String line : answer){
                            printStream.println(line);

                            }
                            // Нотификация: получено сообщение
                            server.onMessageReceived(this, messageReceived);
                            // Отправляем всем сообщение
                           // server.sendChatMessage(this, messageReceived);
                        }
                    } catch (Exception ex) {
                        closeSocket();
                        break;
                    }
                }
            } catch(Exception ex) {}
        }




    // Удаляет пользователя из списка онлайн пользователей и освобождает ресурсы сокета
    private void closeSocket() {
        try {
            // Нотификация: пользователь отключился
            server.onUserDisconnected(this);
            // Отправляем всем сообщение
         //   server.sendChatMessage(null, "Отключен пользователь: "+userName);
            // Удаляем пользователя со списка онлайн
        //    server.userList.remove(this);
            // Закрываем потоки
            bufferedReader.close();
            printStream.close();
            socket.close();
        } catch (Exception ex) {}
    }

}

