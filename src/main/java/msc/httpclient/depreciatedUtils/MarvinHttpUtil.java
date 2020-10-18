//package msc.httpclient.depreciatedUtils;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.ssl.SSLContexts;
//import org.apache.http.ssl.TrustStrategy;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.net.ssl.SSLContext;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class MarvinHttpUtil {
//
//	private final static Logger logger = LoggerFactory.getLogger(MarvinHttpUtil.class);
//
//
//	private final static String CHARSET = "UTF-8";
//
//	/**
//	 * 模拟form使用post方法提交
//	 * @param url
//	 * @param params
//	 * @return
//	 */
//	public static String postMapDataAsForm(final String url, final Map<String, Object> params){
//		String result = null;
//		try {
//			CloseableHttpClient httpclient = getIgnoeSSLClient();
//			HttpPost httppost = new HttpPost(url);
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			for (Map.Entry<String, Object> entry : params.entrySet()) {
//                nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
//            }
//			httppost.setEntity(new UrlEncodedFormEntity(nvps, CHARSET));
//			HttpResponse response = httpclient.execute(httppost);
//			HttpEntity resEntity = response.getEntity();
//			result = EntityUtils.toString(resEntity, CHARSET);
//		} catch (Exception e) {
//			logger.info("http util error --- {}", e);
//		}
//		return result;
//	}
//
//
//
//
//
//
//	/**
//	 * 1. 将map组装成key1=value1&key2=value2......<br>
//	 * 2. 将字符串直接post出去，接收方必须使用输入流才能收到
//	 * @param url
//	 * @param params
//	 * @return
//	 */
//	public static String postMapData(final String url, final Map<String, Object> params) {
//		return postMapData(url, creatLinkString(params));
//	}
//
//
//	/**
//	 * 使用post将字符串直接post出去，接收方需使用输入流接口信息
//	 * @param url
//	 * @param params
//	 * @return
//	 */
//	public static String postMapData(final String url, final String params) {
//		String result = null;
//		try {
//			CloseableHttpClient httpclient = getIgnoeSSLClient();
//			HttpPost httppost = new HttpPost(url);
//			StringEntity strEntity = new StringEntity(params, CHARSET);
//			httppost.setEntity(strEntity);
//			HttpResponse response = httpclient.execute(httppost);
//			HttpEntity resEntity = response.getEntity();
//			result = EntityUtils.toString(resEntity, CHARSET);
//		} catch (Exception e) {
//			logger.info("http util error --- {}", e);
//		}
//		return result;
//	}
//
//
//	private static String creatLinkString(final Map<String, Object> params){
//		StringBuilder sb = new StringBuilder();
//		for(String key: params.keySet()){
//			sb.append(key).append("=").append(params.get(key)).append("&");
//		}
//		return sb.substring(0, sb.length() - 1);
//	}
//
//	private static CloseableHttpClient getIgnoeSSLClient() throws Exception {
//		SSLContext sslContext = SSLContexts.custom()
//				.loadTrustMaterial(null, new TrustStrategy() {
//					@Override
//					public boolean isTrusted(
//							java.security.cert.X509Certificate[] chain,
//							String authType)
//							throws java.security.cert.CertificateException {
//						return true;
//					}
//				}).build();
//		SSLConnectionSocketFactory sslCSF = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null, new NoopHostnameVerifier());
//		CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslCSF).build();
//		return client;
//	}
//
//}
