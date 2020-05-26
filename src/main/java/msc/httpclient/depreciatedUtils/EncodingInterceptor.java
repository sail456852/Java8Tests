package msc.httpclient.depreciatedUtils;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: eugene<br/>
 * Date: 2019/1/9<br/>
 * Time: 10:20<br/>
 * To change this template use File | Settings | File Templates.
 */

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import okhttp3.Headers;
import okhttp3.Headers.Builder;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * logging and response encoding
 * @author Hinsteny
 * @version $ID: EncodingInterceptor 2018-10-30 14:16 All rights reserved.$
 */
public class EncodingInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(EncodingInterceptor.class);

    /**
     * 自定义编码
     */
    private String encoding;

    public EncodingInterceptor(String encoding) {
        this.encoding = encoding;
    }

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        long start = System.nanoTime();
        logger.info("Sending request: {}, headers: {}, request: {}", request.url(), request.headers(), JSON.toJSONString(request.body()));
        Response response = chain.proceed(request);
        long end = System.nanoTime();
        logger.info(String.format("Received response for %s in %.1fms%n %s", response.request().url(), (end - start) / 1e6d, JSON.toJSONString(response.headers())));
        settingClientCustomEncoding(response);
        return response;
    }

    /**
     * setting client custom encoding when server not return encoding
     * @param response
     * @throws IOException
     */
    private void settingClientCustomEncoding(Response response) throws IOException {
//        setHeaderContentType(response);
        setBodyContentType(response);
    }

    /**
     * set contentType in headers
     * @param response
     * @throws IOException
     */
    private void setHeaderContentType(Response response) throws IOException {
        String contentType = response.header("Content-Type");
        if (StringUtils.isNotBlank(contentType) && contentType.contains("charset")) {
            return;
        }
        // build new headers
        Headers headers = response.headers();
        Builder builder = headers.newBuilder();
        builder.removeAll("Content-Type");
        builder.add("Content-Type", (StringUtils.isNotBlank(contentType) ? contentType + "; ":"" ) + "charset=" + encoding);
        headers = builder.build();
        // setting headers using reflect
        Class  _response = Response.class;
        try {
            Field field = _response.getDeclaredField("headers");
            field.setAccessible(true);
            field.set(response, headers);
        } catch (NoSuchFieldException e) {
            throw new IOException("use reflect to setting header occurred an error", e);
        } catch (IllegalAccessException e) {
            throw new IOException("use reflect to setting header occurred an error", e);
        }
    }

    /**
     * set body contentType
     * @param response
     * @throws IOException
     */
    private void setBodyContentType(Response response) throws IOException {
        ResponseBody body = response.body();
        // setting body contentTypeString using reflect
        Class<? extends ResponseBody> aClass = body.getClass();
        try {
            Field field = aClass.getDeclaredField("contentTypeString");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            String contentTypeString = String.valueOf(field.get(body));
            if (StringUtils.isNotBlank(contentTypeString) && contentTypeString.contains("charset")) {
                return;
            }
            field.set(body, (StringUtils.isNotBlank(contentTypeString) ? contentTypeString + "; ":"" ) + "charset=" + encoding);
        } catch (NoSuchFieldException e) {
            throw new IOException("use reflect to setting header occurred an error", e);
        } catch (IllegalAccessException e) {
            throw new IOException("use reflect to setting header occurred an error", e);
        }
    }
}
