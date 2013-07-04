/**
 * Класс отвечает за запуск клиента.
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class Main {
    static Client client = new Client();
    public static void main(String[] args)  {

        client.consoleController.doNewCommand();
    }
}
