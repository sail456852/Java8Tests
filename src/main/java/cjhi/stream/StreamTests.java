package cjhi.stream;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: yz<br/>
 * Date: 6/5/2019<br/>
 * Time: 2:46 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class StreamTests {
    static String contents;
    static {
        try {
            contents = new String(
                    Files.readAllBytes(Paths.get("alice.txt")), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StreamTests() throws IOException {

    }

    public static void main(String[] args) throws IOException {
        List<String> words = Arrays.asList(contents.split("\\PL+"));
        System.err.println("words.size() = " + words.size());
        long count = words.stream().filter(w -> w.length() > 10).count();
        System.err.println("count = " + count);
        words.stream().filter(w -> w.length() > 10).forEach(s -> System.err.println("s = " + s));
    }

    @Test
    public void generateNumberSequenceStream() throws IOException {
        Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE)).limit(10);
        integers.forEach(s -> System.err.println("s = " + s));
    }

    @Test
    public void combinedStream() {
        Stream<String> concat = Stream.concat(letters("Hello"), letters("World"));
        show("hello", concat);
    }

    public static Stream<String> letters(String s) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < s.length(); i++) {
            list.add(s.substring(i, i + 1));
        }
        return list.stream();
    }

    public <T> void show(String title, Stream<T> stream) {
        System.err.println("title = [" + title);
        stream.limit(10).forEach(s -> System.err.println("s = " + s));
    }

    @Test
    public void testOptional() {
        String[] split = contents.split("\\PL+");
        List<String> words = Arrays.asList(split);
        Optional<String> startWithQ = words.stream().filter(s -> s.startsWith("Q")).findAny();
        Optional<String> qAny = words.stream().parallel().filter(s -> s.startsWith("Q")).findAny();
//        String result = startWithQ.orElse("");
//        System.err.println("result = " + result);
//        String s = qAny.orElseGet(() -> Locale.getDefault().getDisplayName());
        qAny.ifPresent(v -> System.err.println("v = " + v));
    }
}
