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

    static {
        makeDirectoriesReady(sourceDirLengthBefore, sourceDir, targetDirs);
    }

    @org.junit.Test
    public void smallTest() {
        StringBuilder sb = new StringBuilder();
        sb.append(sourceDir).append('\n');
        for (int i = 0; i < targetDirs.length; i++) {
            sb.append(targetDirs[i]).append('\n');
        }
        sb.append("n").append('\n');
        sb.append("y").append('\n');
        sb.append("n").append('\n');
        String data = sb.toString();

        super.main(data);
        assertEquals(checkSizeOfDirectory(sourceDir), sourceDirLengthAfter);
        for (int i=0;i<targetDirs.length;i++){
            assertEquals(checkSizeOfDirectory(targetDirs[i]),targetDirLengthAfter);
        }

    }

}
