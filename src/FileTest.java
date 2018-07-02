import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTest {


        public static void main(String[] args) throws Exception {
            //Создаем поток-чтения-байт-из-файла
           /* FileInputStream inputStream = new FileInputStream("C:\\Users\\Maxim\\Dropbox\\(!)Shurka\\withKeysAndWithout\\sourse\\14_0503_internet_ban_1_key.jpg");
            // Создаем поток-записи-байт-в-файл
            FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Maxim\\Dropbox\\(!)Shurka\\withKeysAndWithout\\target\\14_0503_internet_ban_1_key.jpg");

            while (inputStream.available() > 0) //пока есть еще непрочитанные байты
            {
                int data = inputStream.read(); // прочитать очередной байт в переменную data
                outputStream.write(data); // и записать его во второй поток
            }

            inputStream.close(); //закрываем оба потока. Они больше не нужны.
            outputStream.close();
        }*/

            StringBuilder sb = new StringBuilder("C:\\Users\\Maxim\\Dropbox\\(!)Shurka\\withKeysAndWithout\\sourse\\14_0503_internet_ban_1_key.jpg");
            String resut = sb.replace(sb.length()-3,sb.length(),"eps").toString();
            System.out.println(resut);
    }}

