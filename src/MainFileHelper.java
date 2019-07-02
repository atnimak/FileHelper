import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainFileHelper {
    private static final Logger LOGGER = Logger.getLogger(MainFileHelper.class.getName());
    private static List<Task> tasks = new ArrayList<>();
    private static boolean del = false;
    private static boolean running = true;
    private static boolean askQuestions = true;

    static {
        LogConfigurator.configureLog();
    }

    public static void main(String... args) {
        LOGGER.info("Start the program. Check the arguments.");
        if (args.length > 0 && args[0].equals("-c")) {
            askQuestions = false;
            ConfigurationFileStarter.setArguments(new File("configurationFile.txt"));
        } else if (args.length > 0) {
            askQuestions = false;
            ArgumentStarter.setArguments(args);
        }

        LOGGER.info("Start the program. Begin a dialog with the user.");

        boolean check = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tmp = "";
        if (askQuestions) {
            System.out.println("Привет, это приложение File Helper ver. 0.022 скопирует файлы jpg с ключами и их eps-пары в одну или несколько директорий.\n" +
                    "Если в целевых директориях уже есть файлы с именами, совпадающими с копируемыми файлами, эти файлы будут просто перезаписаны.\n" +
                    "Никаких уведомлений в этом случае показано не будет.\n" +
                    "При необходимости исходные файлы могут быть удалены.\n" +
                    "Из-за особенностей коммандной строки Windows программа не умеет работать с кирилицей,\n" +
                    "обратите внимание, чтобы все символы в имени файла и адресе были цифрами или символами латинского алфавита.\n" +
                    "Отвечать на вопросы программы можно используя клвиши Y в значении ДА и N в значении НЕТ,\n" +
                    "также допустимо использование строчных символов y и n.\n");
        }
        do {
            File sourceDir;
            List<File> targetDirList = new ArrayList<>();


            check = true;

            do {
                System.out.println("Запрашиваю директорию, файлы из которой необходимо обработать");
                if (askQuestions) {
                    System.out.println("Введите директорию:");
                }
                tmp = readLine(reader);
                sourceDir = new File(tmp);
                if (!sourceDir.isDirectory() && !sourceDir.exists()) {
                    System.out.println("Этот адрес " + sourceDir.getAbsolutePath() + " не существует или это не директория!");
                } else if (sourceDir.isDirectory() && sourceDir.exists()) {
                    System.out.println("Директория " + sourceDir.getAbsolutePath() + " принята!");
                    check = false;
                } else {
                    System.out.println("Что-то пошло не так. Проверьте путь к директории " + sourceDir.getAbsolutePath() + "\n " +
                            "существует ли директория и есть ли к ней доступ");
                }
            } while (check);

            check = true;
            do {
                tmp = "";
                System.out.println("Запрашивается директория, в которую необходимо скопировать файлы");
                if (askQuestions) {
                    System.out.println("Введите директорию или N:");
                }
                tmp = readLine(reader);
                File targetDir = new File(tmp);
                if ((!tmp.isEmpty()) && (!(tmp.equals("N") || tmp.equals("n"))) && targetDir.isDirectory()) {
                    targetDirList.add(targetDir);
                    System.out.println("Целевая директория " + targetDir.getAbsolutePath() + " принята!");
                } else if (tmp.isEmpty()) {
                    System.out.println("Адрес директории " + targetDir.getAbsolutePath() + " не может быть пустым");
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
                    System.out.println("Что-то пошло не так. Проверьте путь к директории " + targetDir.getAbsolutePath() + " , существует ли директория\n" +
                            " и есть ли к ней доступ");
                }
            } while (check);

            check = true;
            do {
                tmp = "";
                System.out.println("Запрашивается необходимость удаления файлов");
                if (askQuestions) {
                    System.out.println("Удалить файлы из исходной аудитории после копирования? Y/N");
                }
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

            Task task = new Task();
            task.setSourceDir(sourceDir);
            task.setTargetDirList(targetDirList);
            task.setDeleteFileAccept(del);
            tasks.add(task);

            check = true;
            do {
                tmp = "";
                LOGGER.info("Asking about new tasks");
                System.out.println("Запрашиваются дополнительные задачи...");
                if (askQuestions) {
                    System.out.println("Хотите еще что-нибудь сделать? Y/N");
                }
                tmp = readLine(reader);
                if (tmp.equals("Y") || tmp.equals("y")) {
                    LOGGER.info("Got positive answer");
                    System.out.println("Формируем дополнительные задачи...");
                    running = true;
                    check = false;
                } else if (tmp.equals("N") || tmp.equals("n")) {
                    LOGGER.info("Got negative answer");
                    System.out.println("Дополнительных задач нет");
                    System.out.println("Начинаем выполнение запланированных задач!");
                    running = false;
                    check = false;
                } else {
                    LOGGER.info("Got wrong answer");
                    System.out.println("Ответ не принят. Просто введите один из вариантов Y/N");
                }

            } while (check);

        } while (running);

        LOGGER.info("Start copying files");
        System.out.println("Проводятся запрошенные операции...");
        for (Task task : tasks) {
            System.out.println();
            task.copyFiles();
        }
        System.out.println("Все задачи выполнены. Завершение работы программы");
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
