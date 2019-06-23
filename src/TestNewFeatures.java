import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.JOptionPane;

public class TestNewFeatures {

    private static final LogManager logManager = LogManager.getLogManager();
    private static final Logger LOGGER = Logger.getLogger(TestNewFeatures.class.getName());

    static {
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
    }

    /*public static void main(String[] args) {
        LOGGER.fine("Fine message logged");
        LOGGER.info("Almost get it");
    }*/


        public static void main(String[] args) {
        LOGGER.fine("Fine message logged");
        LOGGER.info("Almost get it");
        try {
            String path = getProgramPath2();

            String fileSeparator = System.getProperty("file.separator");
            String newDir = path + fileSeparator + "newDir2" + fileSeparator;
            JOptionPane.showMessageDialog(null, newDir);

            File file = new File(newDir);
            file.mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProgramPath2() throws UnsupportedEncodingException {
        URL url = TestNewFeatures.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        String parentPath = new File(jarPath).getParentFile().getPath();
        return parentPath;
    }
}