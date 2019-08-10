import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс представляет собой "задачу", в конструктор которй передаются:
 * - исходная папка
 * - лист целевых папок
 * - boolean, который указывает на необходимость удаления файлов из исходной папки после перемещения. true - удалять, false - не удалять
 * <p>
 * При вызове метода copyFiles() "задача" начинает выполнять сама себя: проверяет исходную директорию на наличие jpg-файлов с ключевыми словами,
 * создает список этих файлов и их пар (eps-файлов с идентичными названиями), и копирует их в каждую папку из списка targetDirList - списка целевых папок,
 * затем, если третий параметр конструктора del равен true - удаляет все, скопированные файлы из исходной директории.
 */
public class Task {
    private static final Logger LOGGER = Logger.getLogger(Task.class.getName());

    File sourceDir;
    List<File> targetDirList;
    boolean del;

    /**
     * Заготовка для комментария
     */
    public Task(File sourceDir, List<File> targetDirList, boolean del) {
        this.sourceDir = sourceDir;
        this.targetDirList = targetDirList;
        this.del = del;
    }

    /**
     * Метод получает исходную директорию в качестве аргумента, проверяет ее содержимое на наличие jpg-файлов с ключевыми словами,
     * создает список из этих файлов и их пар (eps-файлов с идентичными названиями), и возвращает список файлов для копирования (jpg-файлов и их eps-пар).
     */
    private List<File> makeFilesToCopy(File sourceDir) {
        List<File> filesToCopy = new ArrayList<>();

        LOGGER.log(Level.INFO, "Task.class: makeFilesToCopy Looking for files to copy!");
        System.out.println("Проверяем директорию, ищем файлы для копирования...");
        List<File> files = Arrays.asList(sourceDir.listFiles());
        for (File file : files) {
            if (!file.isDirectory() && file.getName().toLowerCase().endsWith(".jpg")) {
                String s = KeywordsReader.readMetadata(file.getAbsolutePath());
                s = s.replaceAll("XP Keywords \\s+ : ", "").trim();
                if (!s.isEmpty()) {
                    filesToCopy.add(file);
                }
            }

        }
        return filesToCopy;

    }

    /**
     * Метод получает в качестве аргументов список файлов для копирования и список целевых директорий. Для каждого файла из списка
     * для копирования, с помощью адреса целевых директорий, метод формирует целевой адрес и вызывает метод copyFile(), чтобы
     * скопировать файл в целевую директорию. В поцессе копирования метод формирует список файлов для удаления. И если переменная
     * del равна truе, вызывает метод deleteFile(), для каждого файла из списка filesToDelete
     */
    private void doOperations(List<File> filesToCopy, List<File> targetDirList) {
        LOGGER.log(Level.INFO, "Task.class: doOperations Started copying files! ");
        System.out.println("Копируем файлы. Осталось скопировать " + filesToCopy.size() * 2 + " файлов...");
        List<File> filesToDelete = new ArrayList<>();
        for (File oldJpgFile : filesToCopy) {
            StringBuilder oldEpsSB = new StringBuilder(oldJpgFile.getAbsolutePath());
            String oldEps = oldEpsSB.replace(oldEpsSB.length() - 3, oldEpsSB.length(), "eps").toString();
            File oldEpsFile = new File(oldEps);
            for (File target : targetDirList) {
                File newJpgFile = new File(target.getAbsolutePath() + "\\" + oldJpgFile.getName());

                StringBuilder newEpsSB = new StringBuilder(newJpgFile.getAbsolutePath());
                String newEps = newEpsSB.replace(newEpsSB.length() - 3, newEpsSB.length(), "eps").toString();
                File newEpsFile = new File(newEps);

                copyFile(oldJpgFile, newJpgFile);
                copyFile(oldEpsFile, newEpsFile);
                ProgressChecker.checkProgress(oldJpgFile, filesToCopy);

            }
            if (del) {
                filesToDelete.add(oldEpsFile);
                filesToDelete.add(oldJpgFile);
            }
        }
        ProgressChecker.resetProgress();

        if (del) {
            LOGGER.log(Level.INFO, "Task.class: Files deletion started!");
            System.out.println("Удаляем файлы. Осталось удалить " + filesToDelete.size() + " файлов...");
            for (File file : filesToDelete) {
                ProgressChecker.checkProgress(file, filesToDelete);
                deleteFile(file);
            }
        }
        ProgressChecker.resetProgress();
        System.out.println("Задача выполнена!");
        LOGGER.log(Level.INFO, "Task.class: All operations in this task are done");

    }


    /**
     * Метод получает в качестве аргументов адрес исходного файла и адрес конечного файла и копирует файл из исходного месторасположения в конечное.
     */
    private void copyFile(File source, File dest) {
        LOGGER.log(Level.CONFIG, "Task.class: copyFile");
        LOGGER.log(Level.CONFIG, "Task.class: Сopying " + source.getAbsolutePath() + " to " + dest.getAbsolutePath());

        try (FileChannel ic = new FileInputStream(source).getChannel(); FileChannel oc = new FileOutputStream(dest).getChannel()) {
            ic.transferTo(0, ic.size(), oc);
            if (dest.exists()) {
                LOGGER.log(Level.CONFIG, "Task.class: File " + source.getAbsolutePath() + " was successfully copied to " + dest.getAbsolutePath());
            } else {
                LOGGER.log(Level.WARNING, "Task.class: While copying the file " + source.getAbsolutePath() + " to " + dest.getAbsolutePath() + " an error occurred!");
                System.out.println("Во время копирования " + source.getAbsolutePath() + " в " + dest.getAbsolutePath() + " произошла ошибка!");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Task.class: While copying the file " + source.getAbsolutePath() + " to " + dest.getAbsolutePath() + " an error occurred! The source file may not be available or it is not possible to write the file to the target folder.");
            System.out.println("Во время копирования " + source.getAbsolutePath() + " в " + dest.getAbsolutePath() + " произошла ошибка!\n" +
                    " Возможно недоступен исходный файл или нет возможности записать файл в целевую папку");
        }
    }

    /**
     * Метод получает в качестве аргумента адрес файла из списка filesToDelete, поэтому метод просто удаляет это файл.
     */
    private void deleteFile(File file) {
        LOGGER.log(Level.CONFIG, "Task.class: deleteFile");
        LOGGER.log(Level.CONFIG, "Task.class: Deleting file " + file.getAbsolutePath());
        file.delete();
        if (!file.exists()) {
            LOGGER.log(Level.CONFIG, "Task.class: File " + file.getAbsolutePath() + " was successfully deleted.");
        } else {
            LOGGER.log(Level.WARNING, "Task.class: File " + file.getAbsolutePath() + " was successfully deleted. An error occurred!");
        }
    }

    /**
     * Метод вызывается без аргументов, так как все необходимые данные передаются в класс в конструкторе. Этот метод запускает выполнение
     * задач: сначала вызывает метод makesFilesToCopy(), чтобы получить списко файлов для копирования. Затем вызывает метод doOperations,
     * который и контролирует выполнение копирования и удаления файлов из исходной директории.
     */
    public void copyFiles() {
        List<File> filesToCopy = makeFilesToCopy(sourceDir);
        doOperations(filesToCopy, targetDirList);
    }
}
