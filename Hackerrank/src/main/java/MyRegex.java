import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: Eugene_Wang<br/>
 * Date: 7/27/2020<br/>
 * Time: 3:30 PM<br/>
 * To change this template use File | Settings | File Templates.
 * @author Eugene_Wang
 */
public class MyRegex {
    private static final String zeroTo255 = "([0-9]|[0-9][0-9]|(0|1)[0-9][0-9]|2[0-4][0-9]|25[0-5])";
    private static final String patternString = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;


    public static void main(String[] args) {
        MyRegex javaRegex = new MyRegex();
        String sampleTestStrings = "000.12.12.034\n" +
                "121.234.12.12\n" +
                "23.45.12.56\n" +
                "00.12.123.123123.123\n" +
                "122.23\n" +
                "Hello.IP";
        String[] split = sampleTestStrings.split("\n");
        List<String> list = Arrays.asList(split);

        for (String testString : list) {
            System.err.println(testString);
            boolean b = javaRegex.validateIPString(testString);
            System.err.println(b);
        }
    }

    public boolean validateIPString(String testString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(testString);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    @Test
    public void test1() {
        boolean b = validateIPString("00.12.123.123123.123");
        System.err.println(b);
    }
}
