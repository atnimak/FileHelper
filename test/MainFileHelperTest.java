import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class MainFileHelperTest {
    /**
     * Переменные с количеством файлов в тестовых папках
     */
    private int sourceDirLengthBefore;
    private int sourceDirLengthAfter;
    private int targetDirLengthAfter;
    /**
     * Переменные с тестовыми папками
     */
    private String sourceDir;
    private String[] targetDirs;

    protected MainFileHelperTest(String sourceDir, String[] targetDirs, int sourceDirLengthBefore, int sourceDirLengthAfter, int targetDirLengthAfter) {
        this.sourceDirLengthBefore = sourceDirLengthBefore;
        this.sourceDirLengthAfter = sourceDirLengthAfter;
        this.targetDirLengthAfter = targetDirLengthAfter;
        this.sourceDir = sourceDir;
        this.targetDirs = targetDirs;

    }

    /**
     * Метод подготавливает тестовые папки для работы программы. Проверяет есть ли в целевых папках файлы и если есть переносит их
     * в исходную папку.
     */
    @Before
    public void makeDirectoriesReady() {
        //Создаем объекты File на основе строк с адресами тестовых папок
        File sourceFileDir = new File(sourceDir);
        List<File> targetFileDirs = new ArrayList<>();
        for (int i = 0; i < targetDirs.length; i++) {
            targetFileDirs.add(new File(targetDirs[i]));
        }

        //Проверяем достаточно ли файлов в исходной директории. Если файлов недостаточно,
        // Перемещаем все файлы из целевых директорий в исходную директорию.
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
        /**Формируем аргументы для запуска программы через пробел в порядке:
         1 - исходная директория,
         2 - несколько целевыя директорий,
         3 - ответ на вопрос будут ли еще целевые директории (отрицательный),
         4 - ответ на вопрос об удалении файлов (положительный)
         5 - ответ на вопрос о дальнейших действиях (отрицательный).*/
        StringBuilder sb = new StringBuilder();
        sb.append(sourceDir).append('\n');
        for (int i = 0; i < targetDirs.length; i++) {
            sb.append(targetDirs[i]).append('\n');
        }
        sb.append("n").append('\n');
        sb.append("y").append('\n');
        sb.append("n").append('\n');

        /**Кладем данные в строку*/
        String data = sb.toString();

        /**Вызываем метод main*/
        MainFileHelper.main(data);

        /**Проверяем результат работы программы. Сначала сравниваем количество файлов
         в исходной папке после работы программы с ожидаемым значением. Затем в цикле сравниваем количество файлов в
         целевых папках с ожидаемыми значениями*/
        assertEquals(checkSizeOfDirectory(sourceDir), sourceDirLengthAfter);
        for (int i = 0; i < targetDirs.length; i++) {
            assertEquals(checkSizeOfDirectory(targetDirs[i]), targetDirLengthAfter);
        }
    }
}