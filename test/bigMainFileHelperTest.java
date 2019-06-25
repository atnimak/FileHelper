import org.junit.Before;

public class bigMainFileHelperTest extends MainFileHelperTest {
    static int sourceDirLengthBefore = 2088;
    static int sourceDirLengthAfter = 178;
    static int targetDirLengthAfter = 1910;

    //Создаем переменные с тестовыми папками
    static String sourceDir = "C:\\TestFilesForFileHelper\\sourse";
    static String[] targetDirs = {"C:\\TestFilesForFileHelper\\target1",
            "C:\\TestFilesForFileHelper\\target1"};

    @Before
    public void makeReady() {
        makeDirectoriesReady(sourceDirLengthBefore, sourceDir, targetDirs);
    }

    @org.junit.Test
    public void bigTest() {
        main(sourceDir, targetDirs, sourceDirLengthAfter, targetDirLengthAfter);
    }
}
