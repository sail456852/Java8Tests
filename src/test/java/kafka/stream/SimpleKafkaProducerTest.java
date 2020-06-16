package kafka.stream;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.<br/>
 *
 * @author: Eugene_Wang<br />
 * Date: 6/16/2020<br/>
 * Time: 12:01 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class SimpleKafkaProducerTest {

    private stream.SimpleKafkaProducer simpleKafkaProducer;

    @Before
    public void setUp() {
        simpleKafkaProducer = new stream.SimpleKafkaProducer();
    }

    @Test
    public void producer() {
        simpleKafkaProducer.simpleProducer();
    }

}