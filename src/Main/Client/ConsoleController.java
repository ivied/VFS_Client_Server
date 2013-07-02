import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 27.06.13
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleController {

    private String commandLine;
    private Client client;
    ClientConsole clientConsole = new ClientConsole();

    ConsoleController (Client client) {
        this.client = client;
        write("Welcome to Client app! At the beginning connect to server ");

    }

    public void doNewCommand(){
        String [] command = getCommand();

        doCommand(command);

    }

    public void doCommand(String[] command)  {

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

    public String [] getCommand(){
        //write("Type command");
        commandLine  = clientConsole.readNewInput();
        String  [] command =   commandLine.split(" ");

        return command;

    }

    public void write(String message){
        clientConsole.write(message);
    }

    public void write(List<String> message){
        for (String line : message){
            clientConsole.write(line);
        }
    }


}
