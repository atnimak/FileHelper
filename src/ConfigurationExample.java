import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ConfigurationExample {

    private static final LogManager logManager = LogManager.getLogManager();
    private static final Logger LOGGER = Logger.getLogger(ConfigurationExample.class.getName());

    /*static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd'at'HH.mm.ss");
        File logDir = new File("Logs");
        if(!logDir.exists()) logDir.mkdirs();
        File logFile = new File("Logs/log"+dateFormat.format(new Date())+".txt");
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream("logger.properties")){
            properties.load(fis);
            properties.setProperty("java.util.logging.FileHandler.pattern",logFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in loading configuration", e);
        }

        try(FileOutputStream fos = new FileOutputStream("logger.properties")){
            properties.store(fos,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in loading configuration", e);
        }

        try {
            logManager.readConfiguration(new FileInputStream("logger.properties"));
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Error in loading configuration", exception);
        }
    }*/

    public static void main(String[] args) {
        LOGGER.fine("Fine message logged");
        LOGGER.info("Almost get it");

    }
}
