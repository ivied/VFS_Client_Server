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

    public String deleteObject(FileSystemObj objToRemove) {

        return (objToRemove.getClass() == File.class) ? deleteFile((File) objToRemove): deleteFolder((Folder) objToRemove);
    }

    private String deleteFolder(Folder folderToRemove) {
         return locksCheck(folderToRemove)  ? "Folder locked" : answerRemoveFolder(folderToRemove);

    }

    private String answerRemoveFolder(Folder folderToRemove) {
        folderToRemove.trueParentFolder.folderList.remove(folderToRemove);
        return "Remove folder " + folderToRemove.name;
    }

    private boolean locksCheck(Folder folderToRemove) {
        for (FileSystemObj fileSystemObj : folderToRemove.folderList){
            boolean objLock =  (fileSystemObj.getClass() == File.class) ? !((File) fileSystemObj).users_locks.isEmpty(): locksCheck(folderToRemove);
            if (objLock) return true;
        }
        return false;
    }

    private String deleteFile(File fileToRemove) {

        return fileToRemove.users_locks.isEmpty() ? answerDeleteFile(fileToRemove) : "File locked";
    }

    private String answerDeleteFile(File fileToRemove) {
        fileToRemove.trueParentFolder.folderList.remove(fileToRemove);
        return "Remove file " + fileToRemove.name;  //To change body of created methods use File | Settings | File Templates.
    }
}
