package interview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: yz<br/>
 * Date: 10/23/2019<br/>
 * Time: 8:25 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class Serilizer {
    public List<String> readFromText(String path) {
        try {
            List<String> strings = Files.readAllLines(Paths.get(path));
            return strings;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeToFile(String path, List<String> lines) {
        try {
            Path path1 = Paths.get(path);
            Files.write(path1, lines, UTF_8, APPEND, CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



