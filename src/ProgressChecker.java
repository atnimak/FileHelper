import java.io.File;
import java.util.List;
import java.util.Random;

public class ProgressChecker {
    static int progress = 0;

    public static void main(String... args) {

        int i = new Random().nextInt(100);
        System.out.println(i);
        System.out.println((int) (Math.rint((double) i / 10) * 10));
    }

    public static void resetProgress(){
        progress = 0;
    }

    public static void checkProgress(File file, List<File> files) {
        int index = Math.round(((float) files.indexOf(file) / (float) files.size()) * (float) 100);
        index = (int) (Math.rint((double) index / 10) * 10);

        if (index > progress) {
            progress = index;
            switch (progress) {
                case 10:
                    System.out.print("10%...");
                    break;
                case 20:
                    System.out.print("20%...");
                    break;
                case 30:
                    System.out.print("30%...");
                    break;
                case 40:
                    System.out.print("40%...");
                    break;
                case 50:
                    System.out.print("50%...");
                    break;
                case 60:
                    System.out.print("60%...");
                    break;
                case 70:
                    System.out.print("70%...");
                    break;
                case 80:
                    System.out.print("80%...");
                    break;
                case 90:
                    System.out.print("90%...");
                    break;
                case 100:
                    System.out.println("100%");
                    break;




           /* if (index<10){

            } else if (progress >= 10 && index < 25) {
                System.out.print("20%...");
            } else if (progress >= 25 && index < 35) {
                System.out.print("30%...");
            } else if (progress >= 25 && index < 45) {
                System.out.print("40%...");
            } else if (progress >= 45 && index < 65) {
                System.out.print("60%...");
            } else if (progress >= 65 && index < 75) {
                System.out.print("70%...");
            } else if (progress >= 75 && index < 85) {
                System.out.print("80%...");
            } else if (progress >= 85 && index < 95) {
                System.out.print("90%...");
            } else if (progress >= 95) {
                System.out.print("95%...");
            } else {
                System.out.println("100%");
            }*/

            }


        }
    }
}
