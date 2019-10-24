public class smallMainFileHelperTest extends MainFileHelperTest {
    /**
     * Создаем переменные с количеством файлов в тестовых папках. До и после работы программы
     */
    private static int sourceDirLengthBefore = 216;//32
    private static int sourceDirLengthAfter = 4;
    private static int targetDirLengthAfter = 212;//28

    /**
     * Создаем переменные с тестовыми папками
     */
    private static String sourceDir1 = "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\sourse";
    private static String[] targetDirs = {"C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target1",
            "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target2",
            "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target3",
            "C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target4"};

    public smallMainFileHelperTest() {
        super(sourceDir1, targetDirs, sourceDirLengthBefore, sourceDirLengthAfter, targetDirLengthAfter);
    }
}
