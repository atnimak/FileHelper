public class bigMainFileHelperTest extends MainFileHelperTest {
    /**
     * Создаем переменные с количеством файлов в тестовых папках. До и после работы программы
     */
    private static int sourceDirLengthBefore = 2088;
    private static int sourceDirLengthAfter = 178;
    private static int targetDirLengthAfter = 1910;

    /**
     * Создаем переменные с тестовыми папками
     */
    private static String sourceDir = "C:\\TestFilesForFileHelper\\sourse";
    private static String[] targetDirs = {"C:\\TestFilesForFileHelper\\target1",
            "C:\\TestFilesForFileHelper\\target2"};

    public bigMainFileHelperTest() {
        super(sourceDir, targetDirs, sourceDirLengthBefore, sourceDirLengthAfter, targetDirLengthAfter);
    }
}

