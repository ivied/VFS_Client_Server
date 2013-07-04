/**
 * Список методов которые должен реализовавыть крафический интерфейс Server'a
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public interface GUIListener {

    public void serverStarted(String ip, int port);

    public void serverStopped();

    public void onUserConnected(User user);

    public void onUserDisconnected(User user);

    public void onMessageReceived(User user, String message);

    public void printOnServer (String message);
}
