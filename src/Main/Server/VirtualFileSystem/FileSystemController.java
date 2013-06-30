package VirtualFileSystem;

import VirtualFileSystem.FileSystem;
import VirtualFileSystem.Folder;

import java.lang.reflect.Array;
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
    private Folder currentFolder = FileSystem.getInstance().ROOT_FOLDER;

    private static final FileSystem fileSystem = FileSystem.getInstance();
    public enum Commands {
         MD, PING, CD
    }

    public List<String> doCommand(String messageReceived) {

        try{
            ArrayList<String> messageAsArray = new ArrayList<String>(Arrays.asList( messageReceived.split(" ")));
            Commands command =  Commands.valueOf(messageAsArray.get(0).toUpperCase())  ;
            messageAsArray.remove(0);

            synchronized (fileSystem)  {
            switch (command){
                case MD: return createFolder(messageAsArray);
                case CD: return changeCurrentFolder(messageAsArray);
                case PING: return answer("pong");
                default:  return answer("Unhandle command");

            }
            }
        }    catch (IllegalArgumentException e)  {
            return answer("Wrong command");

        }


    }

    public List<String> changeCurrentFolder(ArrayList<String> messageAsArray) {
       // currentFolder.makeDataForCheckPath(messageAsArray.get(0));
         Folder newCurrentFolder = currentFolder.checkPath(new ArrayList(Arrays.asList(messageAsArray.get(0).split("\\\\"))), currentFolder);
       if (newCurrentFolder == null) return answer("Can not change directory");
        else {
           currentFolder = newCurrentFolder;
           return answer ("Current directory " + newCurrentFolder.name );
       }
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
