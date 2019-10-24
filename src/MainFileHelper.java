import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Это main класс, он стартует со стартом приложения. Этот класс контролирует работу программы. Запускает диалог с пользователем,
 * собирает данные для создание инстанса Task и запускает его метод copyFiles(), чтобы скопировать и по необходимости удалить файлы из
 * исходной директории.
 */
public class MainFileHelper {
    private static final Logger LOGGER = Logger.getLogger(MainFileHelper.class.getName());
    private static List<Task> tasks = new ArrayList<>();
    private static boolean del = false;
    private static boolean running = true;
    private static boolean askQuestions = true;
    private static String ver = "0.032";

    /**
     * В этом блоке вызывается статический метод configureLog() класса LogConfigurator, для того, чтобы настроить логгер.
     */
    static {
        LogConfigurator.configureLog();
    }

    /**
     * Метод main(), запускается при старте программы. Этот метод контролирует работу программы. Запускает диалог с пользователем,
     * собирает данные для создание инстанса Task и запускает его метод copyFiles(), чтобы скопировать и по необходимости удалить файлы из
     * исходной директории.
     */
    public static void main(String... args) {
        LOGGER.log(Level.INFO, "Start the program. Check the arguments.");

        /**
         * При запуске программы этот блок проверяет аргументы метода main() и передает управление методу setArguments() класса ConfigurationFileStarter для
         * обработки настроек из файла configurationFile, если при запуске приложения получен аргумент -с.
         * Если получен другой аргумент управление передается методу setArguments() класса ArgumentStarter, для обработки настроек из аргументов коммандной строки.
         */
        if (args.length > 0 && args[0].equals("-c")) {
            askQuestions = false;
            ConfigurationFileStarter.setArguments(new File("configurationFile.txt"));
        } else if (args.length > 0) {
            askQuestions = false;
            ArgumentStarter.setArguments(args);
        }

        LOGGER.log(Level.INFO, "Start the program. Begin a dialog with the user.");

        /**
         * Заготовка для комментария
         */
        boolean check = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tmp = "";
        if (askQuestions) {
            System.out.println("Привет, это приложение File Helper ver." + ver + " скопирует файлы jpg с ключами и их eps-пары в одну или несколько директорий.\n" +
                    "Если в целевых директориях уже есть файлы с именами, совпадающими с копируемыми файлами, эти файлы будут просто перезаписаны.\n" +
                    "Никаких уведомлений в этом случае показано не будет.\n" +
                    "При необходимости исходные файлы могут быть удалены.\n" +
                    "Из-за особенностей коммандной строки Windows программа не умеет работать с кирилицей,\n" +
                    "обратите внимание, чтобы все символы в имени файла и адресе были цифрами или символами латинского алфавита.\n" +
                    "Отвечать на вопросы программы можно используя клвиши Y в значении ДА и N в значении НЕТ,\n" +
                    "также допустимо использование строчных символов y и n.\n");
            System.out.print(System.lineSeparator());
        }

        /**
         * Заготовка для комментария
         */
        do {
            File sourceDir;
            List<File> targetDirList = new ArrayList<>();
            check = true;

            /**
             * Блок ниже запрашивает и проверяет на доступность целевую директорию - директорию из которой необходимо скопировать файлы.
             * Запрос директории происходит в цикле, так что если директория не доступна или не существует, цикл запросит директорию еще раз.
             */
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
            System.out.print(System.lineSeparator());

            /**
             * Блок ниже запрашивает и проверяет на доступность директории в которые необходимо скорпировать файлы.
             * Запрос директории происходит в цикле, так что если директория не доступна или не существует, цикл запросит директорию еще раз.
             * Также цикл будет запрашивать новые целевые директории до тех пор пока не получит в ответ N или n.
             */
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
                    System.out.println("Добавление целевых директорий окончено."+System.lineSeparator());
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
            System.out.print(System.lineSeparator());

            /**
             * Блок ниже запрашивает необходимость удаления файлов. Ожидается, что ответ будет Y/y в случае, если файлы нужно удалить
             * и N/n, если файлы удалять не нужно. Запрос посходит в цикле, так что если получен иной ответ, цикл повторит вопрос.
             */
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
            System.out.print(System.lineSeparator());

            /**
             * В блоке ниже создается новая задача в конструктор которй передается целевая директория, список целевых директорий и переменая
             * del, которая указывает на необходимость удаления файлов из целевой директории. Затем новая задача добавляется в список tasks.
             */
            tasks.add(new Task(sourceDir, targetDirList, del));
            LOGGER.log(Level.INFO, "Task was added to task list");

            /**
             * Блок ниже запрашивает, нужно ли сформировать дополнительные задачи. И если нет - передает управление следующему блоку,
             * а если да, то возвращает управление в начало цикла, блоку запрашивающему целевую директорию.
             */
            check = true;
            do {
                tmp = "";
                LOGGER.log(Level.INFO, "Asking about new tasks");
                System.out.println("Запрашиваются дополнительные задачи...");
                if (askQuestions) {
                    System.out.println("Хотите еще что-нибудь сделать? Y/N");
                }
                tmp = readLine(reader);
                if (tmp.equals("Y") || tmp.equals("y")) {
                    LOGGER.log(Level.INFO, "Got positive answer");
                    System.out.println("Формируем дополнительные задачи...");
                    running = true;
                    check = false;
                } else if (tmp.equals("N") || tmp.equals("n")) {
                    LOGGER.log(Level.INFO, "Got negative answer");
                    System.out.println("Дополнительных задач нет");
                    System.out.println("Начинаем выполнение запланированных задач!");
                    running = false;
                    check = false;
                } else {
                    LOGGER.log(Level.INFO, "Got wrong answer");
                    System.out.println("Ответ не принят. Просто введите один из вариантов Y/N");
                }

            } while (check);
            System.out.print(System.lineSeparator());

        } while (running);

        /**
         * Следующий блок для каждого элемента task листа tasks, вызывает метод copyFiles(), чтобы запустить выполнение task копирования и
         * удаления файлов.
         */
        LOGGER.log(Level.INFO, "Start copying files");
        System.out.println("Проводятся запрошенные операции...");
        for(int i=0;i<tasks.size();i++){
            Task task = tasks.get(i);
            System.out.print(System.lineSeparator());
            System.out.println("Выполняется задача "+(i+1)+" из "+tasks.size());
            task.copyFiles();
        }
        System.out.println("Все задачи выполнены. Завершение работы программы");
        LOGGER.log(Level.INFO, "End the program");
    }

    /**
     * Метод получает BufferedReader в качестве аргумента, считывает и возвращает строку из System.in.
     */
    private static String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Во время чтения команды произошла ошибка. Если ошибка повтооряется перезапустите программу!");
            LOGGER.log(Level.WARNING, "While reading command from command line\n" + e.getStackTrace());
        }
        return new String("");
    }
}
