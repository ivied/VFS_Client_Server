import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 27.06.13
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

       try {
            port = Integer.valueOf(matcher.group(2));
        }catch (Exception e){

        }
        userName = command[CLIENT_NAME];
    }

}
