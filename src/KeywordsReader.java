import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс считывает ключевые слова из EXIF, jpg-файлов изображений.
 */
public class KeywordsReader {
    private static final Logger LOGGER = Logger.getLogger(KeywordsReader.class.getName());

    /**
     * Метод считывает ключевые слова из EXIF, jpg-файлов изображений.
     */
    public static String metadataReader(String filename) {
        LOGGER.log(Level.CONFIG, "KeywordsReader metadataReader");
        File file = new File(filename);
        String result;

        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (ImageProcessingException e) {
            System.out.println("Во время чтения метаданных файла " + file.getAbsolutePath() + " произошла ошибка. Возможно файл недоступен!");
            LOGGER.log(Level.WARNING, "While reading methadata from file" + file.getAbsolutePath() + "an error occurred!\n" + e.getStackTrace());
        } catch (IOException e) {
            System.out.println("Во время чтения метаданных файла " + file.getAbsolutePath() + " произошла ошибка. Возможно файл недоступен!");
            LOGGER.log(Level.WARNING, "While reading methadata from file" + file.getAbsolutePath() + "an error occurred!\n" + e.getStackTrace());
        }
        List<Directory> tagList = (List<Directory>) metadata.getDirectories();
        String windowsKeywords = checkWindowsKeywords(tagList);
        String IPTCKeywords = checkIPTCKeywords(tagList);

        if (windowsKeywords == null && IPTCKeywords == null) {
            result = new String();
        } else if (windowsKeywords == null && IPTCKeywords != null) {
            result = IPTCKeywords;
        } else if (windowsKeywords != null && IPTCKeywords == null) {
            result = windowsKeywords;
        } else {
            result = windowsKeywords;
        }
        return result;

    }

    /**
     * Метод проверяет EXIF tag Windows keywords
     */
    public static String checkWindowsKeywords(List<Directory> tagList) {
        LOGGER.log(Level.CONFIG, "KeywordsReader checkWindowsKeywords");
        String result = tagList.get(2).getString(ExifSubIFDDirectory.TAG_WIN_KEYWORDS, "UTF-8");
        return result;
    }

    /**
     * Метод проверяет EXIF tag IPTC keywords
     */
    public static String checkIPTCKeywords(List<Directory> tagList) {
        LOGGER.log(Level.CONFIG, "KeywordsReader checkIPTCKeywords");

        Collection<Tag> collection = new ArrayList<>();

        try {
            collection = tagList.get(7).getTags();
        } catch (IndexOutOfBoundsException ioobe) {
        }
        List<Tag> listOfTag = new ArrayList<>(collection);
        String result = null;
        try {
            result = listOfTag.get(6).getDescription();
        } catch (IndexOutOfBoundsException ioobe) {
        }
        return result;
    }
}
