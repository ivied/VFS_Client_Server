import VirtualFileSystem.*;

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
    Commands command;
    public enum Commands {
        MD, PING, CD, MF, LOCK, UNLOCK, RD, DEL, DELTREE, COPY, MOVE, PRINT
    }

    public List<String> doCommand(String messageReceived, User user) {
        this.user = user;
        try{
            ArrayList<String> messageAsArray = new ArrayList<String>(Arrays.asList( messageReceived.split(" ")));
            command =  Commands.valueOf(messageAsArray.get(0).toUpperCase())  ;
            messageAsArray.remove(0);

            synchronized (fileSystem)  {
                switch (command){
                    case MD: return createFolder(messageAsArray);
                    case CD: return changeCurrentFolder(messageAsArray);
                    case MF: return makeFile(messageAsArray);
                    case LOCK: return lockOrUnlockFile(messageAsArray, LOCK);
                    case UNLOCK: return lockOrUnlockFile(messageAsArray, UNLOCK);
                    case RD: return removeObj(messageAsArray);
                    case DEL: return removeObj(messageAsArray);
                    case DELTREE: return removeObj(messageAsArray);
                    case COPY: return copyOrMoveObj(messageAsArray);
                    case MOVE: return copyOrMoveObj(messageAsArray);
                    case PRINT: return printFileSystem();
                    case PING: return answer("pong");

                    default:  return answer("Unhandle command");

                }
            }
        }    catch (IllegalArgumentException e)  {
            return answer("Wrong command");

        }


    }

    private List<String> printFileSystem() {
        List<String> structure =  FileSystem.getInstance().printStructure();
        for (String line : structure){
            answer(line);
        }
        return  structure;

    }

    private List<String> copyOrMoveObj(ArrayList<String> messageAsArray) {
        FileSystemObjWithFlag objToCopy = checkPath(messageAsArray.get(0));
        FileSystemObjWithFlag objToPaste = checkPath(messageAsArray.get(1));
        if((objToCopy.fileSystemObj == null) || (objToPaste.fileSystemObj ==  null) ) return answer("Bad path") ;
        if(command == Commands.MOVE) {
            List<String> ifRemoveAnswer = new ArrayList<>() ;
            ifRemoveAnswer.add("Remove " + objToCopy.fileSystemObj.name);
            command = (objToCopy.fileSystemObj.getClass() == File.class)? Commands.DEL : Commands.DELTREE;
            if (!removeObj(messageAsArray).equals(ifRemoveAnswer)) return answer("Cant move this");
        }
        if ((objToPaste.flag != FileSystemObjWithFlag.NEW_FOLDER) || ((objToCopy.flag != FileSystemObjWithFlag.NEW_FOLDER)&&((objToCopy.flag != FileSystemObjWithFlag.FILE)))) return answer("Bad path");
        fileSystem.addObjWithLexicOrder((Folder) objToPaste.fileSystemObj, objToCopy.fileSystemObj);
        return answerWithFileSystemChanges("Copy " + objToCopy.fileSystemObj.name + " to " + objToPaste.fileSystemObj.name);
    }


    private List<String> removeObj(ArrayList<String> messageAsArray) {
        FileSystemObj objToRemove = checkPath(messageAsArray.get(0)).fileSystemObj;
        if (objToRemove == null)  return answer("Bad path");
        return removingDependenciesFromCommand( objToRemove);


    }

    private List<String> removingDependenciesFromCommand( FileSystemObj objToRemove) {
        switch (command){
            case RD:
            case DELTREE:
                if (objToRemove.getClass() != Folder.class)  return answer("For remove file use DEL command") ;
                Folder folderToDelete = (Folder) objToRemove;

                List<String> answer =  answerIfFolderHaveSubfolders(folderToDelete);
                if(answer !=  null) return answer;
                return (folderToDelete != currentFolder ) ? answerRemovingObj(folderToDelete)  : answer("Bad path");
            case DEL:

                return  (objToRemove.getClass() == File.class) ? answerRemovingObj(objToRemove) :   answer("For remove folder use RD or DELTREE command") ;
            default:
                return null;
        }
    }

    private List<String> answerIfFolderHaveSubfolders( Folder folderToDelete) {
        if(command == Commands.RD) for (FileSystemObj fileSystemObj: folderToDelete.folderList) {
            if (fileSystemObj.getClass() == Folder.class) return answer("Deleting Folder consist subFolders. Try use DELTREE command");
        }
        return null;
    }

    private List<String> answerRemovingObj(FileSystemObj objToRemove) {
        String answerIfRemove = "Remove " + objToRemove.name;
        String deleteTryingMessage = FileSystem.getInstance().deleteObject(objToRemove);
        return deleteTryingMessage.equalsIgnoreCase(answerIfRemove)? answerWithFileSystemChanges(answerIfRemove): answer(deleteTryingMessage);
    }



    public List<String> lockOrUnlockFile(ArrayList<String> messageAsArray, boolean lockOrUnlock) {
        FileSystemObj objToLock = checkPath(messageAsArray.get(0)).fileSystemObj;
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

    private FileSystemObjWithFlag checkPath(String fileSystemObjPath) {
        return FileSystem.getInstance().checkPath(new ArrayList(Arrays.asList(fileSystemObjPath.split("\\\\"))), currentFolder);
    }

    private List<String> makeFile(ArrayList<String> messageAsArray) {
        File file = FileSystem.getInstance().createFile(messageAsArray, currentFolder);
        return file.exist? answerWithFileSystemChanges("Create " + file.name): answer("Bad path");  //To change body of created methods use File | Settings | File Templates.
    }

    private List<String> answerWithFileSystemChanges(String changes) {
        user.iMakeChanges(changes);
        return answer(changes);
    }



    public List<String> changeCurrentFolder(ArrayList<String> messageAsArray) {
        FileSystemObjWithFlag fileSystemObjWithFlag = checkPath(messageAsArray.get(0));
        if (fileSystemObjWithFlag.fileSystemObj == null )return  answer("Can not change directory");
        FileSystemObj newCurrentFolder = fileSystemObjWithFlag.fileSystemObj;
        if ( (newCurrentFolder.getClass() == Folder.class ))  {
            currentFolder = (Folder) newCurrentFolder;
            return answer ("Current directory " + newCurrentFolder.name );
        }   else return answer("Can not change directory");
    }

    private List<String> createFolder(ArrayList<String> messageAsArray) {

        Folder folder = FileSystem.getInstance().createFolder(messageAsArray, currentFolder);

        return folder.exist? answerWithFileSystemChanges("Create " + folder.name): answer("Bad path");

    }

    private List <String> answer(String message) {
        ArrayList <String> answer =   new ArrayList<String>();
        answer.add(message);
        return answer;
    }
}
