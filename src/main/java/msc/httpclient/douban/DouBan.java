package msc.httpclient.douban;

import msc.collection.MapConvertFile;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.*;

/**
 * 描述:
 *
 * @outhor lizhichao
 * @create 2018-05-18 16:33
 */
public class DouBan {

    private static List<String> urls;
    static String doubanLogonCookies = "doubanLogonCookies";
    private static Logger logger ;
    private static Random random = new Random(100);
    ;
    private static boolean calledByJob = false;

    static {
        logger = LoggerFactory.getLogger(DouBan.class);
        urls = Arrays.asList(
                "https://www.douban.com/group/topic/131119511/",
                "https://www.douban.com/group/topic/130698339/",
                "https://www.douban.com/group/topic/131156212/",
                "https://www.douban.com/group/topic/131153591/",
                "https://www.douban.com/group/topic/131153118/",
                "https://www.douban.com/group/topic/131119957/"
        );
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        callComment(new HashMap<>(), false);
    }

    @Test
    public void loginTest() throws IOException, ClassNotFoundException {
        new DouBan().login();
    }

    public static void login() throws IOException, ClassNotFoundException {
        String url = "https://accounts.douban.com/login";
        Connection connect = Jsoup.connect(url);
        Connection method = connect.method(Connection.Method.POST).timeout(10000);
        Connection.Response response = method.execute();
        Map<String, String> cookies = response.cookies();
        System.err.println("cookies map before entering password = " + cookies);

        Document tempDoc = response.parse();
        Element imgEle = tempDoc.getElementById("captcha_image");
        Elements attribute = tempDoc.select("input[name]");

        Connection.Response loginResponse;
        Map<String, String> logonCookie;

        String captchaId = "";
        String captchaCode = null;
        if (imgEle != null) {
            for (Element element : attribute) {
                if (element.attr("name").toString().equals("captcha-id")) {
                    captchaId = element.attr("value");
                }
            }
            String src = imgEle.attr("src");
            System.out.println("验证码链接：" + src + "\n请输入验证码");
            Scanner sc = new Scanner(System.in);
            captchaCode = sc.next();
            System.out.println(captchaCode);
        }

        Map<String, String> dataMap = new HashMap<String, String>() {{
            put("form_email", "sail456852@hotmail.com");
            put("form_password", "i1234567");
            put("source", "index_nav");
            put("redir", "https://www.douban.com");
            put("source", "none");
        }};

        if (captchaCode == null) {
            System.err.println("DouBan.login no captcha yeah!");
            if (calledByJob) {
                logger.info("DouBan.login no captcha yeah!");
            }
        } else {
            System.err.println("DouBan.login calling comment function captcha is not empty");
            if (calledByJob) {
                logger.info("DouBan.login calling comment function captcha is not empty");
            }
            dataMap.put("captcha-id", captchaId);
            dataMap.put("captcha-solution", captchaCode);
//            loginResponse = Jsoup
//                    .connect(url)
//                    .data("form_email", "sail456852@hotmail.com", "form_password", "i1234567",
//                            "source", "index_nav", "captcha-id", captchaId, "captcha-solution", captchaCode,
//                            "redir", "https://www.douban.com", "source", "None").cookies(cookies)
//                    .method(Connection.Method.POST).execute();
        }
        loginResponse = Jsoup
                .connect(url)
                .data(dataMap).cookies(cookies)
                .method(Connection.Method.POST).execute();

        logonCookie = loginResponse.cookies();
        MapConvertFile.outputFile(logonCookie, doubanLogonCookies);
        callComment(logonCookie, false);
    }

    @Test
    public void toStringList() {
        String[] arr = {"form_email", "sail456852@hotmail.com", "form_password", "i1234567",
                "source", "index_nav", "redir", "https://www.douban.com", "source", "None"};
        List<String> dataList = new ArrayList<>(Arrays.asList(arr));
//        dataList.addAll(new ArrayList<String>(){{add("captcha-id"); add("testcaptchaid");}});
        dataList.add("captcha-id");
        dataList.add("testcaptchaid");
        System.err.println("dataList = " + dataList);
    }

    @Test
    public void readFromCookies() throws IOException, ClassNotFoundException {
        InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream(doubanLogonCookies);
        ObjectInputStream s = new ObjectInputStream(resourceAsStream);
        HashMap<String, String> dlCookies = (HashMap<String, String>) s.readObject();
        System.err.println("dlCookies = " + dlCookies);
    }

    @Test
    public void writeToCookies() throws IOException, ClassNotFoundException {
        InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream("");
        ObjectInputStream s = new ObjectInputStream(resourceAsStream);
        HashMap<String, String> dlCookies = (HashMap<String, String>) s.readObject();
        System.err.println("dlCookies = " + dlCookies);
    }

    @Test
    public void callCommentTest() throws IOException, ClassNotFoundException {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("testKey", "keyValue");
        MapConvertFile.outputFile(map, "testFileYuzhen");
    }


    @SuppressWarnings("Duplicates")
    public static void callComment(Map<String, String> logonCookie, boolean calledByJob) throws IOException, ClassNotFoundException {
        DouBan.calledByJob = calledByJob;
        logger.info("Call by Comment log");
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("testKey", "keyValue");
//        logger.info("test file yuzhen is executing");
//        MapConvertFile.outputFile(map, "testFileYuzhen");
        // read from class path
        logger.info("read from classpath DouBan.class");
//        HashMap<String, String> dlCookies = getCookiesFromClassPath();
          HashMap<String, String> dlCookies = MapConvertFile.inputFile(doubanLogonCookies);
        System.err.println("logonCookie = " + logonCookie);
        System.err.println("dlCookies = " + dlCookies);
        logger.info("callComment() \"dlCookies\": " + dlCookies);
        if (!calledByJob) {
            logger.info("file cookies used!");
            for (String ur : urls) {
                try {
                    Thread.sleep(3000);
                    huitie(dlCookies, ur, "the area best");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            logger.info("file cookies used! called by job");
            int count = 0;
            for (String ur : urls) {
                count++;
                int i = random.nextInt(100);
                try {
                    Thread.sleep(3000 + i);
                    if (count > 2) {
                        Thread.sleep(30000 + i);  // 30s each
                    }
                    huitie(dlCookies, ur, "up");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static HashMap<String, String> getCookiesFromClassPath() throws IOException, ClassNotFoundException {
        InputStream resourceAsStream = DouBan.class.getClassLoader()
                .getResourceAsStream(doubanLogonCookies);
        ObjectInputStream s = new ObjectInputStream(resourceAsStream);
        return (HashMap<String, String>) s.readObject();
    }

    public static void huitie(Map<String, String> cookies, String url, String rv_comment) throws IOException {
        Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).cookies(cookies).execute();
        Document doc = response.parse();
        Element imgEle = doc.getElementById("captcha_image");
        Elements attribute = doc.select("input[name]");
        String captchaCode = null;
        String captcha = "";
        if (imgEle != null) {
            if (calledByJob) {
                logger.info("captcha is not empty before commenting, cancel this try this time, refresh cookie please");
                return;
            }
            for (Element element : attribute) {
                if (element.attr("name").toString().equals("captcha-id")) {
                    captcha = element.attr("value");
                }
            }
            String src = imgEle.attr("src");
            System.out.println("验证码链接：" + src + "\n请输入验证码");
            Scanner sc = new Scanner(System.in);
            captchaCode = sc.next();
            System.out.println(captchaCode);
        }
        Elements select = doc.select("input[name]");
        String ck = "";
        for (Element element : select) {
            if (element.attr("name").equals("ck")) {
                ck = element.attr("value");
                System.out.println(ck);
            }
        }
        cookies.put("ps", "y");
        cookies.put("ap", "1");
        cookies.put("as", url);
        Connection.Response commentResponse;
        if (captchaCode != null) {
            logger.info("commenting captcha code not empty");
            commentResponse = Jsoup.connect(url + "/add_comment#last").data(
                    "ck", ck, "rv_comment", rv_comment, "start", "0", "submit_btn", "发送", "captcha-id", captcha, "captcha-solution", captchaCode
            ).method(Connection.Method.POST).cookies(cookies).execute();
        } else {
            logger.info("commenting captcha code is empty");
            commentResponse = Jsoup.connect(url + "/add_comment#last").data(
                    "ck", ck, "rv_comment", rv_comment, "start", "0", "submit_btn", "发送"
            ).method(Connection.Method.POST).cookies(cookies).execute();
        }
        String comment = url.replaceAll("comment", "");
        if (calledByJob) {
            logger.info("huitie() \"comment\": " + comment);
        }
        System.err.println("comment = " + comment + "  finished ");
    }

    @Test
    public void url() {
        for (String url : urls) {
            String comment = url.replaceAll("comment", "");
            System.err.println("comment = " + comment);
        }
    }
}

