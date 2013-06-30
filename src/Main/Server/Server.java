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
public class Server extends Thread implements ActionListenerServerSide{


        // Хранит онлайн пользователей чата
        public LinkedList<User> userList;
        // Хранит слушателей сервера
        private LinkedList<ActionListenerServerSide> listenerList;
        // СерверСокет
        private ServerSocket serverSocket;

        // Обьект синхронизации доступа
        private final Object lock = new Object();

        // Конструктор
        public Server() {
            super("File server");
            userList = new LinkedList<User>();
            listenerList = new LinkedList<ActionListenerServerSide>();
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
               // userList.add(user);
            } catch (Exception ex) {
            }
        }
    }

    /***************** отправка сообщения всем пользователям ****************/
    // sender - отправитель

    public void sendChatMessage(User sender, String message) {
        synchronized(lock) {
            for(User user : userList) {
                try {
                    user.printStream.println("<"+(sender!=null ? sender.userName : "server")+"> "+message);
                } catch (Exception ex) {}
            }
        }
    }

    /******************** добавление/удаление слушателей ********************/

    // Добавляет слушателя событий сервера
    public void addListener(ActionListenerServerSide listener) {
        synchronized(lock) {
            listenerList.add(listener);
        }
    }

    // Удаляет слушателя
    public void removeListener(ActionListenerServerSide listener) {
        synchronized(lock) {
            listenerList.remove(listener);
        }
    }

    /******************** методы интерфейса ServerListener *******************/

    public void serverStarted(String ip, int port) {
        synchronized(lock) {
            for(ActionListenerServerSide listener : listenerList) {
                listener.serverStarted(ip, port);
            }
        }
    }

    public void serverStopped() {
        synchronized(lock) {
            for(ActionListenerServerSide listener : listenerList) {
                listener.serverStopped();
            }
        }
    }

    public void onUserConnected(User user) {
        synchronized(lock) {
            for(ActionListenerServerSide listener : listenerList) {
                listener.onUserConnected(user);
            }
        }
    }

    public void onUserDisconnected(User user) {
        synchronized(lock) {
            for(ActionListenerServerSide listener : listenerList) {
                listener.onUserDisconnected(user);
            }
        }
    }

    public void onMessageReceived(User user, String message) {
        synchronized(lock) {
            for(ActionListenerServerSide listener : listenerList) {
                listener.onMessageReceived(user, message);
            }
        }
    }

}