import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainFileHelperTest {

    static int sourceDirLengthAfter;
    static int targetDirLengthAfter;

    public static void makeDirectoriesReady(int sourceDirLengthBefore, String sourceDir, String... targetDirs){
        //Создаем объекты File на основе строк с адресами тестовых папок
        File sourceFileDir = new File(sourceDir);
        List<File> targetFileDirs = new ArrayList<>();
        for(int i = 0;i<targetDirs.length;i++){
            targetFileDirs.add(new File(targetDirs[i]));
        }

        //Проверяем достаточно ли файлов в исходной директории. Если файлов недостаточно,
        // Перемещаем все файлы из целевых директорий в исходную директорию.
        if(sourceFileDir.listFiles().length< sourceDirLengthBefore){

            for(File targetDir:targetFileDirs){
                File[] files = targetDir.listFiles();
                for(int i = 0;i<files.length;i++){
                    try {
                        Path temp = Files.move(Paths.get(files[i].getAbsolutePath()),
                                Paths.get(sourceFileDir.getAbsolutePath() + "\\" + files[i].getName()));
                        if (temp == null) throw new IOException();
                    }catch (FileAlreadyExistsException e){

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static int checkSizeOfDirectory(String dir){
        File directory = new File(dir);
        return directory.listFiles().length;
    }

    public void main(String... args) {
        try {
            MainFileHelper.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}