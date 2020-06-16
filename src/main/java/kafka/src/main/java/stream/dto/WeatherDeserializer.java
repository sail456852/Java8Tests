package stream.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.<br/>
 *
 * @author: Eugene_Wang<br />
 * Date: 6/16/2020<br/>
 * Time: 2:01 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class WeatherDeserializer implements Deserializer {

    @Override
    public void configure(Map configs, boolean isKey) {

    }


    @Override
    public Object deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        WeatherDTO weather = null;
        try {
            weather = mapper.readValue(bytes, WeatherDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;
    }

    @Override
    public void close() {

    }
}
