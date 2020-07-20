package immutability;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: Eugene_Wang<br/>
 * Date: 7/16/2020<br/>
 * Time: 11:32 AM<br/>
 * To change this template use File | Settings | File Templates.
 * 2020-07-16 11:50:51,925 INFO [immutability.ThreeStoogesTest] - mainThreadMoeLarryCurly
 *
 * This proves Immutable Reference to a mutable Object can also be modified
 * with single threaded program.
 */
public class ThreeStoogesTest {
    final static Logger log = LoggerFactory.getLogger(ThreeStoogesTest.class.getName());

    @Test
    public void isStooge() {
        ThreeStooges threeStooges = new ThreeStooges();
        log.info("",threeStooges.modifyStooges("mainThread"));
        Thread thread = new Thread(() -> {
            log.info("",threeStooges.modifyStooges("thread1"));
        });
        thread.start();
        String s = threeStooges.toString();
        log.info(s);
    }
}