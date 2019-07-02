import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Task {
    private static final Logger LOGGER = Logger.getLogger(Task.class.getName());

    File sourceDir;
    List<File> targetDirList = new ArrayList<>();
    boolean del;

    public void setDeleteFileAccept(boolean del) {
        this.del = del;
    }

    public void setTargetDirList(List<File> targetDirList) {
        this.targetDirList = targetDirList;
    }

    public void setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
    }

    private List<File> makeFilesToCopy(File sourceDir) {
        List<File> filesToCopy = new ArrayList<>();

        LOGGER.info("Task.class: makeFilesToCopy Looking for files to copy!");
        System.out.println("Проверяем директорию, ищем файлы для копирования...");
        List<File> files = Arrays.asList(sourceDir.listFiles());
        for (File file : files) {
            if (!file.isDirectory() && file.getName().toLowerCase().endsWith(".jpg")) {
                String s = KeywordsReader.metadataReader(file.getAbsolutePath());
                s = s.replaceAll("XP Keywords \\s+ : ", "").trim();
                if (!s.isEmpty()) {
                    filesToCopy.add(file);
                }
            }

        }
        return filesToCopy;

    }

    private void doOperations(List<File> filesToCopy, List<File> targetDirList) {
        LOGGER.info("Task.class: Started copying files!  MainFileHelper doOperations");
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
            LOGGER.info("Task.class: Files deletion started!");
            System.out.println("Удаляем файлы. Осталось удалить " + filesToDelete.size() + " файлов...");
            for (File file : filesToDelete) {
                ProgressChecker.checkProgress(file, filesToDelete);
                deleteFile(file);
            }
        }
        ProgressChecker.resetProgress();
        System.out.println("Похоже все операции проведены!");
        LOGGER.info("Task: All operations are done");

    }


    private void copyFile(File source, File dest) {
        LOGGER.info("Task.class: copyFile");
        LOGGER.info("Task.class: Сopying " + source.getAbsolutePath() + " to " + dest.getAbsolutePath());

        try (FileChannel ic = new FileInputStream(source).getChannel(); FileChannel oc = new FileOutputStream(dest).getChannel()) {
            ic.transferTo(0, ic.size(), oc);
            if (dest.exists()) {
                LOGGER.info("Task.class: File " + source.getAbsolutePath() + " was successfully copied to " + dest.getAbsolutePath());
            } else {
                LOGGER.warning("Task.class: While copying the file " + source.getAbsolutePath() + " to " + dest.getAbsolutePath() + " an error occurred!");
                System.out.println("Во время копирования " + source.getAbsolutePath() + " в " + dest.getAbsolutePath() + " произошла ошибка!");
            }
        } catch (IOException e) {
            LOGGER.warning("Task.class: While copying the file " + source.getAbsolutePath() + " to " + dest.getAbsolutePath() + " an error occurred! The source file may not be available or it is not possible to write the file to the target folder.");
            System.out.println("Во время копирования " + source.getAbsolutePath() + " в " + dest.getAbsolutePath() + " произошла ошибка!\n" +
                    " Возможно недоступен исходный файл или нет возможности записать файл в целевую папку");
        }
    }

    private void deleteFile(File file) {
        LOGGER.info("Task.class: deleteFile");
        LOGGER.info("Task.class: Deleting file " + file.getAbsolutePath());
        file.delete();
        if (!file.exists()) {
            LOGGER.info("Task.class: File " + file.getAbsolutePath() + " was successfully deleted.");
        } else {
            LOGGER.warning("Task.class: File " + file.getAbsolutePath() + " was successfully deleted. An error occurred!");
        }
    }

    public void copyFiles (){
        List<File> filesToCopy = makeFilesToCopy(sourceDir);
        doOperations(filesToCopy, targetDirList);
    }
}
