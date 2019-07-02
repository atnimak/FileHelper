import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * Класс настраивает Logger и LogManager. Формирует формат логов и место хранения. Все настройки хранит в файле logger.properties
 */
public class LogConfigurator {
    private static final LogManager logManager = LogManager.getLogManager();
    private static final Logger LOGGER = Logger.getLogger(LogConfigurator.class.getName());

    public static void configureLog() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd'at'HH.mm.ss");
        File logDir = new File("Logs");
        if (!logDir.exists()) makeDir(logDir);
        File logFile = new File("Logs/log" + dateFormat.format(new Date()) + ".txt");
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream("logger.properties")) {
            properties.load(fis);
            properties.setProperty("java.util.logging.FileHandler.pattern", logFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in loading configuration", e);
        }

        try (FileOutputStream fos = new FileOutputStream("logger.properties")) {
            properties.store(fos, null);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in loading configuration", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in loading configuration", e);
        }

        try {
            logManager.readConfiguration(new FileInputStream("logger.properties"));
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Error in loading configuration", exception);
        }
    }
    /**
     * Метод проверяет есть ли в директории с исполняемым файлов каталог Logs для хранения логов. Если нет - создает каталог.
     */
    private static void makeDir(File logDir) {
        logDir.mkdirs();
        if(!logDir.exists()){
            try {
                String path = getProgramPath();
                String fileSeparator = System.getProperty("file.separator");
                String newDir = path + fileSeparator + logDir.getName() + fileSeparator;
                File file = new File(newDir);
                file.mkdir();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getProgramPath() throws UnsupportedEncodingException {
        URL url = MainFileHelper.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        String parentPath = new File(jarPath).getParentFile().getPath();
        return parentPath;
    }
}
