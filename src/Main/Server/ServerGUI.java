import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс реализует графический интефейс сервера
 * Класс для запуска сервера
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class ServerGUI extends JFrame implements GUIListener {
    private static final String CONFIG_FILE="ServerConfig.txt";
    private static final String PORT = "port";
    private static final String IP = "ip";
    private static final String HEIGHT = "height";
    public static final String WIDTH = "width";

    Server server;
    static Map<String,String> config = new HashMap<String, String>();
    private JTextArea textArea = new JTextArea(6, 20);

    /**
     * Метод считывает конфигурации сервера и формирует  графический интерфейс
     *
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                getConfig();
                ServerGUI frame = new ServerGUI();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setTitle("File Server");
                try{

                    frame.setSize(Integer.parseInt(config.get(WIDTH)), Integer.parseInt(config.get(HEIGHT)));
                } catch (NumberFormatException e)  {
                    frame.setSize(800, 500);
                }

                frame.setLocationByPlatform(true);
                frame.setVisible(true);
            }
        });
    }

    /**
     * Конструктор класса запускет сервер и графический интерфейс
     */
    public ServerGUI() {
        super();
        setTitle("Server");
        setLayout(new BorderLayout());
        server = new Server();
        server.GUIRegistration(this);
        initComponents();
    }



    public void serverStarted(String ip, int port) {
        textArea.append("\nСервер запущен по адресу: "+ip+" порт: "+port);
    }

    public void serverStopped() {
        textArea.append("\nНевозможно запустить сервер ");
    }

    public void onUserConnected(User user) {

        textArea.append("\nПодключен новый пользователь: "+user.userName);
    }

    public void onUserDisconnected(User user) {

        textArea.append("\nПользователь отключился: "+user.userName);
    }

    public void onMessageReceived(User user, String message) {
        textArea.append("\n<"+user.userName+"> "+message);
    }


    public void printOnServer(String message) {
        textArea.append("\n"+message);
    }


    private static void getConfig() {
        String programDir = System.getProperty("user.dir");
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(programDir + "/" +CONFIG_FILE);
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text;

            while ((text = reader.readLine()) != null) {
                addConfigParameter(text);
                list.add(text);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addConfigParameter(String text) {
        Pattern pattern = Pattern.compile("(.*)\\:(.*)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()){
            config.put(matcher.group(1),matcher.group(2));

        }
    }

    private void initComponents() {
        System.out.println(config.get(PORT));
        try{

            server.init(config.get(IP),Integer.parseInt(config.get(PORT)));
        } catch (NumberFormatException e)  {
            try {
                server.init(InetAddress.getLocalHost().getHostAddress(),5463);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
        }
        JScrollPane scroll = new JScrollPane(textArea);
        add(scroll, BorderLayout.CENTER);

    }


}