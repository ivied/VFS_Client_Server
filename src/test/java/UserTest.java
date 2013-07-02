import VirtualFileSystem.File;
import VirtualFileSystem.FileSystem;
import VirtualFileSystem.Folder;
import junit.framework.Assert;
import org.junit.Before;
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
    List<String> answer;
    User user;
    User user2;

    Server server = new Server();


    @Before
    public void setUp () throws IOException {
        server.GUI = new ServerGUI();
        user = new User(server,(new Socket()));
        user.userName = "Tester";

        user2 = new User(server,(new Socket()));
        user2.userName = "Tester2";
    }

    @Test
    public void userLockUnlockTest () throws IOException {


        answer = fileSystemController.doCommand("ping",user);
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

    @Test
    public void userRDTests () throws IOException {
        Folder folder = new Folder("C:\\testRD", FileSystem.getInstance().ROOT_FOLDER);
        Assert.assertTrue(folder.name.equals("testRD"));
        answer = fileSystemController.doCommand("rd testRD",user2);
        mustBe.clear();
        mustBe.add("Remove folder testRD");
        assertTrue(answer.equals(mustBe));



        File file= new File ("testRD.RD", FileSystem.getInstance().ROOT_FOLDER);
        Assert.assertTrue(file.exist);
        answer = fileSystemController.doCommand("rd testRD.RD",user2);
        mustBe.clear();
        mustBe.add("For remove file use DEL command");
        assertTrue(answer.equals(mustBe));


        new Folder("C:\\testRD", FileSystem.getInstance().ROOT_FOLDER);
          new File ("testRD\\testRD.RD", FileSystem.getInstance().ROOT_FOLDER);
         answer = fileSystemController.doCommand("rd testRD",user2);
        mustBe.clear();
        mustBe.add("Remove folder testRD");
        assertTrue(answer.equals(mustBe));



        new Folder("C:\\testRD", FileSystem.getInstance().ROOT_FOLDER);
        new Folder("C:\\testRD\\test", FileSystem.getInstance().ROOT_FOLDER);
        new File ("testRD\\testRD.RD", FileSystem.getInstance().ROOT_FOLDER) ;
        answer = fileSystemController.doCommand("rd testRD",user2);
        mustBe.clear();
        mustBe.add("Deleting Folder consist subFolders");
        assertTrue(answer.equals(mustBe));



        new File ("testRD.RD", FileSystem.getInstance().ROOT_FOLDER);
        answer = fileSystemController.doCommand("lock testRD.RD",user);
        mustBe.clear();
        mustBe.add("Lock testRD.RD");
        assertTrue(answer.equals(mustBe));


        new Folder("C:\\testRD2", FileSystem.getInstance().ROOT_FOLDER);
        new File ("testRD2\\testRD.RD", FileSystem.getInstance().ROOT_FOLDER) ;
        answer =  fileSystemController.doCommand("lock testRD2\\testRD.RD",user2);
        answer = fileSystemController.doCommand("rd testRD2",user2);
        mustBe.clear();
        mustBe.add("Folder locked");
        assertTrue(answer.equals(mustBe));
    }

}
