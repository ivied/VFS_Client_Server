package VirtualFileSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 29.06.13
 * Time: 2:21
 * To change this template use File | Settings | File Templates.
 */
abstract public class FileSystemObj {
   // protected final static Folder ROOT_FOLDER = new Folder ("C:", null);
    String name;
    Folder parentFolder;
    public boolean exist = false;

    public FileSystemObj(String path,Folder currentFolder){

        List<String> parentFoldersName = getParentFoldersName(path);

        setName(parentFoldersName);

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
        return checkAllParentFolders( parentFoldersName, startFolder);

    }

    private Folder checkAllParentFolders(List<String> parentFoldersName, Folder startFolder) {
        Folder savedFolder = startFolder;
        for (int subObjNumber = 1; subObjNumber != parentFoldersName.size(); subObjNumber++ ){
            String subObjName = parentFoldersName.get(subObjNumber);
            this.parentFolder = startFolder;

            FileSystemObj fileSystemObj = startFolder.checkObjExist(startFolder.folderList, subObjName);

            if (fileSystemObj != null && fileSystemObj.getClass().toString().equalsIgnoreCase("class VirtualFileSystem.File"))return savedFolder;
            startFolder = (Folder) fileSystemObj;
            if ((startFolder == null)&&(subObjNumber == parentFoldersName.size()-1)) return null;
            if (startFolder == null ) return savedFolder;

        }
        return startFolder;
    }


    protected FileSystemObj checkObjExist(ArrayList<FileSystemObj> reviseFolder, String subFolderName) {
        for(FileSystemObj fileSystemObj : reviseFolder) {
            if( fileSystemObj.name.equalsIgnoreCase(subFolderName)  )
                return  fileSystemObj;
        }
        return null;
    }

    protected List<String> getParentFoldersName(String path) {
        return new ArrayList(Arrays.asList(path.split("\\\\")));

    }

    protected void setName(List<String> parentFoldersName) {
        name = parentFoldersName.get(parentFoldersName.size()-1);
    }
}
