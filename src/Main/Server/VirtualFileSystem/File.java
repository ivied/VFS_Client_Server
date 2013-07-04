package VirtualFileSystem;

import java.util.ArrayList;

/**
 * Класс обеспечивающий файлы файловой системы
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class File extends FileSystemObj {
    public ArrayList<String> users_locks = new ArrayList<>();


    public File(String path, Folder currentFolder) {
        super(path, currentFolder);
    }


    public boolean isLock () {
        return !users_locks.isEmpty();
    }
}

