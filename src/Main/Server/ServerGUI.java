import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 27.06.13
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class ServerGUI extends JFrame implements GUIListener {
    private static final String CONFIG_FILE="ServerConfig.txt";
    private static final String PORT = "port";
    private static final String IP = "ip";
    Server server;
    static Map<String,String> config = new HashMap<String, String>();
    private JTextArea textArea = new JTextArea(6, 20);
    private JButton startButton = new JButton("Start Server");

    public ServerGUI() {
        super();
        setTitle("Server");
        setLayout(new BorderLayout());
        server = new Server();
        // Регистрируемся в качестве слушателя событий
        server.GUIRegistration(this);
        initComponents();
    }

    private void initComponents() {
        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Запускаем сервак на порту 5463
                System.out.println(config.get(PORT));
                server.init(config.get(IP),Integer.parseInt(config.get(PORT)));
            }

        });

        JScrollPane scroll = new JScrollPane(textArea);

        add(scroll, BorderLayout.CENTER);
        add(startButton, BorderLayout.EAST);

    }


    /******************** методы интерфейса ServerListener *******************/

    public void serverStarted(String ip, int port) {
        //Отображаем в компоненте
        textArea.append("\nСервер запущен по адресу: "+ip+" порт: "+port);
    }

    public void serverStopped() {
        //Отображаем в компоненте
        textArea.append("\nНевозможно запустить сервер ");
    }

    public void onUserConnected(User user) {
        //Отображаем в компоненте
        textArea.append("\nПодключен новый пользователь: "+user.userName);
    }

    public void onUserDisconnected(User user) {
        //Отображаем в компоненте
        textArea.append("\nПользователь отключился: "+user.userName);
    }

    public void onMessageReceived(User user, String message) {
        //Отображаем в компоненте новое сообщение
        textArea.append("\n<"+user.userName+"> "+message);
        // Пишем данные в файл...
    }


    public void printOnServer(String message) {
        textArea.append("\n"+message);
    }


    /******************** запуск приложения *******************/

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                getConfig();
                ServerGUI frame = new ServerGUI();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle("File Server");
                frame.setSize(800, 500);
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
            }
        });
    }

    private static void getConfig() {
        String programDir = System.getProperty("user.dir");
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(programDir + "/" +CONFIG_FILE);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                addConfigParameter(text);
                list.add(text);
                System.out.println(text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private static void addConfigParameter(String text) {
        Pattern pattern = Pattern.compile("(.*)\\:(.*)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()){
            config.put(matcher.group(1),matcher.group(2));

        }
    }

}