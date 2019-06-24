import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ArgumentStarter {
    public static void setArguments(String... args) {
        //Собираем аргументы в StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append('\n');
        }
        //Кладем данные в строку
        String data = sb.toString();
        //Оборачиваем строку в класс ByteArrayInputStream
        InputStream is = new ByteArrayInputStream(data.getBytes());
        //подменяем in
        System.setIn(is);
    }
}
