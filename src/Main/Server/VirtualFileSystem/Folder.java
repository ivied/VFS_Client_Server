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


    public Folder(String path,Folder currentFolder){
        List<String> parentFoldersName = new ArrayList(Arrays.asList(path.split("\\\\")));


        name = parentFoldersName.get(parentFoldersName.size()-1);
        if (name.equalsIgnoreCase("C:")) return;


        if (checkPath(parentFoldersName, currentFolder)==null){

            parentFolder.folderList.add(this);
            exist = true;
        }


    }



    protected Folder checkPath (List<String> parentFoldersName, Folder startFolder) {


        if (parentFoldersName.get(0).equals("C:")) {
            startFolder =   FileSystem.getInstance().ROOT_FOLDER ;

        }else{

            parentFoldersName.add(0, startFolder.name) ;
        }
        Folder savedFolder = startFolder;
        for (int subFolderNumber = 1; subFolderNumber != parentFoldersName.size(); subFolderNumber++ ){
            String subFolderName = parentFoldersName.get(subFolderNumber);
            this.parentFolder = startFolder;

            startFolder = startFolder.checkFolderExist(startFolder.folderList, subFolderName);
            if ((startFolder == null)&&(subFolderNumber == parentFoldersName.size()-1)) return null;
            if (startFolder == null ) return savedFolder;




        }
        return startFolder;


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
