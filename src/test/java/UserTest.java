import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 01.07.13
 * Time: 21:30
 * To change this template use File | Settings | File Templates.
 */
public class UserTest {
    FileSystemController fileSystemController = new FileSystemController();
    List<String> mustBe = new ArrayList<>();
    @Test
    public void userTests () throws IOException {

        Socket socket= new Socket();

        User user = new User((new Server()),(new Socket()));
        user.userName = "Tester";
        User user2 = new User((new Server()),(new Socket()));
        user2.userName = "Tester2";
        List<String> answer = fileSystemController.doCommand("ping",user);
        mustBe.clear();
        mustBe.add("pong");
        assertTrue(answer.equals(mustBe));
        answer = fileSystemController.doCommand("mf user.txt",user);
        mustBe.clear();
        mustBe.add("Create user.txt");
        assertTrue(answer.equals(mustBe));
        answer = fileSystemController.doCommand("lock user.txt",user2);
        mustBe.clear();
        mustBe.add("Lock user.txt");
        assertTrue(answer.equals(mustBe));
        answer = fileSystemController.doCommand("lock user.txt",user);
        mustBe.clear();
        mustBe.add("Lock user.txt");
        assertTrue(answer.equals(mustBe));
        answer = fileSystemController.doCommand("unlock user.txt",user);
        mustBe.clear();
        mustBe.add("Unlock user.txt");
        assertTrue(answer.equals(mustBe));
        answer = fileSystemController.doCommand("unlock user.txt",user);
        mustBe.clear();
        mustBe.add("You already unlock this file");
        assertTrue(answer.equals(mustBe));
        answer = fileSystemController.doCommand("unlock user.txt",user2);
        mustBe.clear();
        mustBe.add("Unlock user.txt");
        assertTrue(answer.equals(mustBe));


    }



}
