import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MainFileHelper {


    private static final Logger LOGGER = Logger.getLogger(MainFileHelper.class.getName());

    static {
      LogConfigurator.configureLog();
    }

    private static File sourceDir;
    private static List<File> targetDirList;
    private static List<File> filesToCopy;
    private static List<File> filesToDelete;
    private static boolean del = false;
    private static boolean running = true;

    public static void main(String... args) throws IOException {
        LOGGER.info("Старт программы. Начинаем диалог с пользователем");

        boolean check = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tmp = "";
        System.out.println("Привет, это приложение File Helper ver. 0.021 скопирует файлы jpg с ключами и их eps-пары в одну или несколько директорий.\n" +
                "Если в целевых директориях уже есть файлы с именами, совпадающими с копируемыми файлами, эти файлы будут просто перезаписаны.\n" +
                "Никаких уведомлений в этом случае показано не будет.\n" +
                "При необходимости исходные файлы могут быть удалены.\n" +
                "Из-за особенностей коммандной строки Windows программа не умеет работать с кирилицей,\n" +
                "обратите внимание, чтобы все символы в имени файла и адресе были цифрами или символами латинского алфавита.\n" +
                "Отвечать на вопросы программы можно используя клвиши Y в значении ДА и N в значении НЕТ,\n" +
                "также допустимо использование строчных символов y и n.\n");
        do {
            targetDirList = new ArrayList<>();
            filesToCopy = new ArrayList<>();
            filesToDelete = new ArrayList<>();
            check=true;

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
                System.out.println("Введите директорию, в которую необходимо скопировать файлы или N");
                tmp = reader.readLine();
                File targetDir = new File(tmp);
                if ((!tmp.isEmpty()) && (!(tmp.equals("N") || tmp.equals("n"))) && targetDir.isDirectory()) {
                    targetDirList.add(targetDir);
                    System.out.println("Целевая директория принята!");
                } else if (tmp.isEmpty()) {
                    System.out.println("Адрес директории не может быть пустым");
                } else if ((tmp.equals("N") || tmp.equals("n")) && targetDirList.size() < 1) {
                    System.out.println("Должна быть хотя бы одна целевая директория");
                } else if ((tmp.equals("N") || tmp.equals("n")) && targetDirList.size() > 0) {
                    System.out.println("Файлы из директории " + sourceDir.getAbsolutePath() + " будут скопированы в директории: ");
                    for (File s : targetDirList) {
                        System.out.println(s.getAbsolutePath());
                    }
                    check = false;
                } else if ((!tmp.isEmpty()) && (!(tmp.equals("N") || tmp.equals("n"))) && (!targetDir.isDirectory())) {
                    System.out.println(targetDir.getAbsolutePath() + " - это не директория!");
                } else {
                    System.out.println("Что-то пошло не так. Проверьте путь к директории, существует ли директория\n" +
                            " и есть ли к ней доступ");
                }
            } while (check);

            check = true;
            do {
                tmp = "";
                System.out.println("Удалить файлы из исходной аудитории после копирования? Y/N");
                tmp = reader.readLine();
                if (tmp.equals("Y") || tmp.equals("y")) {
                    del = true;
                    System.out.println("Файлы из исходной директории будут удалены!");
                    check = false;
                } else if (tmp.equals("N") || tmp.equals("n")) {
                    del = false;
                    System.out.println("Файлы из исходной директории удалены не будут");
                    check = false;
                } else {
                    System.out.println("Ответ не принят. Просто введите один из вариантов Y или N");
                }
            } while (check);
            System.out.println("Проводятся запрошенные операции...");

            makeFilesToCopy();
            doOperations();

            check = true;
            do {
                tmp = "";
                System.out.println("Хотите еще что-нибудь сделать? Y/N");
                tmp = reader.readLine();
                if (tmp.equals("Y") || tmp.equals("y")) {
                    running = true;
                    check = false;
                } else if (tmp.equals("N") || tmp.equals("n")) {
                    System.out.println("Отлично поработали! Хорошего дня!");
                    running = false;
                    check = false;
                } else {
                    System.out.println("Ответ не принят. Просто введите один из вариантов Y/N");
                }

            } while (check);


        } while (running);
        LOGGER.info("Завершение работы программы");

    }

    public static void doOperations() {
        LOGGER.info("Начато копирование файлов!  MainFileHelper doOperations");
        System.out.println("Копируем файлы...");
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
            }
            if (del) {
                filesToDelete.add(oldEpsFile);
                filesToDelete.add(oldJpgFile);
            }
        }

        if (del) {
            LOGGER.info("Начато удаление файлов!");
            System.out.println("Удаляем файлы...");
            for (File file : filesToDelete) {
                deleteFile(file);
            }
        }
        System.out.println("Похоже все операции проведены!");

    }

    public static void makeFilesToCopy() throws IOException {
        LOGGER.info("Ищем файлы для копирования! MainFileHelper makeFilesToCopy");
        System.out.println("Проверяем директорию, ищем файлы для копирования...");
        List<File> files = Arrays.asList(sourceDir.listFiles());
        for (File file : files) {
            if (!file.isDirectory() && file.getName().toLowerCase().endsWith(".jpg")) {
                String s = exifReader(file.getAbsolutePath());
                s = s.replaceAll("XP Keywords \\s+ : ", "").trim();
                if (!s.isEmpty()) {
                    filesToCopy.add(file);
                }
            }

        }

    }

    public static String exifReader(String fileName) throws IOException {
        LOGGER.info("MainFileHelper exifReader");
        return KeywordsReader.metadataReader(fileName);
    }


    private static void copyFile(File source, File dest) {
        LOGGER.info("MainFileHelper copyFile");
        LOGGER.info("Копируем " + source.getAbsolutePath() + " в " + dest.getAbsolutePath());
        //System.out.println("Копируем " + source.getAbsolutePath() + " в " + dest.getAbsolutePath());

        try (FileChannel ic = new FileInputStream(source).getChannel(); FileChannel oc = new FileOutputStream(dest).getChannel()) {
            ic.transferTo(0, ic.size(), oc);
            if (dest.exists()) {
                //System.out.println("Данные успешно скопированы!");
                LOGGER.info("Файл " + source.getAbsolutePath() + " в " + dest.getAbsolutePath() +" успешно скопирован!");
            } else {
                LOGGER.warning("Во время копирования "+ source.getAbsolutePath() + " в " + dest.getAbsolutePath()+" произошла ошибка");
                //System.out.println("Похоже во время копирования что-то пошло не так.");
            }
        } catch (IOException e) {
            LOGGER.warning("Во время копирования "+ source.getAbsolutePath() + " в " + dest.getAbsolutePath()+" произошла ошибка. \n " +
                    "Возможно недоступен исходный файл или нет возможности записать файл в целевую папку");
            /*System.out.println("Похоже во время копирования что-то пошло не так.\n" +
                    " Возможно недоступен исходный файл или нет возможности записать файл в целевую папку");*/
        }
    }

    private static void deleteFile(File file) {
        LOGGER.info("MainFileHelper deleteFile");
        LOGGER.info("Удаляем файл " + file.getAbsolutePath());
        //System.out.println("Удаляем файл" + file.getAbsolutePath());
        file.delete();
        if (!file.exists()) {
            LOGGER.info("Удален файл " + file.getAbsolutePath());
            //System.out.println("Удален файл " + file.getAbsolutePath());
        } else {
            LOGGER.warning("Файл " + file.getAbsolutePath() + " не удален. Что-то пошло не так.");
            //System.out.println("Файл" + file.getAbsolutePath() + "не удален. Что-то пошло не так.");
        }
    }
}
