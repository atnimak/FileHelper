import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainFileHelper {


    private static final Logger LOGGER = Logger.getLogger(MainFileHelper.class.getName());
    private static List<Task> tasks = new ArrayList<>();

    static {
        LogConfigurator.configureLog();
    }


    private static boolean del = false;
    private static boolean running = true;

    public static void main(String... args) {
        LOGGER.info("Start the program. Check the arguments.");
        if (args.length > 0 && args[0].equals("-c")) {
            ConfigurationFileStarter.setArguments(new File("configurationFile.txt"));
        } else if (args.length > 0) {
            ArgumentStarter.setArguments(args);
        }

        LOGGER.info("Start the program. Begin a dialog with the user.");

        boolean check = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tmp = "";
        System.out.println("Привет, это приложение File Helper ver. 0.022 скопирует файлы jpg с ключами и их eps-пары в одну или несколько директорий.\n" +
                "Если в целевых директориях уже есть файлы с именами, совпадающими с копируемыми файлами, эти файлы будут просто перезаписаны.\n" +
                "Никаких уведомлений в этом случае показано не будет.\n" +
                "При необходимости исходные файлы могут быть удалены.\n" +
                "Из-за особенностей коммандной строки Windows программа не умеет работать с кирилицей,\n" +
                "обратите внимание, чтобы все символы в имени файла и адресе были цифрами или символами латинского алфавита.\n" +
                "Отвечать на вопросы программы можно используя клвиши Y в значении ДА и N в значении НЕТ,\n" +
                "также допустимо использование строчных символов y и n.\n");
        do {
            File sourceDir;
            List<File> targetDirList = new ArrayList<>();


            check = true;

            do {
                System.out.println("Введите директорию, файлы из которой необходимо обработать");
                tmp = readLine(reader);
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
                tmp = readLine(reader);
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
                tmp = readLine(reader);
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

            Task task = new Task();
            task.setSourceDir(sourceDir);
            task.setTargetDirList(targetDirList);
            task.setDeleteFileAccept(del);
            tasks.add(task);

            check = true;
            do {
                tmp = "";
                LOGGER.info("Asking about new tasks");
                System.out.println("Хотите еще что-нибудь сделать? Y/N");
                tmp = readLine(reader);
                if (tmp.equals("Y") || tmp.equals("y")) {
                    LOGGER.info("Got positive answer");
                    running = true;
                    check = false;
                } else if (tmp.equals("N") || tmp.equals("n")) {
                    LOGGER.info("Got negative answer");
                    System.out.println("Отлично поработали! Хорошего дня!");
                    running = false;
                    check = false;
                } else {
                    LOGGER.info("Got wrong answer");
                    System.out.println("Ответ не принят. Просто введите один из вариантов Y/N");
                }

            } while (check);


        } while (running);

        LOGGER.info("Start copying files");
        for(Task task:tasks){
            task.copyFiles();
        }
        LOGGER.info("End the program");

    }

    private static String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Во время чтения команды произошла ошибка. Если ошибка повтооряется перезапустите программу!");
            LOGGER.warning("While reading command from command line\n" + e.getStackTrace());
        }
        return new String("");
    }






}
