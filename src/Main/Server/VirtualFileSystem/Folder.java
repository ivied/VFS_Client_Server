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

    protected ArrayList<FileSystemObj> folderList = new ArrayList<FileSystemObj>();

    public boolean exist = false;
   // protected  Folder currentDirectory = ROOT_FOLDER;


    public Folder(String path,Folder currentDirectory){
        List<String> parentFoldersName = new ArrayList(Arrays.asList(path.split("\\\\")));

        name = parentFoldersName.get(parentFoldersName.size()-1);
        if (name.equalsIgnoreCase("C:")) return;
        Folder startFolder;

        if (parentFoldersName.get(0).equals("C:")) {
        startFolder =   FileSystem.getInstance().ROOT_FOLDER ;
        parentFoldersName.remove(0) ;
        }else{
            startFolder = currentDirectory ;
        }

        if (checkPath(parentFoldersName, startFolder)){

            parentFolder.folderList.add(this);
            exist = true;
        }

    }



    private boolean checkPath(List<String> parentFoldersName, Folder currentFolder) {

        for (int currentElement = 0; currentElement < parentFoldersName.size(); currentElement++ ){
            String subFolderName = parentFoldersName.get(currentElement);
            this.parentFolder = currentFolder;

            currentFolder = currentFolder.checkFolderExist(currentFolder.folderList, subFolderName);
            if ((currentFolder == null)&&(currentElement == parentFoldersName.size()-1)) return true;
            if (currentFolder == null ) return false;




        }
        return false;


    }

    private Folder checkFolderExist(ArrayList<FileSystemObj> reviseFolder, String subFolderName) {
        for(FileSystemObj fileSystemObj : reviseFolder) {
           String lol =  fileSystemObj.getClass().toString();
            if( (fileSystemObj.name.equalsIgnoreCase(subFolderName) ) && (lol.equalsIgnoreCase("class VirtualFileSystem.Folder")) )
                return (Folder) fileSystemObj;
        }
        return null;
    }

   /* public ArrayList<FileSystemObj> getFolderList (){
         return folderList;
    }*/


}
