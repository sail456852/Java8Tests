package stream;

import com.amazonaws.util.DateUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import stream.dto.WeatherDTO;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.<br/>
 *
 * @author: Eugene_Wang<br />
 * Date: 6/16/2020<br/>
 * Time: 11:49 AM<br/>
 * To change this template use File | Settings | File Templates.
 *
 */
public class SimpleKafkaProducer {
    static final Logger logger = LogManager.getLogger(SimpleKafkaProducer.class);

    public static void main(String[] args) {
        SimpleKafkaProducer simpleKafkaProducer = new SimpleKafkaProducer();
        simpleKafkaProducer.simpleProducer();
    }

    public void simpleProducer() {
        //Assign topicName to string variable
        String topicName = "testTopic";

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:6667");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

//        props.put("value.serializer", "kafka.stream.dto.WeatherSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        Random rnd = new Random();
        for (long nEvents = 0; nEvents < 10; nEvents++) {
            String formatRFC822Date = DateUtils.formatRFC822Date(new Date());
            String ip = "192.168.2." + rnd.nextInt(255);
            String msg = formatRFC822Date + ",www.example.com," + ip;
            logger.info(msg);
            ProducerRecord<String, String> data = new ProducerRecord<String, String>(topicName, ip, msg);
            producer.send(data);
        }
        producer.close();

//        weatherProducer(topicName, props);
    }

    private void weatherProducer(String topicName, Properties props) {
        Producer<String, WeatherDTO> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 10; i++) {
            WeatherDTO weatherDTO = new WeatherDTO();
            weatherDTO.setLng(Double.valueOf(i));
            weatherDTO.setLat(Double.valueOf(i));
            weatherDTO.setAvg_tmpr_f(Double.valueOf(i));
            weatherDTO.setAvg_tmpr_c(Double.valueOf(i));
            weatherDTO.setWthr_date(String.valueOf(i));
            weatherDTO.setYear(i);
            weatherDTO.setMonth(i);
            weatherDTO.setDay(i);
            producer.send(new ProducerRecord<String, WeatherDTO>(topicName, Integer.toString(i), weatherDTO));
            logger.info(i);
        }
        producer.close();
    }

}
