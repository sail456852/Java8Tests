package map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: Eugene_Wang<br/>
 * Date: 7/14/2020<br/>
 * Time: 2:49 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class ConcurrentMapTest {
    final static Logger log = LoggerFactory.getLogger(ConcurrentMapTest.class.getName());

    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());


    @Test
    public void givenHashMap_whenSumParallel_thenError() throws Exception {
        Map<String, Integer> map = new HashMap<>();
        List<Integer> sumList = parallelSum100(map, 100);
        long wrongResultCount = sumList
                .stream()
                .filter(num -> num != 100)
                .count();
        for (Integer integer : sumList) {
            log.info("INT: {} " , integer);
        }
        log.info(String.valueOf(wrongResultCount));
        assertTrue(wrongResultCount > 0);
        assertNotEquals(1, sumList
                .stream()
                .distinct()
                .count());
    }

    private List<Integer> parallelSum100(Map<String, Integer> map,
                                         int executionTimes) throws InterruptedException {
        List<Integer> sumList = new LinkedList<Integer>();
        for (int i = 0; i < executionTimes; i++) {
            map.put("test", 0);
            for (int j = 0; j < 10; j++) {
                threadPool.execute(() -> {
                    for (int k = 0; k < 10; k++) {
                        map.computeIfPresent(
                                "test",
                                (key, value) -> value + 1
                        );
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
            sumList.add(map.get("test"));
        }
        return sumList;
    }

    @Test
    public void givenConcurrentMap_whenSumParallel_thenCorrect()
            throws Exception {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        List<Integer> sumList = parallelSum100(map, 1000);

        assertEquals(1, sumList.stream().distinct().count());
        long wrongResultCount = sumList.stream().filter(num -> num != 100).count();
        assertEquals(0, wrongResultCount);
    }
}
