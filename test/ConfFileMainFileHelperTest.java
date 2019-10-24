import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConfFileMainFileHelperTest {
    /**
     * Переменные с количеством файлов в тестовых папках
     */
    private static int sourceDirLengthBefore = 216;//32
    private static int sourceDirLengthAfter = 4;
    private static int targetDirLengthAfter = 212;//28
    private static File configurationFile = new File("configurationFile.txt");
    private static String argument = "-c";

    /**
     * Переменные с тестовыми папками
     */
    private String sourceDir;
    private String[] targetDirs;

    public ConfFileMainFileHelperTest() {
        List<String> args = new ArrayList<>();
        List<String> listTargetDirs = new ArrayList<>();
        try {
            args = Files.readAllLines(configurationFile.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sourceDir = args.get(0);
        for (int i = 1; i < args.size(); i++) {
            if (!(args.get(i).equals("n") || args.get(i).equals("y"))) {
                listTargetDirs.add(args.get(i));
            }
        }
        targetDirs = listTargetDirs.toArray(new String[0]);
    }

    /**
     * Метод подготавливает тестовые папки для работы программы. Проверяет есть ли в целевых папках файлы и если есть переносит их
     * в исходную папку.
     */
    @Before
    public void makeDirectoriesReady() {
        /**
         * Создаем объекты File на основе строк с адресами тестовых папок*
         */
        File sourceFileDir = new File(sourceDir);
        List<File> targetFileDirs = new ArrayList<>();
        for (int i = 0; i < targetDirs.length; i++) {
            targetFileDirs.add(new File(targetDirs[i]));
        }

        /**
         * Проверяем достаточно ли файлов в исходной директории. Если файлов недостаточно,
         * Перемещаем все файлы из целевых директорий в исходную директорию.
         * */
        if (sourceFileDir.listFiles().length < sourceDirLengthBefore) {

            for (File targetDir : targetFileDirs) {
                File[] files = targetDir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    try {
                        Path temp = Files.move(Paths.get(files[i].getAbsolutePath()),
                                Paths.get(sourceFileDir.getAbsolutePath() + "\\" + files[i].getName()));
                        if (temp == null) throw new IOException();
                    } catch (FileAlreadyExistsException e) {

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Проверяет количество файлов в директории. Принимает строку с адресом папки.
     */
    public int checkSizeOfDirectory(String dir) {
        File directory = new File(dir);
        return directory.listFiles().length;
    }

    @Test
    public void main() {
        MainFileHelper.main(argument);

        /**
         * Проверяем результат работы программы. Сначала сравниваем количество файлов
         * в исходной папке после работы программы с ожидаемым значением. Затем в цикле сравниваем количество файлов в
         * целевых папках с ожидаемыми значениями
         * */
        assertEquals(checkSizeOfDirectory(sourceDir), sourceDirLengthAfter);
        for (int i = 0; i < targetDirs.length; i++) {
            assertEquals(checkSizeOfDirectory(targetDirs[i]), targetDirLengthAfter);
        }
    }
}
