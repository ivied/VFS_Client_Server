package VirtualFileSystem;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 01.07.13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class File extends FileSystemObj {
    public ArrayList<String> users_locks = new ArrayList<>();


    public File(String path, Folder currentFolder) {
        super(path, currentFolder);
    }
}
