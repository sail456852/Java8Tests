package msc.httpclient.kuaidi100.utils;

import msc.httpclient.kuaidi100.config.Const;
import msc.httpclient.kuaidi100.pojo.TaskRequest;
import msc.httpclient.kuaidi100.pojo.TaskResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HttpRequest {

    private static Logger logger = LoggerFactory.getLogger(HttpRequest.class);

	public static String addUrl(String head, String tail) {
		if (head.endsWith("/")) {
			if (tail.startsWith("/")) {
				return head.substring(0, head.length() - 1) + tail;
			} else {
				return head + tail;
			}
		} else {
			if (tail.startsWith("/")) {
				return head + tail;
			} else {
				return head + "/" + tail;
			}
		}
	}

	public  static String postData(String url, Map<String, String> params, String codePage) throws Exception {

		final HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10 * 1000);

		final PostMethod method = new PostMethod(url);
		if (params != null) {
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, codePage);
			method.setRequestBody(assembleRequestParams(params));
		}
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = new String(method.getResponseBody(), codePage);
		} catch (final Exception e) {
			throw e;
		} finally {
			method.releaseConnection();
		}
		return result;
	}

	public synchronized static String postData(String url, String codePage) throws Exception {
		final HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10 * 1000);

		final GetMethod method = new GetMethod(url);
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = new String(method.getResponseBody(), codePage);
		} catch (final Exception e) {
			throw e;
		} finally {
			method.releaseConnection();
		}
		return result;
	}

	/**
	 * 组装http请求参数
	 * 
	 * @param data
     * @return
	 */
	private synchronized static NameValuePair[] assembleRequestParams(Map<String, String> data) {
		final List<NameValuePair> nameValueList = new ArrayList<NameValuePair>();

		Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			nameValueList.add(new NameValuePair((String) entry.getKey(), (String) entry.getValue()));
		}

		return nameValueList.toArray(new NameValuePair[nameValueList.size()]);
	}

    public static void main(String[] args) {
        TaskRequest task = new TaskRequest();
        task.setCompany("yunda");
        task.setNumber("8000039826529");
        task.getParameters().put(Const.CALLBACK_URL_NAME, Const.CALLBACK_URL_VALUE);
        task.setKey(Const.KEY);
        post(task);
    }

    public static TaskResponse post(TaskRequest task) {
        TaskResponse resp = new TaskResponse();

        HashMap<String, String> p = new HashMap<String, String>();
        p.put("schema", "json");
        p.put("param", JacksonHelper.toJSON(task));
        try {
            String ret = HttpRequest.postData(Const.POST_URL, p, Const.ENCODING);
            resp = JacksonHelper.fromJSON(ret, TaskResponse.class);

            boolean isSuccess = resp.getResult();

            System.err.println("ret = " + ret);
            if (isSuccess) {
                logger.info("订阅快递100成功,快递单号：" + task.getNumber());
                System.err.println("订阅快递100成功,快递单号：" + task.getNumber());
//                ShopOrderService.updateExpressSubscribeStatus(task.getNumber(), SUBSCRIBE_SUCCESS);
            } else {
                int code = Integer.parseInt(resp.getReturnCode());
                logger.info("订阅快递100失败,快递单号：" + task.getNumber() + ",返回代码：" + code + ",失败原因：" + resp.getMessage());
                System.err.println("订阅快递100失败,快递单号：" + task.getNumber() + ",返回代码：" + code + ",失败原因：" + resp.getMessage());

                if (code == 501) {
//                    ShopOrderService.updateExpressSubscribeStatus(task.getNumber(), SUBSCRIBE_SUCCESS);
                } else {
//                    ShopOrderService.updateExpressSubscribeStatus(task.getNumber(), code);
                }
            }
        } catch (Exception e) {
            logger.error("订阅快递100异常：" + e.getMessage());
            e.printStackTrace();
        }

        return resp;
    }

}
