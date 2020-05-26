package msc.httpclient.depreciatedUtils;

import com.google.common.base.Joiner;
import org.junit.Test;
import com.google.common.base.Splitter;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: eugene<br/>
 * Date: 2019/1/8<br/>
 * Time: 19:17<br/>
 * To change this template use File | Settings | File Templates.
 */
public class CookieUtilTest {

    private String cstr = "ll=118282; bid=4Mjyx_dTshw; ps=y; __utmc=30149280; " +
//            "__utmz=30149280.1546919714.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none);" +
            " _ga=GA1.2.499911770.1546919714;" +
            " _gid=GA1.2.1019472763.1546919793; __utma=30149280.499911770.1546919714.1546919714.1546940298.2; " +
            "ck=fnPz; ap_v=0,6.0; push_noty_num=0; push_doumail_num=0; __utmv=30149280.17623; " +
            "douban-profile-remind=1; _pk_id.100001.8cb4=513fab81cc411b78.1546919714.2.1546940468.1546919804.";
    @Test
    public void cookieStrToMap() {
        Splitter.MapSplitter mapSplitter = Splitter.onPattern(";").withKeyValueSeparator("=");
        Map<String, String> split = mapSplitter.split(cstr);
        for (String s : split.keySet()) {
            System.err.println("key = " + s + ", value=  " + split.get(s));
        }
    }

    @Test
    public void splitUsingHttpCookies() {
        List<HttpCookie> cookies = HttpCookie.parse(cstr);
        for (HttpCookie cookie : cookies) {
            System.err.println("cookie = " + cookie);
        }
    }

    public String splitAndRejoin(String pre) {
        return Joiner.on("/").join(
                Splitter
                        .onPattern("([,.|]|BREAK)")
                        .trimResults()
                        .omitEmptyStrings()
                        .split(pre));
    }

    @Test
    public void splitAndRejoinCaller() {
        assertEquals("a/b/c/d/e/f", splitAndRejoin("a,b,c.d|e BREAK f"));
        assertEquals("A/B/C/D/E/F", splitAndRejoin("A,B,C.D|E BREAK F"));
    }
}
