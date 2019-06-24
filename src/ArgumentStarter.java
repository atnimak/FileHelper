import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArgumentStarter {
    public static void main(String... args){


        File file = new File("C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\target1\\3_0111_01_audience.jpg");
        File sourceFileDir=new File("C:\\Users\\Maxim\\JavaProjects\\TestArchifacts\\FileHelper\\FilesForTest\\sourse");
        try {

            Path temp = Files.move(Paths.get(file.getAbsolutePath()),
                    Paths.get(sourceFileDir.getAbsolutePath()+"\\"+file.getName()));
            if(temp == null) throw new IOException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
