import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class MainFileHelper {
    private static final Logger LOG = Logger.getLogger(MainFileHelper.class.getName());
    private static File sourceDir;
    private static List<File> targetDirList = new ArrayList<>();
    private static List<File> filesToCopy = new ArrayList<>();
    private static List<File> filesToDelete = new ArrayList<>();
    private static boolean del = false;
    private static boolean running = true;

    public static void main(String... args) throws IOException {

        boolean check = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tmp = "";
        System.out.println("Привет, это приложение скопирует файлы jpg с ключами и их eps-пары в одну или несколько директорий.\n" +
                "Если в целевых директориях уже есть файлы с именами, совпадающими с копируемыми файлами, эти файлы будут просто перезаписаны.\n" +
                "Никаких уведомлений в этом случае показано не будет.\n" +
                "При необходимости исходные файлы могут быть удалены.\n" +
                "Из-за особенностей коммандной строки Windows программа не умеет работать с кирилицей,\n" +
                "обратите внимание, чтобы все символы в имени файла и адресе были цифрами или символами латинского алфавита\n");
        do {

            do {
                System.out.println("Введите директорию, файлы из которой необходимо обработать");
                tmp = reader.readLine();
                sourceDir = new File(tmp);
                if (!sourceDir.isDirectory() && !sourceDir.exists()) {
                    System.out.println("Этот адрес не существует или это не директория!");
                } else if (sourceDir.isDirectory() && sourceDir.exists()) {
                    System.out.println("Директория принята!");
                    check = false;
                } else {
                    System.out.println("Что-то пошло не так. Проверьте путь к директории, существует ли директория\n" +
                            " и есть ли к ней доступ");
                }
            } while (check);


            check = true;
            do {
                tmp = "";
                System.out.println("Введите директорию, в которую необходимо скопировать файлы или NO");
                tmp = reader.readLine();
                File targetDir = new File(tmp);
                if ((!tmp.isEmpty()) && (!(tmp.equals("NO"))) && targetDir.isDirectory()) {
                    targetDirList.add(targetDir);
                    System.out.println("Целевая дирктория принята!");
                } else if (tmp.isEmpty()) {
                    System.out.println("Адрес директории не может быть пустым");
                } else if ((tmp.equals("NO")) && targetDirList.size() < 1) {
                    System.out.println("Должна быть хотя бы одна целевая директория");
                } else if ((tmp.equals("NO")) && targetDirList.size() > 0) {
                    System.out.println("Файлы из директории " + sourceDir.getAbsolutePath() + " будут скопированы в директории: ");
                    for (File s : targetDirList) {
                        System.out.println(s.getAbsolutePath());
                    }
                    check = false;
                } else if ((!tmp.isEmpty()) && (!(tmp.equals("NO"))) && (!targetDir.isDirectory())) {
                    System.out.println(targetDir.getAbsolutePath() + " - это не директория!");
                } else {
                    System.out.println("Что-то пошло не так. Проверьте путь к директории, существует ли директория\n" +
                            " и есть ли к ней доступ");
                }
            } while (check);

            check = true;
            do {
                tmp = "";
                System.out.println("Удалить файлы из исходной аудитории после копирования? YES/NO");
                tmp = reader.readLine();
                if (tmp.equals("YES")) {
                    del = true;
                    System.out.println("Файлы из исходной директории будут удалены!");
                    check = false;
                } else if (tmp.equals("NO")) {
                    del = false;
                    System.out.println("Файлы из исходной директории удалены не будут");
                    check = false;
                } else {
                    System.out.println("Ответ не принят. Просто введите один из вариантов YES/NO");
                }
            } while (check);
            System.out.println("Проводятся запрошенные операции...");

            makeFilesToCopy();
            doOperations();

            tmp = "";
            System.out.println("Хотите еще что-нибудь сделать? YES/EXIT");
            tmp = reader.readLine();
            if (tmp.equals("YES")) {
                running = true;
            } else if (tmp.equals("EXIT")) {
                running = false;
            } else {
                System.out.println("Ответ не принят. Просто введите один из вариантов YES/NO");
            }


        } while (running);

    }

    public static void doOperations() {
        for (File oldJpgFile : filesToCopy) {
            for (File target : targetDirList) {
                File newJpgFile = new File(target.getAbsolutePath() + "\\" + oldJpgFile.getName());

                StringBuilder oldEpsSB = new StringBuilder(oldJpgFile.getAbsolutePath());
                String oldEps = oldEpsSB.replace(oldEpsSB.length() - 3, oldEpsSB.length(), "eps").toString();
                File oldEpsFile = new File(oldEps);
                StringBuilder newEpsSB = new StringBuilder(newJpgFile.getAbsolutePath());
                String newEps = newEpsSB.replace(newEpsSB.length() - 3, newEpsSB.length(), "eps").toString();
                File newEpsFile = new File(newEps);


                copyFile(oldJpgFile, newJpgFile);
                copyFile(oldEpsFile, newEpsFile);
                if (del) {
                    filesToDelete.add(oldEpsFile);
                    filesToDelete.add(oldJpgFile);
                }
            }
        }
        if (del) {
            for (File file : filesToDelete) {
                deleteFile(file);
            }
        }
        System.out.println("Похоже все операции проведены!");

    }

    public static void makeFilesToCopy() throws IOException {
        List<File> files = Arrays.asList(sourceDir.listFiles());
        for (File file : files) {
            if (!file.isDirectory() && file.getName().endsWith(".jpg")) {
                String s = exifReader(file.getAbsolutePath());
                s = s.replaceAll("Keywords \\s+ : ", "").trim();
                if (!s.isEmpty()) {
                    filesToCopy.add(file);
                }
            }

        }

    }

    public static String exifReader(String fileName) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"exiftool", "-keywords", fileName};
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

    private static void copyFile(File source, File dest) {
        System.out.println("Копируем " + source.getAbsolutePath() + " в " + dest.getAbsolutePath());

        try (FileChannel ic = new FileInputStream(source).getChannel(); FileChannel oc = new FileOutputStream(dest).getChannel()) {
            ic.transferTo(0, ic.size(), oc);
            if (dest.exists()) {
                System.out.println("Данные успешно скопированы!");
            } else {
                System.out.println("Похоже во время копирования что-то пошло не так.");
            }

        } catch (IOException e) {
            System.out.println("Похоже во время копирования что-то пошло не так.\n" +
                    " Возможно недоступен исходный файл или нет возможности записать файл в целевую папку");
        }
    }

    private static void deleteFile(File file) {
        System.out.println("Удаляем файл" + file.getAbsolutePath());
        file.delete();
        if (!file.exists()) {
            System.out.println("Удален файл " + file.getAbsolutePath());
        } else {
            System.out.println("Файл" + file.getAbsolutePath() + "не удален. Что-то пошло не так.");
        }
    }
}
