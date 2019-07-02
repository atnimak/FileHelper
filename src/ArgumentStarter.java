import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * Класс собирает полученные аргументы в строку и подменяет System.in, чтобы метод main()  в классе MainFileHelper считывая строки
 * из System.in получил нужные адреса папок и настройки в нужный момент.
 */
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
