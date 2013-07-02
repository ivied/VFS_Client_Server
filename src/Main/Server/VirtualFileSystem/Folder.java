package VirtualFileSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 29.06.13
 * Time: 1:30
 * To change this template use File | Settings | File Templates.
 */
public class Folder extends FileSystemObj {

    public ArrayList<FileSystemObj> folderList = new ArrayList<FileSystemObj>();

    public Folder(String path, Folder currentFolder) {
        super(path, currentFolder);
    }


    // protected  Folder currentDirectory = ROOT_FOLDER;














   /* public ArrayList<FileSystemObj> getFolderList (){
         return folderList;
    }*/


}
