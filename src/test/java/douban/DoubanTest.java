package douban;

import com.google.common.base.Splitter;
import msc.httpclient.utils.CookieUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.riversun.okhttp3.OkHttp3CookieHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: eugene<br/>
 * Date: 2019/1/8<br/>
 * Time: 17:44<br/>
 * To change this template use File | Settings | File Templates.
 */
public class DoubanTest {
    private String cstr = "ll=118282; bid=4Mjyx_dTshw; ps=y; " +
//                "__utmc=30149280; __utmz=30149280.1546919714.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none);" +
            " _ga=GA1.2.499911770.1546919714; _gid=GA1.2.1019472763.1546919793; ck=fnPz; push_noty_num=0; push_doumail_num=0;" +
            " __utmv=30149280.17623; douban-profile-remind=1; _pk_ses.100001.8cb4=*; ap_v=0,6.0; " +
            "__utma=30149280.499911770.1546919714.1546940298.1546997720.3; __utmt=1;" +
            " _pk_id.100001.8cb4=513fab81cc411b78.1546919714.3.1546997869.1546940468.; __utmb=30149280.10.10.1546997720";

    @Test
    public void javaSoupDouban() throws IOException {
        String url = "https://www.douban.com/doumail/";

        Map<String, String> params = setRequestParam();
        Connection conn = Jsoup.connect(url);
//        conn.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
//        conn.data(params);
        conn.header("Connection", "keep-alive");
        conn.header("Pragma", "no-cache");
        conn.header("Cache-Control", "no-cache");
        conn.header("Upgrade-Insecure-Requests", "1");
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        conn.header("Referer", "https://www.douban.com/people/176233839/");
        conn.header("Accept-Encoding", "gzip, deflate, br");
        conn.header("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,ja;q=0.6,zh-TW;q=0.5,ru;q=0.4");
        conn.header("Cookie", "ll=\"118282\"; bid=4Mjyx_dTshw; ps=y; __utmc=30149280; __utmz=30149280.1546919714.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.499911770.1546919714; _gid=GA1.2.1019472763.1546919793; ck=fnPz; push_noty_num=0; push_doumail_num=0; __utmv=30149280.17623; douban-profile-remind=1; _pk_ses.100001.8cb4=*; ap_v=0,6.0; __utma=30149280.499911770.1546919714.1546940298.1546997720.3; __utmt=1; _pk_id.100001.8cb4=513fab81cc411b78.1546919714.3.1546998613.1546940468.; __utmb=30149280.14.10.1546997720");
        conn.header("cache-control", "no-cache");
        conn.header("Postman-Token", "509eac00-708c-40c7-a42c-0414b895a023");
        conn.header("Host", "www.douban.com");


        Map<String, String> cookieMap = CookieUtil.cookieStrToMap(cstr);
        Map<String, String> cookieMapNew = new HashMap<>();
        cookieMapNew.putAll(cookieMap);
        cookieMapNew.put("__utmz=", "30149280.1546919714.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
        conn.cookies(cookieMapNew);
        Document document = conn.get();
        System.err.println("document = " + document);
    }

    @Test
    public void okHttp() throws IOException {
        String url = "https://www.douban.com/doumail/";
        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        setCookies(url, cookieHelper);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();
        Request request = new Request.Builder()
                .url(url).get()
                .addHeader("Connection", "keep-alive")
                .addHeader("Pragma", "no-cache")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Referer", "https://www.douban.com/people/176233839/")
                .addHeader("Cookie", "ll=\"118282\"; bid=4Mjyx_dTshw; ps=y; __utmc=30149280; __utmz=30149280.1546919714.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.499911770.1546919714; _gid=GA1.2.1019472763.1546919793; ck=fnPz; push_noty_num=0; push_doumail_num=0; __utmv=30149280.17623; douban-profile-remind=1; _pk_ses.100001.8cb4=*; ap_v=0,6.0; __utma=30149280.499911770.1546919714.1546940298.1546997720.3; __utmt=1; _pk_id.100001.8cb4=513fab81cc411b78.1546919714.3.1546998613.1546940468.; __utmb=30149280.14.10.1546997720")
                .addHeader("cache-control", "no-cache")
                .addHeader("Host", "www.douban.com")
                .addHeader("Postman-Token", "509eac00-708c-40c7-a42c-0414b895a023")
                .build();
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                .get()
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Pragma", "no-cache")
//                .addHeader("Cache-Control", "no-cache")
//                .addHeader("Upgrade-Insecure-Requests", "1")
////                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
////                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//                .addHeader("Referer", "https://www.douban.com/people/176233839/")
//                .addHeader("Accept-Encoding", "gzip, deflate, br")
//                .addHeader("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,ja;q=0.6,zh-TW;q=0.5,ru;q=0.4")
//                .addHeader("Cookie", "ll=\"118282\"; bid=4Mjyx_dTshw; ps=y; __utmc=30149280; __utmz=30149280.1546919714.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.499911770.1546919714; _gid=GA1.2.1019472763.1546919793; ck=fnPz; push_noty_num=0; push_doumail_num=0; __utmv=30149280.17623; douban-profile-remind=1; _pk_ses.100001.8cb4=*; ap_v=0,6.0; __utma=30149280.499911770.1546919714.1546940298.1546997720.3; __utmt=1; _pk_id.100001.8cb4=513fab81cc411b78.1546919714.3.1546998613.1546940468.; __utmb=30149280.14.10.1546997720")
//                .addHeader("cache-control", "no-cache")
//                .addHeader("Postman-Token", "509eac00-708c-40c7-a42c-0414b895a023")
//                .build();
//
        Response response = client.newCall(request).execute();
        System.err.println("response.body().string() = " + response.body().string());

    }

    /**
     * @author: eugene @date: 2019/1/10
     * Keep
     * @throws IOException
     * sample:
     *  <a rel="direct" title="真的要举报为垃圾豆邮？"
     *       data-id="123379059"
     *       data-slink="https://www.douban.com/people/123379059/"
     *       data-sname="球球4400"
     *       data-user="🐰小姐"
     */
    @Test
    public void doubanClient() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.douban.com/doumail/")
                .get()
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//                .addHeader("Accept-Encoding", "gzip, deflate, br")
//                .addHeader("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,ja;q=0.6,zh-TW;q=0.5,ru;q=0.4")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", "ll=\"118282\"; bid=4Mjyx_dTshw; ps=y; __utmc=30149280; __utmz=30149280.1546919714.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.499911770.1546919714; dbcl2=\"176233839:tKLLECsXumE\"; ck=fnPz; push_noty_num=0; push_doumail_num=0; __utmv=30149280.17623; douban-profile-remind=1; _pk_id.100001.8cb4=513fab81cc411b78.1546919714.5.1547083542.1547017668.; _pk_ses.100001.8cb4=*; ap_v=0,6.0; __utma=30149280.499911770.1546919714.1547017289.1547083542.5; __utmb=30149280.2.10.1547083542")
                .addHeader("Host", "www.douban.com")
                .addHeader("Pragma", "no-cache")
                .addHeader("Referer", "https://www.douban.com/people/176233839/")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "7d04bc29-d583-4773-a23b-b734703f5e8a")
                .build();

        Response response = client.newCall(request).execute();
        System.err.println("response.body().string() = " + response.body().string());

    }

    private void setCookies(String url, OkHttp3CookieHelper cookieHelper) {
        Splitter.MapSplitter mapSplitter = Splitter.onPattern(";").withKeyValueSeparator("=");
        Map<String, String> cookieMap = mapSplitter.split(cstr);
        for (String key : cookieMap.keySet()) {
            String value = cookieMap.get(key);
            cookieHelper.setCookie(url, key, value);
        }
        String sKey = "__utmz=";
        String sValue = "30149280.1546919714.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)";
        cookieHelper.setCookie(url, sKey, sValue);
    }


    @NotNull
    private Map<String, String> setRequestParam() {
        Map<String, String> params = new HashMap<>();
        params.put("ck", "fnPz");
        params.put("k", "176233839:6645873e4815c0cf2257c526e58b9a1ddb73928e");
        params.put("from_push", "undefined");
        params.put("callback", "jsonp_j0x74alhmjxly0y");
        return params;
    }


    /**
     * https://stackoverflow.com/questions/7139178/jsoup-cookies-for-https-scraping/10533366
     *
     * @throws IOException
     */
    @Test
    public void stackOverFlow() throws IOException {

        //This will get you the response.
        Connection.Response res = Jsoup
                .connect("url")
                .data("loginField", "login@login.com", "passField", "pass1234")
                .method(Connection.Method.POST)
                .execute();
        //This will get you cookies
        Map<String, String> cookies = res.cookies();
        //And this is the easieste way I've found to remain in session
        Document doc = Jsoup.connect("url").cookies(cookies).get();
    }

}
