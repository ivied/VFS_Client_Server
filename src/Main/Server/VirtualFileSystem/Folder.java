package VirtualFileSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс обеспечивающий папки файловой системы
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class Folder extends FileSystemObj {

    public ArrayList<FileSystemObj> folderList = new ArrayList<FileSystemObj>();

    public Folder(String path, Folder currentFolder) {
        super(path, currentFolder);
    }



    public boolean isClear(){
        return folderList.isEmpty();
    }

}
