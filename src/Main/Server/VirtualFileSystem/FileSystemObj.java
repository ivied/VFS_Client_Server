package VirtualFileSystem;

import java.util.*;

/**
 *  Абстрактный объект файловой системы
 *
 * Created with IntelliJ IDEA.
 * User: Serv
 */
abstract public class FileSystemObj {

    /**
     *  Флаг присваивается объекту системы в методе FileSystemSinglеton.chekPath()
     *  если невозможно найти указанный путь в дирректории  и это не последний объект в  пути
     */
    public final static int CURRENT_FOLDER_FLAG = 0;
    /**
     *  Флаг присваивается объекту системы в методе FileSystemSingelton.chekPath()
     *  если невозможно найти указанный путь в дирректории, но это последний  объект в  пути
     */
    public final static int NEW_FOLDER_FLAG = 1;
    /**
     *  Флаг присваивается объекту системы в методе FileSystemSingelton.chekPath()
     *  если путь заканичвается файлом
     */
    public final static int FILE_FLAG = 2;
    public String name;
    public Folder parentFolder;
    public boolean exist = false;
    public int checkingFlag;

    /**
     * Cоздает объект. Помещает его в файловую систему если корректо задан путь
     */
    public FileSystemObj(String path,Folder currentFolder){

        List<String> parentFoldersName = getParentFoldersName(path);


        setName(parentFoldersName);
        if (name.equalsIgnoreCase("C:")) return;
        FileSystemObj newObj = FileSystemSingleton.getInstance().checkPath(parentFoldersName, currentFolder);
        if (newObj == null){
            parentFoldersName.remove(parentFoldersName.size() - 1);
            parentFolder = parentFoldersName.isEmpty() ? parentFolder = currentFolder : (Folder) FileSystemSingleton.getInstance().checkPath(parentFoldersName, currentFolder);
            FileSystemSingleton.getInstance().addObjWithLexicOrder(parentFolder, this);
            exist = true;
        }

    }


    public boolean isFile (){
        return this.getClass() == File.class;
    }

    public boolean isFolder (){
        return this.getClass() == Folder.class;
    }

    public boolean isFlagingCurrent (){
        return checkingFlag == CURRENT_FOLDER_FLAG;
    }

    public boolean isFlagingNew (){
        return checkingFlag == NEW_FOLDER_FLAG;
    }

    public boolean isFlagingFile (){
        return checkingFlag == FILE_FLAG;
    }

    public static List<String> getParentFoldersName(String path) {
        return new ArrayList<String>(Arrays.asList(path.split("\\\\")));

    }

    protected FileSystemObj setFlag(int flag){
        checkingFlag = flag;
        return this;
    }



    private void setName(List<String> parentFoldersName) {
        name = parentFoldersName.get(parentFoldersName.size()-1);
    }




}
