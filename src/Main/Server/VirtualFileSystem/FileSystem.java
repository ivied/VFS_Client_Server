package VirtualFileSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public Folder createFolder(ArrayList<String> messageAsArray, Folder currentFolder) {

        return new Folder(messageAsArray.get(0), currentFolder);

    }

    public File createFile(ArrayList<String> messageAsArray, Folder currentFolder) {

        return new File(messageAsArray.get(0), currentFolder);

    }

    public String deleteObject(FileSystemObj objToRemove) {

        return (objToRemove.getClass() == File.class) ? deleteFile((File) objToRemove): deleteFolder((Folder) objToRemove);
    }

    private String deleteFolder(Folder folderToRemove) {
         return locksCheck(folderToRemove)  ? "Folder locked" : answerRemoveObj(folderToRemove);

    }

    private String answerRemoveObj(FileSystemObj folderToRemove) {
        folderToRemove.parentFolder.folderList.remove(folderToRemove);
        return "Remove " + folderToRemove.name;
    }

    private boolean locksCheck(Folder folderToRemove) {
        for (FileSystemObj fileSystemObj : folderToRemove.folderList){
            boolean objLock =  (fileSystemObj.getClass() == File.class) ? !((File) fileSystemObj).users_locks.isEmpty(): locksCheck((Folder)fileSystemObj);
            if (objLock) return true;
        }
        return false;
    }

    private String deleteFile(File fileToRemove) {

        return fileToRemove.users_locks.isEmpty() ? answerRemoveObj(fileToRemove) : "File locked";
    }


    public List<String> printStructure() {
        List<String> structure = getFolderStructure(ROOT_FOLDER, 0);
        structure.add(0, "C:");
        return structure;
    }

    private List<String> getFolderStructure(Folder folder, int depth) {
        List<String> structure =  new ArrayList<>();
        for (FileSystemObj fileSystemObj : folder.folderList) {
            structure.add(folderNameWithIndent(fileSystemObj, depth));
               if (fileSystemObj.getClass() == Folder.class) {
                   List<String> subFolders = getFolderStructure((Folder)fileSystemObj, depth+1);
                   for (String subFolder : subFolders ){
                           structure.add(subFolder);
                   }
               }
        }
        return structure;  //To change body of created methods use File | Settings | File Templates.
    }

    private String folderNameWithIndent(FileSystemObj fileSystemObj, int depth) {
        StringBuilder nameWithIndent = new StringBuilder();
        for (int i = 0; i<depth; i++){
            nameWithIndent.append("| ");
        }
        nameWithIndent.append("|_" + fileSystemObj.name) ;
        if (fileSystemObj.getClass() == File.class) appendLocks(nameWithIndent, ((File) fileSystemObj).users_locks);
        return nameWithIndent.toString();  //To change body of created methods use File | Settings | File Templates.
    }

    private void appendLocks(StringBuilder nameWithIndent, ArrayList<String> users_locks) {
        for (String lockerName : users_locks) {
            nameWithIndent.append("[LOCKED by " + lockerName + "]");
        }
    }

    public void addObjWithLexicOrder(Folder parentFolder, FileSystemObj fileToAdd) {
        int i;
        if (parentFolder.folderList.isEmpty()) {
            parentFolder.folderList.add(fileToAdd);
            return;
        }
        String [] names = new String[parentFolder.folderList.size()+1];
        for( i = 0; i <  parentFolder.folderList.size(); i++) {

            names[i] = parentFolder.folderList.get(i).name;


        }
        names[parentFolder.folderList.size()]  = fileToAdd.name;
        Arrays.sort(names);
        for(i = 0; i <  names.length; i++) {
            if(fileToAdd.name.equalsIgnoreCase(names[i]))parentFolder.folderList.add(i,fileToAdd);
        }



    }


    public FileSystemObjWithFlag checkPath(List<String> parentFoldersName, Folder startFolder) {

        if (parentFoldersName.get(0).equalsIgnoreCase("C:")) {
            startFolder =   FileSystem.getInstance().ROOT_FOLDER ;

        }else{

            parentFoldersName.add(0, startFolder.name) ;
        }
        return checkAllParentFolders( parentFoldersName, startFolder);

    }

    private FileSystemObjWithFlag checkAllParentFolders(List<String> parentFoldersName, Folder startFolder) {
        Folder savedFolder = startFolder;
        for (int subObjNumber = 1; subObjNumber != parentFoldersName.size(); subObjNumber++ ){
            String subObjName = parentFoldersName.get(subObjNumber);


            FileSystemObj fileSystemObj = checkObjExist(startFolder.folderList, subObjName);

            if (fileSystemObj != null && fileSystemObj.getClass() == File.class)return new FileSystemObjWithFlag( fileSystemObj, FileSystemObjWithFlag.FILE,null);
            if ((fileSystemObj == null)&&(subObjNumber == parentFoldersName.size()-1)) return new FileSystemObjWithFlag( null, FileSystemObjWithFlag.CREATE_OBJ, startFolder);
            startFolder = (Folder) fileSystemObj;

            if (startFolder == null ) return new FileSystemObjWithFlag( savedFolder, FileSystemObjWithFlag.CURRENT_FOLDER, null);

        }
        return new FileSystemObjWithFlag( startFolder, FileSystemObjWithFlag.NEW_FOLDER, null);
    }


    protected FileSystemObj checkObjExist(ArrayList<FileSystemObj> reviseFolder, String subFolderName) {
        for(FileSystemObj fileSystemObj : reviseFolder) {
            if( fileSystemObj.name.equalsIgnoreCase(subFolderName)  )
                return  fileSystemObj;
        }
        return null;
    }
}
