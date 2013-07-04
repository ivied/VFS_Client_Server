package VirtualFileSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс отвечаеющий за фаловую сиситему. Сделан по паттерну Singleton.
 * Created with IntelliJ IDEA.
 * User: Serv
 */
public class FileSystemSingleton {

    public final String ROOT = "C:";

    private static FileSystemSingleton instance;
    public   Folder ROOT_FOLDER =null;

    private FileSystemSingleton(){
         ROOT_FOLDER = new Folder ( ROOT , null);
    }

    public static FileSystemSingleton getInstance() {
        if (instance == null) {
            instance = new FileSystemSingleton();
        }
        return instance;
    }


    /**
     * Метод проверят наличие файла в дирректории. Возвращает объект файловой системы с флгом.
     * Значение флага зависит от того чем закончилась проверка пути
     */
    public FileSystemObj checkPath(List<String> parentFoldersName, Folder startFolder) {

        List<String> parentsName = new ArrayList<>(parentFoldersName);

        startFolder = correctionPathOrCurrentFolder(startFolder, parentsName);
        return checkAllParentFolders( parentsName, startFolder);

    }



    public Folder createFolder(ArrayList<String> messageAsArray, Folder currentFolder) {

        return new Folder(messageAsArray.get(0), currentFolder);

    }

    public File createFile(ArrayList<String> messageAsArray, Folder currentFolder) {

        return new File(messageAsArray.get(0), currentFolder);

    }

    /**
     *  Метод удаляет объект если он не заблокирован
     */
    public String deleteObject(FileSystemObj objToRemove) {

        return (objToRemove.isFile()) ? deleteFile((File) objToRemove): deleteFolder((Folder) objToRemove);
    }

    /**
     *   Метод возвращает полную структуру файловой системы в виде List
     */
    public List<String> printStructure() {
        List<String> structure = new ArrayList<String>();
        getFolderStructure(structure, ROOT_FOLDER, 0);
        structure.add(0, ROOT);
        return structure;
    }

    /**
     * Добавляет элемент в файловую систему
     */
    public void addObjWithLexicOrder(Folder parentFolder, FileSystemObj objToAdd) {
        if (parentFolder.isClear()) {
            parentFolder.folderList.add(objToAdd);
            return;
        }
        String [] subFoldersNames = new String[parentFolder.folderList.size()+1];
        fillNamesArray(parentFolder, objToAdd, subFoldersNames);
        Arrays.sort(subFoldersNames);
        addToFolderWithLexicOrder(parentFolder, objToAdd, subFoldersNames);
    }


    private List<String> getFolderStructure(List<String> structure,Folder folder, int depth) {

        for (FileSystemObj fileSystemObj : folder.folderList) {
            addObjToPrintStructure(depth, structure, fileSystemObj);
        }
        return structure;
    }

    private FileSystemObj checkAllParentFolders(List<String> parentFoldersName, Folder startFolder) {
        Folder savedFolder = startFolder;


        for (int subObjNumber = 1; subObjNumber != parentFoldersName.size(); subObjNumber++ ){
            String subObjName = parentFoldersName.get(subObjNumber);


            FileSystemObj fileSystemObj = checkObjExist(startFolder.folderList, subObjName);

            if (fileSystemObj != null && fileSystemObj.isFile())return fileSystemObj.setFlag(FileSystemObj.FILE_FLAG);
            if ((fileSystemObj == null)&&(subObjNumber == parentFoldersName.size()-1)) return null;


            if (fileSystemObj == null ) return savedFolder.setFlag(FileSystemObj.CURRENT_FOLDER_FLAG);
            startFolder = (Folder) fileSystemObj;
        }
        return startFolder.setFlag(FileSystemObj.NEW_FOLDER_FLAG);
    }


    private void addObjToPrintStructure(int depth, List<String> structure, FileSystemObj fileSystemObj) {
        structure.add(folderNameWithIndent(fileSystemObj, depth));
        if (fileSystemObj.isFolder()) {
          getFolderStructure(structure, (Folder)fileSystemObj, depth+1);

        }
    }


    private String deleteFolder(Folder folderToRemove) {
        return locksCheck(folderToRemove)  ? "Folder locked" : answerRemoveObj(folderToRemove);

    }

    private String answerRemoveObj(FileSystemObj folderToRemove) {
        folderToRemove.parentFolder.folderList.remove(folderToRemove);
        return "Remove " + folderToRemove.name;
    }

    private String deleteFile(File fileToRemove) {

        return fileToRemove.users_locks.isEmpty() ? answerRemoveObj(fileToRemove) : "File locked";
    }

    private boolean locksCheck(Folder folderToRemove) {
        for (FileSystemObj fileSystemObj : folderToRemove.folderList){
            boolean objLock =  (fileSystemObj.isFile()) ? ((File) fileSystemObj).isLock(): locksCheck((Folder)fileSystemObj);
            if (objLock) return true;
        }
        return false;
    }

    private FileSystemObj checkObjExist(ArrayList<FileSystemObj> reviseFolder, String subFolderName) {
        for(FileSystemObj fileSystemObj : reviseFolder) {
            if( fileSystemObj.name.equalsIgnoreCase(subFolderName)  )
                return  fileSystemObj;
        }
        return null;
    }


    private void addToFolderWithLexicOrder(Folder parentFolder, FileSystemObj objToAdd, String[] subFoldersNames) {
        int i;
        for(i = 0; i <  subFoldersNames.length; i++) {
            if(objToAdd.name.equalsIgnoreCase(subFoldersNames[i])){
                if (checkObjExist(parentFolder.folderList, objToAdd.name) == null) parentFolder.folderList.add(i,objToAdd);
                return;
            }
        }
    }

    private void fillNamesArray(Folder parentFolder, FileSystemObj objToAdd, String[] subFoldersNames) {
        int i;
        for( i = 0; i <  parentFolder.folderList.size(); i++) {

            subFoldersNames[i] = parentFolder.folderList.get(i).name;

        }
        subFoldersNames[parentFolder.folderList.size()]  = objToAdd.name;
    }

    private Folder correctionPathOrCurrentFolder(Folder startFolder, List<String> parentsName) {
        if (parentsName.get(0).equalsIgnoreCase("C:")) {
            startFolder =   FileSystemSingleton.getInstance().ROOT_FOLDER ;

        }else{

            parentsName.add(0, startFolder.name) ;
        }
        return startFolder;
    }

    private String folderNameWithIndent(FileSystemObj fileSystemObj, int depth) {
        StringBuilder nameWithIndent = new StringBuilder();
        for (int i = 0; i<depth; i++){
            nameWithIndent.append("| ");
        }
        nameWithIndent.append("|_" + fileSystemObj.name) ;
        if (fileSystemObj.getClass() == File.class) appendLocks(nameWithIndent, ((File) fileSystemObj).users_locks);
        return nameWithIndent.toString();
    }


    private void appendLocks(StringBuilder nameWithIndent, ArrayList<String> users_locks) {
        for (String lockerName : users_locks) {
            nameWithIndent.append("[LOCKED by " + lockerName + "]");
        }
    }
}
