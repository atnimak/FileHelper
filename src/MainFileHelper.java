import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class MainFileHelper {
    private static final Logger LOG = Logger.getLogger(MainFileHelper.class.getName());
    private static File sourseDir;
    private static List<File> targetDirList = new ArrayList<>();
    private static List<File> filesToCopy = new ArrayList<>();


    public static void main(String... args) throws IOException {
        conversation();
        makeFilesToCopy();
        for (File file : filesToCopy) {
            for (File target : targetDirList) {
                File newFile = new File(target.getAbsolutePath() + "\\" + file.getName());

                StringBuilder oldEpsSB = new StringBuilder(file.getAbsolutePath());
                String oldEpsFile = oldEpsSB.replace(oldEpsSB.length() - 3, oldEpsSB.length(), "eps").toString();
                StringBuilder newEpsSB = new StringBuilder(newFile.getAbsolutePath());
                String newEpsFile = newEpsSB.replace(newEpsSB.length() - 3, newEpsSB.length(), "eps").toString();


                copyFile(file, newFile);
                copyFile(new File(oldEpsFile), new File(newEpsFile));



            }
        }
    }

    public static void makeFilesToCopy() throws IOException {
        List<File> files = Arrays.asList(sourseDir.listFiles());
        for (File file : files) {
            if (!file.isDirectory()&&file.getName().endsWith(".jpg")) {
                String s = exifReader(file.getAbsolutePath());
                s = s.replaceAll("Keywords \\s+ : ", "").trim();
                if (!s.isEmpty()) {
                    filesToCopy.add(file);
                }
            }

        }

    }

    public static void conversation() throws IOException {
        boolean check = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String dir = "";
        do {
            System.out.println("Введите директорию, файлы из которой необходимо обработать");
            dir = reader.readLine();
            sourseDir = new File(dir);
            if (!sourseDir.isDirectory() && !sourseDir.exists()) {
                System.out.println("Этот адрес не существует или это не директория!");
            } else if (sourseDir.isDirectory() && sourseDir.exists()) {
                System.out.println("Директория принята!");
                check = false;
            }
        } while (check);


        check = true;
        do {
            String tmp = "";
            System.out.println("Введите директорию, в которую необходимо скопировать файлы или НЕТ");
            tmp = reader.readLine();
            File targetDir = new File(tmp);
            if ((!tmp.isEmpty()) && (!(tmp.equals("НЕТ"))) && targetDir.isDirectory()) {

                targetDirList.add(targetDir);
                System.out.println("Целевая дирктория принята!");

            } else if (tmp.isEmpty() || targetDirList.size() < 1) {
                System.out.println("Должна быть хотя бы одна целевая директория");
            } else if (tmp.isEmpty() || tmp.equals("НЕТ") && targetDirList.size() > 1) {
                System.out.println("Файлы из директории " + dir + " будут скопированы в директории: ");
                for (File s : targetDirList) {
                    System.out.println(s.getAbsolutePath());
                }
                check = false;
            } else {
                System.out.println(targetDir.getAbsolutePath() + " - это не директория!");
            }
        } while (check);

        check = true;
        String delete = "";

        boolean del = false;
        do {
            System.out.println("Удалить файлы из исходной аудитории после копирования? ДА/НЕТ");
            delete = reader.readLine();
            if (delete.equals("ДА")) {
                del = true;
                System.out.println("Файлы из исходной директории будут удалены!");
                check = false;
            } else if (delete.equals("НЕТ")) {
                del = false;
                System.out.println("Файлы из исходной директории удалены не будут");
                check = false;
            }
        } while (check);
    }

    public static String exifReader(String fileName) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"exiftool", "-keywords", fileName};
        //"C:\\Users\\Maxim\\Desktop\\14_0503_internet_ban_1.jpg"
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

// read the output from the command
        String result = "";
        String check = null;
        while ((check = stdInput.readLine()) != null) {
            result += check;
        }

// read any errors from the attempted command
        String errors = null;
        while ((errors = stdError.readLine()) != null) {
            LOG.warning("Here is the standard error of the command (if any):\n" + errors);
        }
        return result;
    }

    private static void copyFile(File source, File dest) throws IOException {

        System.out.println("Копируем " + source.getAbsolutePath() + " в " + dest.getAbsolutePath());


        try (FileChannel ic = new FileInputStream(source).getChannel(); FileChannel oc = new FileOutputStream(dest).getChannel()) {
            ic.transferTo(0, ic.size(), oc);

        } finally {
            System.out.println("Данные успешно скопированы!");
        }


    }


}
