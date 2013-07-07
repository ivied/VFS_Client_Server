import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за работу клиента
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class Client {

    ConsoleController consoleController;
    protected volatile Socket serverSocket;

    PrintWriter output;
    volatile BufferedReader  input;
    MessageReceiver messageReceiver;


    public  Client(){
        consoleController = new ConsoleController(this);
    }


    public void connect(ConnectData connectData ) throws IOException {
        if (messageReceiver != null){output.println("quit");}


        serverSocket = new Socket(connectData.ip,connectData.port);

        messagingWithServer(connectData.userName) ;
        messageReceiver = new MessageReceiver();
        messageReceiver.start();

    }

    /**
     * Метод реализует обмен сообщениями с сервером если сокет подключен
     */
    public List<String> messagingWithServer(String message) throws IOException {
        if(serverSocket == null)  {
            consoleController.write("At the beginning connect to server!");
            return null;
        }
        if (serverSocket.isClosed())    consoleController.write("At the beginning connect to server!");
        output = new PrintWriter(serverSocket.getOutputStream(), true);
        output.println(message);


        return null  ;

    }

    /**
     * Метод инициализирует отключение клиента от сервера в соотвествии с протоколом
     */
    public void disconnect() {
        if (serverSocket.isConnected()){

            output.println("quit");

        }    else {
            consoleController.write("At the beginning connect to server!");
        }
    }

    /**
     * Класс отвечает за обмен сообщений между пользователем и сервером в соответствии с протоколом
     */
    public class MessageReceiver extends Thread{
        @Override
        public void run() {

            try {
                input = new
                        BufferedReader(new
                        InputStreamReader(serverSocket.getInputStream()));
                String messageFromServer;

                while (serverSocket.isConnected()) {

                    messageFromServer = input.readLine();
                    consoleController.write("Server say: " + messageFromServer);

                    if (messageFromServer.equalsIgnoreCase("quit")) break;

                }

                output.close();
                input.close();

                consoleController.write("You was disconnected from server");
            } catch (IOException e) {

            consoleController.write("Server close your connection");
            }


        }
    }



}
