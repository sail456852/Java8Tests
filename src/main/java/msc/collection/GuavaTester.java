package msc.collection;

import com.google.common.base.Splitter;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: eugene<br/>
 * Date: 2019/1/8<br/>
 * Time: 19:32<br/>
 * To change this template use File | Settings | File Templates.
 */
public class GuavaTester {
   private String cstr = "ll=118282; bid=4Mjyx_dTshw; ps=y; __utmc=30149280; " +
//           "__utmz=30149280.1546919714.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.499911770.1546919714;" +
           " _gid=GA1.2.1019472763.1546919793; __utma=30149280.499911770.1546919714.1546919714.1546940298.2; " +
           "ck=fnPz; ap_v=0,6.0; push_noty_num=0; push_doumail_num=0; __utmv=30149280.17623; " +
           "douban-profile-remind=1; _pk_id.100001.8cb4=513fab81cc411b78.1546919714.2.1546940468.1546919804.";

    public static void main(String args[]) {
        GuavaTester tester = new GuavaTester();
        Iterable<String> strings = tester.testSplitter();
        strings.forEach(System.out::println);
    }

    private Iterable<String> testSplitter() {
        Iterable<String> split = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split("the ,quick, ,brown, fox, jumps, over, the, lazy, little dog.");
        return split;
    }

    @Test
    public void splitter() {
        Iterable<String> split = Splitter.on(';')
                .trimResults()
                .omitEmptyStrings()
                .split(cstr);
        for (String s : split) {
            System.err.println("s = " + s);
        }
    }
}
