package VirtualFileSystem;

/**
* Created with IntelliJ IDEA.
* User: Serv
* Date: 03.07.13
* Time: 1:38
* To change this template use File | Settings | File Templates.
*/
public class FileSystemObjWithFlag {

    public FileSystemObj fileSystemObj;
    public int flag;
    public Folder parentFolder;

    public final static int CURRENT_FOLDER = 0;
    public final static int NEW_FOLDER = 1;
    public final static int FILE = 2;
    public final static int CREATE_OBJ = 3;

    public FileSystemObjWithFlag(FileSystemObj fileSystemObj, int flag, Folder parentFolder) {
        this.fileSystemObj =  fileSystemObj;
        this.flag = flag;
        this.parentFolder = parentFolder;
    }
}
