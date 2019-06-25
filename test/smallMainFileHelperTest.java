import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class smallMainFileHelperTest extends MainFileHelperTest {
    static int sourceDirLengthBefore = 32;
    static int sourceDirLengthAfter = 4;
    static int targetDirLengthAfter = 28;

    //Создаем переменные с тестовыми папками
    static String sourceDir = "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\sourse";
    static String[] targetDirs = {"C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target1",
            "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target2",
            "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target3",
            "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target4"};

    @Before
    public void makeReady(){
        makeDirectoriesReady(sourceDirLengthBefore, sourceDir, targetDirs);
    }

    @org.junit.Test
    public void smallTest() {
        main(sourceDir,targetDirs,sourceDirLengthAfter,targetDirLengthAfter);

    }

}
