import VirtualFileSystem.File;
import VirtualFileSystem.FileSystem;
import VirtualFileSystem.FileSystemObj;
import VirtualFileSystem.Folder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 29.06.13
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemController {
    public static final boolean LOCK = true;
    public static final boolean UNLOCK = false;
    private Folder currentFolder = FileSystem.getInstance().ROOT_FOLDER;

    private static final FileSystem fileSystem = FileSystem.getInstance();
    private User user;

    public enum Commands {
        MD, PING, CD, MF, LOCK, UNLOCK, RD
    }

    public List<String> doCommand(String messageReceived, User user) {
        this.user = user;
        try{
            ArrayList<String> messageAsArray = new ArrayList<String>(Arrays.asList( messageReceived.split(" ")));
            Commands command =  Commands.valueOf(messageAsArray.get(0).toUpperCase())  ;
            messageAsArray.remove(0);

            synchronized (fileSystem)  {
                switch (command){
                    case MD: return createFolder(messageAsArray);
                    case CD: return changeCurrentFolder(messageAsArray);
                    case MF: return makeFile(messageAsArray);
                    case LOCK: return lockOrUnlockFile(messageAsArray, LOCK);
                    case UNLOCK: return lockOrUnlockFile(messageAsArray, UNLOCK);
                    case RD: return removeFolder(messageAsArray);
                    case PING: return answer("pong");
                    default:  return answer("Unhandle command");

                }
            }
        }    catch (IllegalArgumentException e)  {
            return answer("Wrong command");

        }


    }

    private List<String> removeFolder(ArrayList<String> messageAsArray) {
        FileSystemObj objToRemove = checkPath(messageAsArray);
        if (objToRemove == null)  return answer("Bad path");
        if (objToRemove.getClass() != Folder.class)  return answer("For remove file use DEL command") ;
        Folder folderToDelete = (Folder) objToRemove;
        for (FileSystemObj fileSystemObj: folderToDelete.folderList) {
            if (fileSystemObj.getClass() == Folder.class) return answer("Deleting Folder consist subFolders");  }
        return (folderToDelete != currentFolder ) ? answerRemovingFolder(folderToDelete)  : answer("Bad path");

    }

    private List<String> answerRemovingFolder(FileSystemObj objToRemove) {
        String answerIfRemove = "Remove folder " + objToRemove.name;
        String deleteTryingMessage = FileSystem.getInstance().deleteObject(objToRemove);
        return deleteTryingMessage.equalsIgnoreCase(answerIfRemove)? answerWithFileSystemChanges(answerIfRemove): answer(deleteTryingMessage);
    }



    public List<String> lockOrUnlockFile(ArrayList<String> messageAsArray, boolean lockOrUnlock) {
        FileSystemObj objToLock = checkPath(messageAsArray);
        return ((objToLock != null) && (objToLock.getClass()==File.class)) ? answerLockForCurrentUser((File) objToLock, lockOrUnlock): answer("Bad path");
    }

    private List<String> answerLockForCurrentUser(File fileToLock, boolean lockOrUnlock) {
        if (lockOrUnlock) {
        return fileToLock.users_locks.contains(user.userName) ?  answer("You already lock this file"): lockFileAndAnswer(fileToLock);
        }else {
            return  fileToLock.users_locks.contains(user.userName) ?  unLockFileAndAnswer(fileToLock) : answer("You already unlock this file");
        }

    }

    private List<String> unLockFileAndAnswer(File fileToLock) {
        fileToLock.users_locks.remove(user.userName);
        String changes = "Unlock " + fileToLock.name;

        return answerWithFileSystemChanges(changes);
    }

    private List<String> lockFileAndAnswer(File fileToLock) {
        fileToLock.users_locks.add(user.userName);
        String changes = "Lock " + fileToLock.name;

        return answerWithFileSystemChanges(changes);
    }

    private FileSystemObj checkPath(ArrayList<String> messageAsArray) {
        return currentFolder.checkPath(new ArrayList(Arrays.asList(messageAsArray.get(0).split("\\\\"))), currentFolder);
    }

    private List<String> makeFile(ArrayList<String> messageAsArray) {
        File file = new File(messageAsArray.get(0), currentFolder);
        return file.exist? answerWithFileSystemChanges("Create " + file.name): answer("Bad path");  //To change body of created methods use File | Settings | File Templates.
    }

    private List<String> answerWithFileSystemChanges(String changes) {
        user.iMakeChanges(changes);
        return answer(changes);
    }



    public List<String> changeCurrentFolder(ArrayList<String> messageAsArray) {

        FileSystemObj newCurrentFolder = checkPath(messageAsArray);
        if ((newCurrentFolder) != null && (newCurrentFolder.getClass() ==Folder.class ))  {
            currentFolder = (Folder) newCurrentFolder;
            return answer ("Current directory " + newCurrentFolder.name );
        }   else return answer("Can not change directory");
    }

    private List<String> createFolder(ArrayList<String> messageAsArray) {

        Folder folder = new Folder(messageAsArray.get(0), currentFolder);

        return folder.exist? answerWithFileSystemChanges("Create " + folder.name): answer("Bad path");

    }

    private List <String> answer(String message) {
        ArrayList <String> answer =   new ArrayList<String>();
        answer.add(message);
        return answer;
    }
}
