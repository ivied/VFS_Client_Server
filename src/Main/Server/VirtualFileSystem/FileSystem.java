package VirtualFileSystem;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 29.06.13
 * Time: 21:17
 * To change this template use File | Settings | File Templates.
 */
public class FileSystem  {
    private static FileSystem instance;
    public   Folder ROOT_FOLDER =null;

    private FileSystem(){
         ROOT_FOLDER = new Folder ("C:", null);
    }

    public static FileSystem getInstance() {
        if (instance == null) {
            instance = new FileSystem();
        }
        return instance;
    }

}
