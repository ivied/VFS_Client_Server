import VirtualFileSystem.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс обеспечивающий контроль за файловой системой.
 * Для каждого пользователя обязательно иметь свой контроллер.
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class FileSystemController {
    public static final boolean LOCK = true;
    public static final boolean UNLOCK = false;
    private Folder currentFolder = FileSystemSingleton.getInstance().ROOT_FOLDER;

    private static final FileSystemSingleton fileSystem = FileSystemSingleton.getInstance();
    private User user;
    Commands command;

    /**
     * Комманды файловой системы
     */
    public enum Commands  {
        MD(1),
        PING(0),
        CD(1),
        MF(1),
        LOCK(1),
        UNLOCK(1),
        RD(1),
        DEL(1),
        DELTREE(1),
        COPY(2),
        MOVE(2),
        PRINT(0);
        protected final int signature;
        Commands (int signature){
             this.signature = signature;
        }
    }

    /**
     * Метод позволяет User управлять файловой системой
     * Действия пользователя синхронизируется с изменениями файловой системы
     */
    public List<String> doCommand(String messageReceived, User user) {
        this.user = user;

            ArrayList<String> messageAsArray = new ArrayList<String>(Arrays.asList( messageReceived.split(" ")));
            command = getCommandAndValidation(messageAsArray);
            if (command== null)return answer("Bad command");
            //command =  Commands.valueOf(messageAsArray.get(0).toUpperCase())  ;
           // messageAsArray.remove(0);

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

    }





    private List<String> printFileSystem() {
        List<String> structure =  fileSystem.printStructure();
        for (String line : structure){
            answer(line);
        }
        return  structure;

    }

    private List<String> lockOrUnlockFile(ArrayList<String> messageAsArray, boolean lockOrUnlock) {
        FileSystemObj objToLock = checkPath(messageAsArray.get(0));
        return ((objToLock != null) && objToLock.isFile()) ? answerLockForCurrentUser((File) objToLock, lockOrUnlock): answer("Bad path");
    }

    private List<String> copyOrMoveObj(ArrayList<String> messageAsArray) {
        FileSystemObj objToCopy = checkPath(messageAsArray.get(0));
        FileSystemObj objToPaste = checkPath(messageAsArray.get(1));
        if((objToCopy == null) || (objToPaste ==  null) ) return answer("Bad path") ;
        if(command == Commands.MOVE) {
            List<String> ifRemoveAnswer = new ArrayList<>() ;
            ifRemoveAnswer.add("Remove " + objToCopy.name);
            command = (objToCopy.isFile())? Commands.DEL : Commands.DELTREE;
            if (!removeObj(messageAsArray).equals(ifRemoveAnswer)) return answer("Cant move this");
        }
        if (!objToPaste.isFlagingNew() || (!objToCopy.isFlagingNew() && !objToCopy.isFlagingFile())) return answer("Bad path");
        copyObj(messageAsArray, objToCopy);
        return answerWithFileSystemChanges("Copy " + objToCopy.name + " to " + objToPaste.name);
    }




    private List<String> removeObj(ArrayList<String> messageAsArray) {
        FileSystemObj objToRemove = checkPath(messageAsArray.get(0));
        if (objToRemove == null)  return answer("Bad path");
        return removingDependenciesFromCommand( objToRemove);


    }

    private List<String> makeFile(ArrayList<String> messageAsArray) {
        File file = fileSystem.createFile(messageAsArray, currentFolder);
        return file.exist? answerWithFileSystemChanges("Create " + file.name): answer("Bad path");
    }


    private List<String> createFolder(ArrayList<String> messageAsArray) {

        Folder folder = fileSystem.createFolder(messageAsArray, currentFolder);

        return folder.exist? answerWithFileSystemChanges("Create " + folder.name): answer("Bad path");

    }

    private FileSystemObj copyObj(ArrayList<String> messageAsArray, FileSystemObj objToCopy) {
        messageAsArray.add(0, messageAsArray.get(1).concat("\\" + objToCopy.name));
        return (objToCopy.isFile()) ?  copyFile(messageAsArray, (File) objToCopy) : copyFolder(messageAsArray, (Folder) objToCopy);
    }

    private Folder copyFolder(ArrayList<String> messageAsArray, Folder objToCopy) {
        Folder folder = fileSystem.createFolder(messageAsArray, currentFolder);
        folder.folderList = new ArrayList<>((objToCopy).folderList);
        return folder;
    }

    private File copyFile(ArrayList<String> messageAsArray, File objToCopy) {
        File file = fileSystem.createFile(messageAsArray, currentFolder);
        file.users_locks = new ArrayList<>((objToCopy).users_locks);
        return  file;
    }


    private List<String> answerWithFileSystemChanges(String changes) {
        user.iMakeChanges(changes);
        return answer(changes);
    }

    private List <String> answer(String message) {
        ArrayList <String> answer =   new ArrayList<String>();
        answer.add(message);
        return answer;
    }

    private FileSystemObj checkPath(String fileSystemObjPath) {
        return fileSystem.checkPath(FileSystemObj.getParentFoldersName(fileSystemObjPath), currentFolder);
    }



    private List<String> removingDependenciesFromCommand( FileSystemObj objToRemove) {
        switch (command){
            case RD:
            case DELTREE:

                if (objToRemove.isFile())  return answer("For remove file use DEL command") ;
                Folder folderToDelete = (Folder) objToRemove;

                List<String> answer =  answerIfFolderHaveSubfolders(folderToDelete);
                if(answer !=  null) return answer;
                return (folderToDelete != currentFolder ) ? answerRemovingObj(folderToDelete)  : answer("Bad path");
            case DEL:

                return  (objToRemove.isFile()) ? answerRemovingObj(objToRemove) : answer("For remove folder use RD or DELTREE command") ;
            default:
                return null;
        }
    }

    private List<String> answerLockForCurrentUser(File fileToLock, boolean lockOrUnlock) {
        if (lockOrUnlock) {
        return fileToLock.users_locks.contains(user.userName) ?  answer("You already lock this file"): lockFileAndAnswer(fileToLock);
        }else {
            return  fileToLock.users_locks.contains(user.userName) ?  unLockFileAndAnswer(fileToLock) : answer("You already unlock this file");
        }

    }


    private List<String> answerIfFolderHaveSubfolders( Folder folderToDelete) {
        if(command == Commands.RD) for (FileSystemObj fileSystemObj: folderToDelete.folderList) {
            if (fileSystemObj.isFolder()) return answer("Deleting Folder consist subFolders. Try use DELTREE command");
        }
        return null;
    }

    private List<String> answerRemovingObj(FileSystemObj objToRemove) {
        String answerIfRemove = "Remove " + objToRemove.name;
        String deleteTryingMessage = fileSystem.deleteObject(objToRemove);
        return deleteTryingMessage.equalsIgnoreCase(answerIfRemove)? answerWithFileSystemChanges(answerIfRemove): answer(deleteTryingMessage);
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




    private List<String> changeCurrentFolder(ArrayList<String> messageAsArray) {
        FileSystemObj fileSystemObj = checkPath(messageAsArray.get(0));
        if (fileSystemObj == null )return  answer("Can not change directory");
        if ( fileSystemObj.isFolder())  {
            currentFolder = (Folder) fileSystemObj;
            return answer ("Current directory " + fileSystemObj.name );
        }   else return answer("Can not change directory");
    }

    private Commands getCommandAndValidation(ArrayList<String> messageAsArray) {
        try{
            Commands command = Commands.valueOf(messageAsArray.get(0).toUpperCase());
            messageAsArray.remove(0);
            if (command.signature == (messageAsArray.size())) return command;
        }    catch (IllegalArgumentException e)  {
            return null ;

        }
        return null;
    }

}
