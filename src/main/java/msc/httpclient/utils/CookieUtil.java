package msc.httpclient.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.google.common.base.Splitter;

public class CookieUtil {
	/**
	 * 设置cookie
	 * 
	 * @param response
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */
	public static void addCookie(HttpServletResponse response, String name,
			String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	/**
	 * 根据名字获取cookie
	 * 
	 * @param request
	 * @param name
	 *            cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = cookieMap.get(name);
			return cookie;
		} else {
			return null;
		}
	}

	/**
	 * 将cookie封装到request里面
	 * 
	 * @param request
	 * @throws UnsupportedEncodingException
	 */
	public static void addRequestFromCookie(HttpServletRequest request)
			throws UnsupportedEncodingException {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("merchantName")) {
				request.setAttribute(cookie.getName(),
						java.net.URLDecoder.decode(cookie.getValue(), "UTF-8"));
			} else {
				request.setAttribute(cookie.getName(), cookie.getValue());
			}
		}
	}

	/**
	 * 将map封装到cookie里面
	 * 
	 * @param response
     * @param map
     * @param maxAge
	 */
	public static void addCookieMap(HttpServletResponse response,
			Map<String, String> map, int maxAge) {
		Set<Entry<String, String>> set = map.entrySet();
		Iterator<Entry<String, String>> i = set.iterator();
		while (i.hasNext()) {
			Entry<String, String> me = i.next();
			addCookie(response, me.getKey().toString(), me.getValue()
					.toString(), maxAge);
		}
	}

	/**
	 * 将cookie封装到Map里面
	 * 
	 * @param request
	 * @return
	 */
	private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}


    /**
     * cookie string to map
     * @param cookieStr
     * @return
     */
	public static Map<String, String> cookieStrToMap(String cookieStr){
        Splitter.MapSplitter mapSplitter = Splitter.onPattern(";").withKeyValueSeparator("=");
        Map<String, String> split = mapSplitter.split(cookieStr);
        return split;
    }



}
