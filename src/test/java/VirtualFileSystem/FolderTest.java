package VirtualFileSystem;


import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 29.06.13
 * Time: 1:30
 * To change this template use File | Settings | File Templates.
 */
public class FolderTest {

    @Test
    public void createFolder(){

        Folder folder = new Folder("C:\\test", FileSystemSingleton.getInstance().ROOT_FOLDER);
        Assert.assertTrue(folder.name.equals("test"));
        Folder folder1 = new Folder("C:\\test1\\test", folder);
        Assert.assertFalse(folder1.exist);
        Folder folder2 = new Folder("C:\\test\\test", folder);
        Assert.assertTrue(folder2.exist);
        Folder folder3 = new Folder("C:\\test\\notExist\\test", folder);
        Assert.assertFalse(folder3.exist);
        Folder folder4 = new Folder("C:", null);
        Assert.assertFalse(folder4.exist);
        Folder folder5 = new Folder("C:\\test\\test", folder);
        Assert.assertFalse(folder5.exist);
        Folder folder10 = new Folder("test\\test", folder);
        Assert.assertTrue(folder10.exist);
        Folder folder6 = new Folder("test\\test\\test", folder);
        Assert.assertTrue(folder6.exist);
        Folder folder7 = new Folder("test\\test\\test2", folder);
        Assert.assertTrue(folder7.exist);
        Folder folder8 = new Folder("test2", folder);
        Assert.assertTrue(folder8.exist);
        Folder folder9 = new Folder("test\\test2\\test2", folder);
        Assert.assertFalse(folder9.exist);
        Folder folder11 = new Folder("test2", folder8);
        Assert.assertTrue(folder11.exist);
        Folder folder12 = new Folder("C:\\Test", FileSystemSingleton.getInstance().ROOT_FOLDER);
        Assert.assertFalse(folder12.exist);
        Folder folder13 = new Folder("C:\\Test3", FileSystemSingleton.getInstance().ROOT_FOLDER);
        Assert.assertTrue(folder13.exist);
        Folder folder14 = new Folder("Test3", folder);
        Assert.assertTrue(folder14.exist);


    }

}
