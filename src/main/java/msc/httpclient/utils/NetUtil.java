package msc.httpclient.utils;

import msc.collection.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


public class NetUtil {
	static final Log log = LogFactory.getLog(NetUtil.class);
	
	public static final int MAX_NUM = 50;
	
	//确认订单是否付款成功
	public static final String CHK_URL = "http://www.yeahka.com/cgi-bin/machordernotifyconform.cgi?";


	public static String getLocalHostName() {
		try {
			InetAddress a = InetAddress.getLocalHost();
			return a.getHostName();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public static String getAddByIP(String ip) {
		String url = "http://www.ip138.com/ips138.asp?ip=" + ip;
		String html = getHtml(url);
		String addr = html.substring(html.indexOf("<li>本站主数据：") + 10, html.indexOf("</li><li>参考数据"));
		return addr;
	}

	public static Map<String, String> getXml(String urlStr, Object... obj) throws Exception {
		return XmlUtil.xml2Map(getHtml(urlStr, obj));
	}

	public static JSONObject getJson(String urlStr, Object... obj) throws Exception {
		return new JSONObject(getHtml(urlStr, obj));
	}

	public static String getHtml(String urlStr, Object... obj) {
		String encoding = "gb2312";
		if (obj != null && obj.length > 0) {
			encoding = (String) obj[0];
		}
		URL url;
		StringBuffer sb = new StringBuffer();
        httpConnection(urlStr, encoding, sb);
        log.info("result  interface<<<<<<" + sb.toString());
		return sb.toString();
	}

    private static void httpConnection(String urlStr, String encoding, StringBuffer sb) {
        URL url;
        try {
            url = new URL(urlStr);
            log.info("request interface>>>>>>" + url);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;

            httpConnection.setReadTimeout(60000);

            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream urlStream = httpConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream, encoding));
                String sCurrentLine = "";
                while ((sCurrentLine = bufferedReader.readLine()) != null) {
                    sb.append(sCurrentLine);
                }
            } else {
                throw new RuntimeException("get URLConection fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("get URLConection fail", e);
        }
    }

    public static String getFullUrl(String baseUrl, String url) throws Exception {
		String line = url;
		if (!url.toLowerCase().startsWith("http://")) {
			URL r = new URL(baseUrl);
			String port = (r.getPort() == 80 || r.getPort() == -1) ? "" : ":" + (r.getPort());
			if (url.startsWith("/")) {
				line = r.getProtocol() + "://" + r.getHost() + port + url;
			} else {
				String path = r.getPath();
				path = path.substring(0, path.lastIndexOf("/") + 1);
				if ("".equals(path))
					path = "/";
				line = r.getProtocol() + "://" + r.getHost() + port + path + url;
			}
		}
		return line;
	}

	public static void downLoadImage(URL url, File file) throws Exception {
		File dir = file.getParentFile();
		if (!dir.exists())
			dir.mkdirs();

		try {
			HttpURLConnection imgConnection = (HttpURLConnection) url.openConnection();
			BufferedInputStream fileIn = new BufferedInputStream(imgConnection.getInputStream());
			DataOutputStream fileOut = new DataOutputStream(new FileOutputStream(file));
			int c = 0;
			while ((c = fileIn.read()) != -1) {
				fileOut.write(c);
			}
			fileOut.flush();
			fileOut.close();
			fileIn.close();
		} catch (Exception e) {
			System.err.println("下载图片文件" + file.getPath() + "失败：" + e.getMessage());
		}
	}






	public static Map<String, String> getXmlWxPay(String urlStr, Object... obj) throws Exception {
		return XmlUtil.xml2Map(getHtmlWxPay(urlStr, obj));
	}
	
	public static String getHtmlWxPay(String urlStr, Object... obj) {
		String encoding = "utf-8";
		if (obj != null && obj.length > 0) {
			encoding = (String) obj[0];
		}
		URL url;
		StringBuffer sb = new StringBuffer();
        httpConnection(urlStr, encoding, sb);
        log.info("result  interface<<<<<<" + sb.toString());
		return sb.toString();
	}

	
	public static void main(String[] args) throws Exception{
		FileReader fr = new FileReader("d:/1.txt");
        BufferedReader bf = new BufferedReader(fr);
        String s = null;
        Set<String> set = new LinkedHashSet<String>();
        while ((s=bf.readLine())!=null){
            if(s.length()==0){
                continue;
            }
            set.add(s);
//            System.out.println("~~~" + s);
//            NetUtil.sendMsg(s, msg, "127.0.0.1", "online_sale_msg");
        }
        System.out.println(set.size());
        
        String ss = "";
        for(String m : set){
        	if(ss.length() > 0){
        		ss += ",";
        	}
        	ss += m;
        }
        System.out.println(ss);
	}
	
}
