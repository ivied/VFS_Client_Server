import java.util.Scanner;

/**
 * Класс отвечает за чтение и вывод данных на консоль
 * Created with IntelliJ IDEA.
 * User: Serv
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
