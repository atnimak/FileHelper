import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ArgumentStarter {
    public static void setArguments(String... args) {
        /**
         * Собираем аргументы в StringBuilder, кладем данные в строку, Оборачиваем строку в класс ByteArrayInputStream,подменяем in
         */
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append('\n');
        }
        String data = sb.toString();
        InputStream is = new ByteArrayInputStream(data.getBytes());
        System.setIn(is);
    }
}
