import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
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
    public static String readMetadata(String filename) {
        LOGGER.log(Level.CONFIG, "KeywordsReader.class: readMetadata");
        File file = new File(filename);
        String result;

        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
            LOGGER.log(Level.CONFIG, "KeywordsReader.class: readMetadata Metadata reading from file " + file.getAbsolutePath() + " was successful");
        } catch (ImageProcessingException e) {
            System.out.println("Во время чтения метаданных файла " + file.getAbsolutePath() + " произошла ошибка. Возможно файл недоступен!");
            LOGGER.log(Level.WARNING, "KeywordsReader.class: readMetadata While reading metadata from file " + file.getAbsolutePath() + " an error occurred!\n" + e.getStackTrace());
        } catch (IOException e) {
            System.out.println("Во время чтения метаданных файла " + file.getAbsolutePath() + " произошла ошибка. Возможно файл недоступен!");
            LOGGER.log(Level.WARNING, "KeywordsReader.class: readMetadata While reading metadata from file " + file.getAbsolutePath() + " an error occurred!\n" + e.getStackTrace());
        }
        List<Directory> directoryList = (List<Directory>) metadata.getDirectories();
        result = checkKeywords(directoryList);
        return result;
    }

    /**
     * Метод проверяет EXIF tag WindowsXP keywords
     */
    public static String checkKeywords(List<Directory> tagList) {
        LOGGER.log(Level.CONFIG, "KeywordsReader.class: checkKeywords");
        String result = null;
        for (Directory dir : tagList) {
            if (dir.getName().equals("Exif IFD0")) {
                result = dir.getString(ExifSubIFDDirectory.TAG_WIN_KEYWORDS, "UTF-8");
                break;
            }
        }

        if(result == null){
            for(Directory dir : tagList){
                if(dir.getName().equals("IPTC")){
                    for(Tag tag: dir.getTags()){
                        if(tag.getTagName().equals("Keywords")){
                            result = tag.getDescription();
                            break;
                        }
                    }
                }
            }
        }

        if (result == null) {
            result = new String();
            LOGGER.log(Level.CONFIG, "KeywordsReader.class: checkKeywords There are no keywords in this file!");
        }else {
            LOGGER.log(Level.CONFIG, "KeywordsReader.class: checkKeywords Keywords in the file were found!");
        }
        return result;
    }
}
