package VirtualFileSystem;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 29.06.13
 * Time: 2:21
 * To change this template use File | Settings | File Templates.
 */
abstract public class FileSystemObj {
   // protected final static Folder ROOT_FOLDER = new Folder ("C:", null);
    public String name;

    public Folder parentFolder;
    public boolean exist = false;

    public FileSystemObj(String path,Folder currentFolder){

        List<String> parentFoldersName = getParentFoldersName(path);

        setName(parentFoldersName);

        if (name.equalsIgnoreCase("C:")) return;

        FileSystemObjWithFlag newObj = FileSystem.getInstance().checkPath(parentFoldersName, currentFolder);
        if (newObj.fileSystemObj==null){
            parentFolder = newObj.parentFolder;
            FileSystem.getInstance().addObjWithLexicOrder(parentFolder, this);
           // parentFolder.folderList.add(this);

            exist = true;
        }


    }




    protected List<String> getParentFoldersName(String path) {
        return new ArrayList(Arrays.asList(path.split("\\\\")));

    }

    protected void setName(List<String> parentFoldersName) {
        name = parentFoldersName.get(parentFoldersName.size()-1);
    }


}
