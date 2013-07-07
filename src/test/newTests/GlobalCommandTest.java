import VirtualFileSystem.File;
import VirtualFileSystem.FileSystemSingleton;
import VirtualFileSystem.Folder;

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
 * Date: 07.07.13
 * Time: 22:11
 * To change this template use File | Settings | File Templates.
 */
public class GlobalCommandTest {
    FileSystemController fileSystemController = new FileSystemController();
    List<String> mustBe = new ArrayList<>();
    List<String> answer;
    User user;
    User user2;

    Server server = new Server();
    @Before
    public void setUp() throws IOException {
        server.GUI = new ServerGUI();
        user = new User(server,(new Socket()));
        user.userName = "Tester";

        user2 = new User(server,(new Socket()));
        user2.userName = "Tester2";
        new Folder("C:\\folder1", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new Folder("C:\\folder2", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new Folder("C:\\folder1\\subFolder1", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new Folder("C:\\folder1\\subFolder2", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new Folder("C:\\folder3", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new Folder("C:\\folder4", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new Folder("C:\\folder2\\subFolder1", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new Folder("C:\\folder2\\subFolder2", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new File("C:\\folder1\\subFolder1\\file", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new File("C:\\folder4\\file", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new File("C:\\file1", FileSystemSingleton.getInstance().ROOT_FOLDER);
        new File("C:\\file2", FileSystemSingleton.getInstance().ROOT_FOLDER);
        fileSystemController.doCommand("lock file1",user);
        fileSystemController.doCommand("lock folder4\\file",user);

    }

    @Test
    public void GlobalTest() {


        answer =fileSystemController.doCommand("copy folder4 folder3", user2);
        mustBe.clear();
        mustBe.add("Copy folder4 to folder3");
        assertTrue(answer.equals(mustBe));

        answer =fileSystemController.doCommand("move file2 folder3", user2);
        mustBe.clear();
        mustBe.add("Copy file2 to folder3");
        assertTrue(answer.equals(mustBe));

        answer =fileSystemController.doCommand("move file1 folder3", user2);
        mustBe.clear();
        mustBe.add("Cant move this");
        assertTrue(answer.equals(mustBe));

        answer =fileSystemController.doCommand("move file1 folder3", user2);
        mustBe.clear();
        mustBe.add("Cant move this");
        assertTrue(answer.equals(mustBe));

    }
}
