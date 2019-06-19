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

public class KeywordsReader {
    public static String metadataReader(String filename) throws IOException {
        File file = new File(filename);
        String result;

        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (ImageProcessingException e) {
            e.printStackTrace();
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

    public static String checkWindowsKeywords(List<Directory> tagList) {
        String result = tagList.get(2).getString(ExifSubIFDDirectory.TAG_WIN_KEYWORDS, "UTF-8");
        return result;
    }

    public static String checkIPTCKeywords(List<Directory> tagList) {

        Collection<Tag> collection = new ArrayList<>();

        try {
            collection = tagList.get(7).getTags();
        } catch (IndexOutOfBoundsException ioobe) {}
        List<Tag> listOfTag = new ArrayList<>(collection);
        String result = null;
        try {
            result = listOfTag.get(6).getDescription();
        } catch (IndexOutOfBoundsException ioobe) {}
        return result;
    }
}
