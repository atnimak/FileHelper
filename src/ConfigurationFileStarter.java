import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationFileStarter {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationFileStarter.class.getName());

    public static void setArguments(File configurationFile) {
        List<String> args = new ArrayList<String>();
        try {
            args = Files.readAllLines(configurationFile.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("Во время чтения файла конфигурации прозошла ошибка. Возможно файл недоступен!");
            LOGGER.log(Level.WARNING, "Error in loading configuration file", e);
        }
        String[] result = args.toArray(new String[0]);
        ArgumentStarter.setArguments(result);
    }
}