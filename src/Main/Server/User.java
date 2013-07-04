import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Класс отвечает за подклченных пользователей
 * Каждый пользователь должен иметь свой экзмпляр класса
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class User  extends Thread {
    FileSystemController fileSystemController = new FileSystemController();
    Server server = null;

    private Socket socket = null;
    public BufferedReader bufferedReader = null;
    public PrintStream printStream = null;

    public String userName = "";

    public String userIp = "";

    User(Server chatServer, Socket socket) throws IOException {
        super("User Thread");
        this.server = chatServer;
        this.socket = socket;

        start();
    }

    /**
     * Метод реализует протокол соединения пользователя с сервером
     * Отключает пользователя если пользователь с таким именем уже зарегестрирован
     * Отключет по комманде "quit" при этом сам отправляет аналогичную комманду пользователю
     */
    @Override
    public void run() {
        try {

            if(!socket.isConnected()) return;   //just for tests !!

            setUserPublicFields();
            synchronized (server)   {
                for (User user : server.userList){
                    if (user.userName.equals(userName)) {
                        printStream.println("This UserName already engaged");
                        closeSocket();
                        return;

                    }
                }
            }

            server.onUserConnected(this);

            printStream.println("Hello " + userName + "!");

            while(true) {
                try {
                    printStream.println("Type new command");
                    String messageReceived = bufferedReader.readLine();
                    if(messageReceived==null) {
                        closeSocket();
                        break;
                    }
                    else if(!messageReceived.isEmpty()) {
                        if (messageReceived.equalsIgnoreCase("quit")) {
                            closeSocket();
                            break;
                        }
                        messageHandling(messageReceived);

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    closeSocket();
                    break;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iMakeChanges(String changes) {
        server.userMakeChanges(changes, this);
    }

    private void closeSocket() {
        try {

            printStream.println("quit");
            server.onUserDisconnected(this);
            bufferedReader.close();
            printStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void messageHandling(String messageReceived) {
        List<String> answer = fileSystemController.doCommand(messageReceived, this);
        for(String line : answer){
            printStream.println(line);

        }

        server.onMessageReceived(this, messageReceived);
    }

    private void setUserPublicFields() throws IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        printStream = new PrintStream(socket.getOutputStream(), true, "UTF-8");
        userIp = socket.getInetAddress().getHostAddress();
        userName = bufferedReader.readLine();

        server.GUI.printOnServer("New user " + userName + " try connect to Server");
    }




}

