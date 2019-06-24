import static org.junit.Assert.assertEquals;

public class bigMainFileHelperTest extends MainFileHelperTest {
    static int sourceDirLengthBefore = 2088;
    static int sourceDirLengthAfter = 178;
    static int targetDirLengthAfter = 1910;

    //Создаем переменные с тестовыми папками
    static String sourceDir = "C:\\TestFilesForFileHelper\\sourse";
    static String[] targetDirs = {"C:\\TestFilesForFileHelper\\target1",
            "C:\\TestFilesForFileHelper\\target1"};

    static {
        makeDirectoriesReady(sourceDirLengthBefore, sourceDir, targetDirs);
    }

    @org.junit.Test
    public void bigTest() {
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
        for (int i = 0; i < targetDirs.length; i++) {
            assertEquals(checkSizeOfDirectory(targetDirs[i]), targetDirLengthAfter);
        }

    }
}
