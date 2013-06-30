package VirtualFileSystem;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 30.06.13
 * Time: 15:49
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemControllerTest {
    FileSystemController fileSystemController  = new FileSystemController();
    @Test
    public void changeFolder() {
        List<String>  answers;
        ArrayList<String> directories = new ArrayList();
        directories.add(0,"C:");
        answers = fileSystemController.changeCurrentFolder( directories);
        Assert.assertTrue(answers.get(0).equals("Current directory C:"));
        directories.add(0,"C:\\testChange");
        answers = fileSystemController.changeCurrentFolder( directories);
        Assert.assertTrue(answers.get(0).equals("Can not change directory"));
        Folder folder = new Folder("C:\\testChange", FileSystem.getInstance().ROOT_FOLDER);
        directories.add(0,"C:\\testChange");
        answers = fileSystemController.changeCurrentFolder( directories);
        Assert.assertTrue(answers.get(0).equals("Current directory testChange"));
        Folder folder1 = new Folder("C:\\testChange\\test", FileSystem.getInstance().ROOT_FOLDER);
        Assert.assertTrue(folder1.exist);
               directories.add(0,"C:\\testChange\\test");
        answers = fileSystemController.changeCurrentFolder( directories);
        Assert.assertTrue(answers.get(0).equals("Current directory test"));
        directories.add(0,"C:");
        answers = fileSystemController.changeCurrentFolder( directories);
        Assert.assertTrue(answers.get(0).equals("Current directory C:"));
        directories.add(0,"test");
        answers = fileSystemController.changeCurrentFolder( directories);
        Assert.assertFalse(answers.get(0).equals("Current directory test"));
        directories.add(0,"testChange");
        answers = fileSystemController.changeCurrentFolder( directories);
        Assert.assertTrue(answers.get(0).equals("Current directory testChange"));


    }

}
