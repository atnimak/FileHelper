import java.io.File;
import java.util.List;

public class ProgressChecker implements Runnable{
    @Override
    public void run() {

    }

    private static void printProgress(File file, List<File> files) {
        int index = Math.round(((float) files.indexOf(file) / (float)files.size()) * (float)100);
        if (index<10){

        } else if (index >= 10 && index < 25) {
            System.out.print("20%...");
        } else if (index >= 25 && index < 45) {
            System.out.print("40%...");
        } else if (index >= 45 && index < 65) {
            System.out.print("60%...");
        } else if (index >= 65 && index < 75) {
            System.out.print("70%...");
        } else if (index >= 75 && index < 85) {
            System.out.print("80%...");
        } else if (index >= 85 && index < 95) {
            System.out.print("90%...");
        } else if (index >= 95) {
            System.out.print("95%...");
        } else {
            System.out.println("100%");
        }

    }
}
