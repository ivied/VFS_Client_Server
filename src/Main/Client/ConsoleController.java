import java.io.IOException;
import java.util.List;

/**
 * Класс помогает клиненту работать с консолью
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class ConsoleController {

    private String commandLine;
    private Client client;
    ClientConsole clientConsole = new ClientConsole();

    ConsoleController (Client client) {
        this.client = client;
        write("Welcome to Client app! At the beginning connect to server ");

    }

    public void write(String message){
        clientConsole.write(message);
    }

    /**
     * Метод делает запрос на консоль для получения новой команды
     * и стартует ее выполнение
     */
    public void doNewCommand(){
        String [] command = getCommand();

        doCommand(command);

    }

   private void doCommand(String[] command)  {
        try{

            switch (command[0].toLowerCase()){
                case "connect":
                    connectClient(command);
                    break;
                case "quit":
                    disconnectClient();
                    break;
                default:
                    messagingWithServer(commandLine);
                    break;
            }
        }    catch (IllegalArgumentException e)  {


        }
        doNewCommand();


    }

    private void disconnectClient() {
        client.disconnect();
    }


    private void messagingWithServer(String command) {
        try {
            client.messagingWithServer(command);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void connectClient(String[] command) {
        try {
            ConnectData connectData = new ConnectData(command);
            client.connect(connectData);
        } catch (IOException e) {
            write("Can't connect");

        }

    }

    private String [] getCommand(){

        commandLine  = clientConsole.readNewInput();
        String  [] command =   commandLine.split(" ");

        return command;

    }

}
