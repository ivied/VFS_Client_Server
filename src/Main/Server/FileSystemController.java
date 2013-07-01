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
        MD, PING, CD, MF, LOCK, UNLOCK
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
                    case PING: return answer("pong");
                    default:  return answer("Unhandle command");

                }
            }
        }    catch (IllegalArgumentException e)  {
            return answer("Wrong command");

        }


    }

    public List<String> lockOrUnlockFile(ArrayList<String> messageAsArray, boolean lockOrUnlock) {
        FileSystemObj objToLock = checkPath(messageAsArray);
        return ((objToLock != null) && (objToLock.getClass().toString().equalsIgnoreCase("class VirtualFileSystem.File"))) ? answerLockForCurrentUser((File) objToLock, lockOrUnlock): answer("Bad path");
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
        return answer("Unlock " + fileToLock.name);
    }

    private List<String> lockFileAndAnswer(File fileToLock) {
        fileToLock.users_locks.add(user.userName);
        return answer("Lock " + fileToLock.name);
    }

    private FileSystemObj checkPath(ArrayList<String> messageAsArray) {
        return currentFolder.checkPath(new ArrayList(Arrays.asList(messageAsArray.get(0).split("\\\\"))), currentFolder);
    }

    private List<String> makeFile(ArrayList<String> messageAsArray) {
        File file = new File(messageAsArray.get(0), currentFolder);
        return file.exist? answer("Create " + file.name): answer("Bad path");  //To change body of created methods use File | Settings | File Templates.
    }

    public List<String> changeCurrentFolder(ArrayList<String> messageAsArray) {
        // currentFolder.makeDataForCheckPath(messageAsArray.get(0));
        FileSystemObj newCurrentFolder = checkPath(messageAsArray);
        if ((newCurrentFolder) != null && (newCurrentFolder.getClass().toString().equalsIgnoreCase("class VirtualFileSystem.Folder")))  {
            currentFolder = (Folder) newCurrentFolder;
            return answer ("Current directory " + newCurrentFolder.name );
        }   else return answer("Can not change directory");
    }

    private List<String> createFolder(ArrayList<String> messageAsArray) {

        Folder folder = new Folder(messageAsArray.get(0), currentFolder);

        return folder.exist? answer("Create " + folder.name): answer("Bad path");

    }

    private List <String> answer(String message) {
        ArrayList <String> answer =   new ArrayList<String>();
        answer.add(message);
        return answer;
    }
}
