package VirtualFileSystem;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 01.07.13
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class FileTest {
    @Test
    public void createFile () {
        File file= new File ("test.test", FileSystem.getInstance().ROOT_FOLDER)     ;
        Assert.assertTrue(file.exist);
        File file2= new File ("test.test\\test", FileSystem.getInstance().ROOT_FOLDER)     ;
        Assert.assertFalse(file2.exist);
        Folder folder = new Folder("fileTest", FileSystem.getInstance().ROOT_FOLDER);
        File file3 = new File("test.test", FileSystem.getInstance().ROOT_FOLDER);
        Assert.assertFalse(file3.exist);
        File file4 = new File("test.test", folder);
        Assert.assertTrue(file4.exist);
        File file5 = new File("C:\\test.test\\test.test", FileSystem.getInstance().ROOT_FOLDER);
        Assert.assertFalse(file5.exist) ;

    }
}
