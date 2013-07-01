import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 27.06.13
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class Server extends Thread implements GUIListener {


    // Хранит онлайн пользователей чата
    public LinkedList<User> userList;

    // СерверСокет
    private ServerSocket serverSocket;

    // Обьект синхронизации доступа
    private final Object lock = new Object();

    ServerGUI GUI ;

    // Конструктор
    public Server() {
        super("File server");
        userList = new LinkedList<User>();

    }

    // Запускает сервер на определенном порту
    public void init(String ip, int port) {
        try {
            serverSocket = new ServerSocket(port);
            start();
            // Нотификация события: сервер запущен
            serverStarted(ip, port);
        } catch(Exception ex) {
            // Нотификация события: cервер прекратил работу
            serverStopped();
        }
    }

    /********************** Обработка потока сервера ************************/

    @Override
    public void run() {
        while(true) {
            try {
                // Метод accept() блочит данный поток пока не подключиться новый пользователь
                Socket socket = serverSocket.accept();
                // Создание нового потока-обработчика для подключенного пользователя
                // он выполняется паралельно данному потоку, и ниче не блочит.
                // Сервер переходит к готовности принять новое соединение
                User user = new User(this, socket);

            } catch (Exception ex) {
            }
        }
    }

    /***************** отправка сообщения всем пользователям ****************/
    // sender - отправитель

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


    /******************** добавление/удаление слушателей ********************/

    // Добавляет слушателя событий сервера
    public void GUIRegistration(ServerGUI GUI) {
        this.GUI =  GUI;
    }

    // Удаляет слушателя
    public void removeListener(GUIListener listener) {
        synchronized(lock) {
            //listenerList.remove(listener);
        }
    }

    /******************** методы интерфейса ServerListener *******************/

    public void serverStarted(String ip, int port) {
        synchronized(lock) {

            GUI.serverStarted(ip, port);

        }
    }

    public void serverStopped() {
        synchronized(lock) {

            GUI.serverStopped();

        }
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
        synchronized(lock) {

            GUI.onMessageReceived(user, message);

        }
    }




}