package msc.httpclient.douban;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.HashMap;

/****
 *author:lizhichao
 *date:2018-05-19 23:34
 *description
 **/
public class BaiduApiDemo {

    public static void main(String[] args) throws IOException {
        HashMap<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", "g9xQEKFwbhSupt3G1sj0vdVK");
        params.put("client_secret", "Tsu1EoOnd8MhSImntmPoI1UUtgWxmOK3");
        Connection.Response execute = Jsoup.connect("https://aip.baidubce.com/oauth/2.0/token")
                .ignoreContentType(true)
                .header("Accept", "text/html, application/xhtml+xml, */*")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0))")
                .timeout(10000)
                .data(params).method(Connection.Method.GET).execute();

        String parse = execute.parse().text();
        JSONObject jsonObject = JSON.parseObject(parse);
        String access_token = jsonObject.getString("access_token");
        System.out.println(access_token);
        String s = OCRVCode("C:\\Users\\Administrator\\captcha.jpg", access_token);
        System.out.println(s);
    }

    public static String OCRVCode(String imageUrl, String access_token) throws IOException {
        HashMap<String, String> params = new HashMap<>();
        imageUrl = GetImageStr(imageUrl);
        params.put("image", imageUrl);
        params.put("language_type", "ENG");
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_enhanced";
        Connection.Response execute = Jsoup.connect(url + "?access_token=" + access_token)
                .ignoreContentType(true)
                .header("Accept", "text/html, application/xhtml+xml, */*")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0))")
                .timeout(10000)
                .data(params).method(Connection.Method.POST).execute();

        return execute.parse().text();
    }


    //图片转化成base64字符串
    public static String GetImageStr(String imgFile) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }

    //base64字符串转化成图片
    public static boolean GenerateImage(String imgStr) {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "d://222.jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
