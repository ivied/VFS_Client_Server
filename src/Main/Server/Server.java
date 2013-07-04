import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Класс отвечает за работу сервера. Прнимает новых  User.
 * Вводит информацию для GUI
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class Server extends Thread  {

    public LinkedList<User> userList;
    private ServerSocket serverSocket;
    private final Object lock = new Object();

    GUIListener GUI ;

    public Server() {
        super("File server");
        userList = new LinkedList<User>();
    }

    /**
     * Запуск сервера по заданному адресу
     */
    public void init(String ip, int port) {
        try {
            serverSocket = new ServerSocket(port);
            start();
            serverStarted(ip, port);
        } catch(Exception ex) {
            serverStopped();
        }
    }

    /**
     * Метод принимает подключения пользователей
     */
    @Override
    public void run() {
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                new User(this, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *Метод позволяет полтзователю оповестить других пользователей о сделанных изменениях в файловой системе
     *Выводит изменения в GUI
     */
    public void userMakeChanges(String changes, User changeMaker) {
        String message = "User " + changeMaker.userName + " "+ changes;
        synchronized(lock) {
            for(User user : userList) {
                try {
                    if (!user.equals(changeMaker)) user.printStream.println(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            GUI.printOnServer(message);
        }
    }


    public void GUIRegistration(GUIListener GUI) {
        this.GUI =  GUI;
    }



    public void serverStarted(String ip, int port) {
        GUI.serverStarted(ip, port);
    }

    public void serverStopped() {
        GUI.serverStopped();
    }

    public void onUserConnected(User user) {
        synchronized(lock) {
            userList.add(user);
            GUI.onUserConnected(user);
        }
    }

    public void onUserDisconnected(User user) {
        synchronized(lock) {
            userList.remove(user);
            GUI.onUserDisconnected(user);

        }
    }

    public void onMessageReceived(User user, String message) {
        GUI.onMessageReceived(user, message);
    }




}