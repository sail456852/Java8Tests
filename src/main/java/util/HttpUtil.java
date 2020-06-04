package util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtil {
    static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    static String HTTP_EXCEPTION = "HTTP EXCEPTION";
    static String IO_EXCEPTION = "IO EXCEPTION";

    private static MultiThreadedHttpConnectionManager connectionManager;
    private static HttpClient client;

    static {
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(10000);
        connectionManager.getParams().setSoTimeout(10000);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);
        connectionManager.getParams().setMaxTotalConnections(20);
        client = new HttpClient(connectionManager);
    }

    public static String doGet(String url, Integer timeout) {
        return doGet(url, null, timeout);
    }

    /**
     * HTTP协议GET请求方法
     */
    public static String httpMethodGet(String url, String gb) {
        if (null == gb || "".equals(gb)) {
            gb = "UTF-8";
        }
        StringBuffer sb = new StringBuffer();
        URL urls;
        HttpURLConnection uc = null;
        BufferedReader in = null;
        try {
            urls = new URL(url);
            uc = (HttpURLConnection) urls.openConnection();
            uc.setConnectTimeout(120000);
            uc.setReadTimeout(120000);
            uc.setRequestMethod("GET");
            uc.connect();
            in = new BufferedReader(new InputStreamReader(uc.getInputStream(),
                    gb));
            String readLine = "";
            while ((readLine = in.readLine()) != null) {
                sb.append(readLine);
            }
            if (in != null) {
                in.close();
            }
            if (uc != null) {
                uc.disconnect();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (uc != null) {
                uc.disconnect();
            }
        }
        return sb.toString();
    }

    public static String doPost(String url, Integer timeout) {
        return doPost(url, null, timeout);
    }

    public static String doGet(String url, Map<String, Object> params, Integer timeout) {
        GetMethod httpGet = null;

        String resultString = "";

        try {
            httpGet = new GetMethod(getUrl(url, params));
            httpGet.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            if (timeout != null && timeout > 0) {
                client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
                client.getHttpConnectionManager().getParams().setSoTimeout(timeout);
            }

            client.executeMethod(httpGet);
            //	resultString = httpGet.getResponseBodyAsString();
            InputStream inputStream = httpGet.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String str = "";
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }
            resultString = stringBuffer.toString();
        } catch (HttpException e) {
            resultString = HTTP_EXCEPTION;

            log.error("HTTP异常", e);
            e.printStackTrace();
        } catch (IOException e) {
            resultString = IO_EXCEPTION;

            log.error("IO异常", e);
            e.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
                httpGet = null;
            }
        }

        return resultString;
    }

    public static String doPost(String url, Map<String, Object> params, Integer timeout) {
        PostMethod httpPost = null;

        String resultString = "";

        try {
            httpPost = new PostMethod(url);
            httpPost.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    Object o = params.get(key);
                    if (o instanceof String) {
                        String val = (String) o;
                        httpPost.addParameter(key, val);
                    } else {
                        if (o.getClass().isArray()) {
                            String[] arr = (String[]) o;
                            for (String val : arr) {
                                httpPost.addParameter(key, val);
                            }
                        }
                    }

                }
            }

            if (timeout != null && timeout > 0) {
                client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
                client.getHttpConnectionManager().getParams().setSoTimeout(timeout);
            }

            client.executeMethod(httpPost);
            resultString = httpPost.getResponseBodyAsString();
        } catch (HttpException e) {
            resultString = HTTP_EXCEPTION;

            log.error("HTTP异常", e);
            e.printStackTrace();
        } catch (IOException e) {
            resultString = HTTP_EXCEPTION;

            log.error("IO异常", e);
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
                httpPost = null;
            }
        }

        return resultString;
    }

    private static String getUrl(String url, Map<String, Object> map) {
        if (null == map || map.keySet().size() == 0) {
            return url;
        }

        StringBuffer sb = new StringBuffer();

        try {
            for (String key : map.keySet()) {
                Object o = map.get(key);

                String value = "";

                if (o instanceof String) {
                    String str = (String) o;
                    value = URLEncoder.encode(str == null ? "" : str, "UTF-8");
                    sb.append(key).append("=").append(value).append("&");
                } else {
                    if (o.getClass().isArray()) {
                        String[] arr = (String[]) o;
                        for (String val : arr) {
                            value = URLEncoder.encode(val == null ? "" : val, "UTF-8");
                            sb.append(key).append("=").append(value).append("&");
                        }
                    }
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url = url + "?" + sb.substring(0, sb.length() - 1);

        return url;
    }

//	public static String doPostToMiaofu(String url,Map<String,String> map,String charset){
//        SSLClient httpClient = null;
//        HttpPost httpPost = null;
//        String result = null;
//        try{
//            httpClient = new SSLClient();
//            httpPost = new HttpPost(url);
//            //设置参数
//            List<NameValuePair> list = new ArrayList<NameValuePair>();
//            Iterator iterator = map.entrySet().iterator();
//            while(iterator.hasNext()){
//                Entry<String,String> elem = (Entry<String, String>) iterator.next();
//                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
//            }
//            if(list.size() > 0){
//                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
//                httpPost.setEntity(entity);
//            }
//            HttpResponse response = httpClient.execute(httpPost);
//            if(response != null){
//                HttpEntity resEntity = response.getEntity();
//                if(resEntity != null){
//                    result = EntityUtils.toString(resEntity,charset);
//                }
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return result;
//    }

}
