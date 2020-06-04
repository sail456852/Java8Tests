package msc.httpclient;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.amazonaws.util.json.Jackson;

/**
 * Created by IntelliJ IDEA.<br/>
 *
 * @author: Eugene_Wang<br />
 * Date: 5/26/2020<br/>
 * Time: 8:19 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtilTest {
    final static Logger logger = LogManager.getLogger(HttpUtilTest.class);

    @org.junit.Test
    public void doGet() {
        String url = "https://api.opencagedata.com/geocode/v1/geojson?q=Best Western Holiday Hills Coalville  US&key=40c5004960f54155bf56be289a792da8";
        url = url.replaceAll("\\s+", "%");
        logger.info(url);
        String s = HttpUtil.doGet(url, 5000);

        JsonNode features = Jackson.jsonNodeOf(s).get("features");
        if (features != null) {
            JsonNode feature = features.get(0);
            JsonNode geometry = feature.get("geometry");
            JsonNode coordinates = geometry.get("coordinates");
            JsonNode longitude = coordinates.get(0);
            logger.info(longitude);
            JsonNode latitude = coordinates.get(1);
            logger.info(latitude);
        }
        logger.info(s);
    }
}