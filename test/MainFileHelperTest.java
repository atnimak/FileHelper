import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MainFileHelperTest {
    int smallSourceDirLengthBefore = 32;
    int smallSourceDirLengthAfter = 5;
    int smallTargetDirLengthAfter = 28;


    public void smallSetUp(){
        //Создаем переменные с тестовыми папками
        String sourceDir = "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\sourse";
        String[] targetDirs = {"C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target1",
                "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target2",
                "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target3",
                "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target4"};

        //Создаем объекты File на основе строк с адресами тестовых папок
        File sourceFileDir = new File(sourceDir);
        List<File> targetFileDirs = new ArrayList<>();
        for(int i = 0;i<targetDirs.length;i++){
            targetFileDirs.add(new File(targetDirs[i]));
        }

        //Проверяем достаточно ли файлов в исходной директории. Если файлов недостаточно,
        // Перемещаем все файлы из целевых директорий в исходную директорию.
        if(sourceFileDir.listFiles().length<smallSourceDirLengthBefore){

            for(File targetDir:targetFileDirs){

               File[] files = targetDir.listFiles();
               for(int i = 0;i<files.length;i++){
                   try {
                       Path temp = Files.move(Paths.get(files[i].getAbsolutePath()),
                               Paths.get(sourceFileDir.getAbsolutePath()+"\\"+files[i].getName()));
                       if(temp == null) throw new IOException();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
            }
        }
    }

    private void bigSetUp() {
    }

    @org.junit.Test
    public void smallTest() {
        smallSetUp();
        main();

    }

    @org.junit.Test
    public void bigTest(){
        bigSetUp();
        main();
    }



    public void main() {

    }
}