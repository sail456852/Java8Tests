package msc.httpclient.douban;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.<br/>
 *
 * @author: eugene<br />
 * Date: 10/18/2020<br/>
 * Time: 9:19 AM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class DoubanRentalScraper {
    private static final Logger logger = LoggerFactory.getLogger(DoubanRentalScraper.class);

    public static void main(String[] args) throws IOException {
        String url = "https://www.douban.com/group/nanshanzufang/";
        Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).execute();
        Elements titles = response.parse().getElementsByClass("title");
        List<String> hrefs = titles.stream().map(v -> v.getElementsByTag("a").get(0)).map(v -> v.attr("href")).collect(Collectors.toList());
        hrefs.forEach(href -> logger.info("href={}", href));
    }
}
