/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 27.06.13
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public interface GUIListener {
    /****** События *******/

    // Сервер запустился
    public void serverStarted(String ip, int port);

    // Сервер прекратил работу
    public void serverStopped();

    // Подключился новый пользователь
    public void onUserConnected(User user);

    // Пользователь отключился
    public void onUserDisconnected(User user);

    // Получено сообщение от пользователя
    public void onMessageReceived(User user, String message)     ;


}
