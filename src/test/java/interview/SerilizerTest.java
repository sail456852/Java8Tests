package interview;

import com.google.common.base.Splitter;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: yz<br/>
 * Date: 10/23/2019<br/>
 * Time: 8:44 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
class SerilizerTest {
    private Serilizer s = new Serilizer();

    @Test
    void readFromText() {
        String outputPath = "C:\\Users\\sail4\\Desktop\\data collector\\data collector\\model_output_me.txt";
        String path1 = "C:\\Users\\sail4\\Desktop\\data collector\\data collector\\input_files\\pipe.txt";
        String path2 = "C:\\Users\\sail4\\Desktop\\data collector\\data collector\\input_files\\comma.txt";
        String path3 = "C:\\Users\\sail4\\Desktop\\data collector\\data collector\\input_files\\space.txt";
        List<String> lines = s.readFromText(path1);
        List<String> lines2 = s.readFromText(path2);
        List<String> lines3 = s.readFromText(path3);
        List<People> list1 = lines.stream().map(line -> splitOneLine(line, "|")).collect(Collectors.toList());
        List<People> list2 = lines2.stream().map(line -> splitOneLine(line, ",")).collect(Collectors.toList());
        List<People> list3 = lines3.stream().map(line -> splitOneLine(line, " ")).collect(Collectors.toList());

        ArrayList<People> list = new ArrayList<People>(list1);
        list.addAll(list2);
        list.addAll(list3);

        // 1.order by by sex Female first then by FirstName
        List<String> sortedBySex = list.stream().sorted(Comparator.comparing(People::getSex).thenComparing(People::getFirstName)).map(People::toString).collect(Collectors.toList());
        ArrayList<String> outputList = new ArrayList<>();

        outputList.add("Output1:  \n");
        outputList.addAll(sortedBySex);
        // 2.order by by birthDay
        List<String> sortedByBirthday = list.stream().sorted(Comparator.comparing(People::getBirthDay)).map(People::toString).collect(Collectors.toList());
        outputList.add("\nOutput2:  \n");
        outputList.addAll(sortedByBirthday);
        // 3.order by FirstName alphabet in reversed order
        List<String> sortedByFirstName = list.stream().sorted(Comparator.comparing(People::getFirstName).reversed()).map(People::toString).collect(Collectors.toList());
        outputList.add("\nOutput3:  \n");
        outputList.addAll(sortedByFirstName);
        for (String s1 : outputList) {
            System.err.println("s1 = " + s1);
        }

        s.writeToFile(outputPath, outputList);

    }


    private People splitOneLine(String line, String separator) {
        String lineNew = line;
        Iterable<String> splintedWord = Splitter.on(separator).omitEmptyStrings().trimResults().split(lineNew);
        List<String> words = StreamSupport.stream(splintedWord.spliterator(), false).map(v -> v.replaceAll("-", "/")).collect(Collectors.toList());
        if("|".equals(separator) || " ".equals(separator)){
            // remove  3rd position element
            for (int i = 0; i < words.size(); i++) {
                if(i ==  2) {
                    words.remove(2);
                }
            }
            Collections.replaceAll(words, "M", "Male");
            Collections.replaceAll(words, "F", "Female");
        }

        if("|".equals(separator) || ",".equals(separator)){
            // exchange 3rd and 4th elements
            Collections.swap(words, 3, 4); // mutable
        }

        People p = new People();
        p.setFirstName(words.get(0));
        p.setLastName(words.get(1));
        p.setSex(words.get(2));
        try {
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(words.get(3));
            p.setBirthDay(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        p.setColor(words.get(4));
        return p;
    }


    @Test
    public void writeToFile() {
        String path = "C:\\Users\\sail4\\Desktop\\data collector\\data collector\\model_output_me.txt";
        List<String> list = new ArrayList<String>(){{
            add("line 1 ..............");
            add("line 2 ..............");
            add("\n");
            add("line 3 ..............");
            add("\n\n");
            add("line 5 ..............");
        }};
        s.writeToFile(path, list);
    }

}