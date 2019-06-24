import java.io.*;

public class ArgumentStarter {
    public static void getArguments(String... args){
        //кладем данные в строку
        StringBuilder sb = new StringBuilder();
        sb.append("C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\sourse").append('\n');
        sb.append("C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target1").append('\n');
        sb.append("C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target2").append('\n');
        sb.append("n").append('\n');
        sb.append("y").append('\n');
        sb.append("n").append('\n');

        String data = sb.toString();
        //Оборачиваем строку в класс ByteArrayInputStream
        InputStream is = new ByteArrayInputStream(data.getBytes());
        //подменяем in
        System.setIn(is);
    }
}
