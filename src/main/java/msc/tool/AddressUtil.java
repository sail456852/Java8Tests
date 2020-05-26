package msc.tool;

import msc.httpclient.utils.NetUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AddressUtil {
	//http://gc.ditu.aliyun.com/regeocoding?l=22.575974,114.101028&type=001 阿里云
	public static String getLLCity(String longitude,String latitude) throws Exception {
		String url = "http://api.map.baidu.com/geocoder?location=" + latitude + "," + longitude + "&coord_type=gcj02&output=xml&ak=O7ALCc3Xbdn2poFmC6N3o7LkDuO25irv";
		return NetUtil.getXml(url, "utf-8").get("GeocoderSearchResponse.result.addressComponent.city");
	}
	public static String getLLProvince(String longitude,String latitude) throws Exception {
		String url = "http://api.map.baidu.com/geocoder?location=" + latitude + "," + longitude + "&coord_type=gcj02&output=xml&ak=O7ALCc3Xbdn2poFmC6N3o7LkDuO25irv";
		return NetUtil.getXml(url, "utf-8").get("GeocoderSearchResponse.result.addressComponent.province");
	}
	public static Map<String, String> getAddress(String longitude,String latitude) throws Exception {
		String url = "http://api.map.baidu.com/geocoder?location=" + latitude + "," + longitude + "&coord_type=gcj02&output=xml&ak=O7ALCc3Xbdn2poFmC6N3o7LkDuO25irv";
		return NetUtil.getXml(url, "utf-8");
	}
	
	public static String getIPCity(String ip) throws JSONException, Exception {
		String url = "http://api.map.baidu.com/location/ip?ak=D657707881345cc4f0de170638463313&ip=" + ip + "&ak=O7ALCc3Xbdn2poFmC6N3o7LkDuO25irv";
		String content = NetUtil.getJson(url).getString("content");
		JSONObject address_detail = new JSONObject(content);
		JSONObject result = new JSONObject(address_detail.getString("address_detail"));
		return result.getString("city");
	}
	public static long calDistance(Double a_x_point, Double a_y_point,  
            Double b_x_point, Double b_y_point) {  
        Double R = new Double(6371);  
        Double dlat = (b_x_point - a_x_point) * Math.PI / 180;  
        Double dlon = (b_y_point - a_y_point) * Math.PI / 180;  
        Double aDouble = Math.sin(dlat / 2) * Math.sin(dlat / 2)  
                + Math.cos(a_x_point * Math.PI / 180)  
                * Math.cos(b_x_point * Math.PI / 180) * Math.sin(dlon / 2)  
                * Math.sin(dlon / 2);  
        Double cDouble = 2 * Math.atan2(Math.sqrt(aDouble), Math  
                .sqrt(1 - aDouble));  
        long d = Math.round((R * cDouble) * 1000) / 1000;  
        return d;  
  
    }
	public static void main(String args[]) throws Exception {
		Map<String,String> map = getAddress("114.101028","22.575974");
		System.out.println(map.get("GeocoderSearchResponse.result.addressComponent.province"));
		System.out.println(map.get("GeocoderSearchResponse.result.addressComponent.city"));
	}
}
