import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс отвечает за генерацию данных для подключения к серверу
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class ConnectData {
    private static final int SERVER_ADDRESS = 1;
    private static final int CLIENT_NAME = 2;
    String ip;
    int port;
    String userName;

    public ConnectData(String [] command){
        Pattern pattern = Pattern.compile("(.*)\\:(.*)");
        if (command.length<3) return;

        Matcher matcher = pattern.matcher(command[SERVER_ADDRESS]);

        if (!matcher.matches()) return ;

        try {
            ip = matcher.group(1).equalsIgnoreCase("localhost") ?  InetAddress.getLocalHost().getHostAddress() :  matcher.group(1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

       try {
            port = Integer.valueOf(matcher.group(2));
        }catch (Exception e){

        }
        userName = command[CLIENT_NAME];
    }

}
