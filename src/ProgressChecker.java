import java.io.File;
import java.util.List;

/**
 * Класс проверяет и выводит на консоль прогресс копирования и удаления файлов.
 */

public class ProgressChecker {
    static boolean finishCheck = true;
    static int progress = 0;

    public static void resetProgress() {
        finishCheck = true;
        progress = 0;
    }

    /**
     * Метод сначала проверяет какой сколько файлов скопировано\удалено, а сколько осталось. Вычисляет процент выполненного.
     * Затем округляет процент до десятков и выводит на консоль.
     */
    public static void checkProgress(File file, List<File> files) {
        int index = Math.round(((float) files.indexOf(file) / (float) files.size()) * (float) 100);
        index = (int) (Math.rint((double) index / 10) * 10);

        if (index > progress) {
            progress = index;
            switch (progress) {
                case 0:
                    System.out.print("1%...");
                    break;
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
                    finishCheck = false;
                    break;
            }
        }

        if (files.indexOf(file) == files.size() - 1 && finishCheck) {
            System.out.println("100%");
            finishCheck = false;
        }

    }
}
