import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 27.06.13
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class ClientConsole  {


    public String readNewInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }

    public void write(String message){
        System.out.println(message);

    }


}
