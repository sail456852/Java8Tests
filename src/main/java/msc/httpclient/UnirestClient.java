package msc.httpclient;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.*;

/**
 * Created by Okita.<br/>
 * User: yz<br/>
 * Date: 11/25/18<br/>
 * Time: 7:29 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class UnirestClient {
    private static AtomicInteger count;
    private static char[] chararr;

    final static Logger logger = LogManager.getLogger(UnirestClient.class);
    static {
        chararr = "abcdefghijklmnopqrstovwxyz".toCharArray();
        count = new AtomicInteger(0);
    }

    public static void main(String[] args) {
        ArrayList<String> validList = new ArrayList<String>();
        ExecutorService executorService = newCachedThreadPool();
        int range = 3;
        UnirestClient uc = new UnirestClient();
        for (int i = 0; i < 23; i += 3) {
            int finalI = i;
            System.err.println("finalI = " + finalI);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        uc.rangeCheck(validList, finalI, range);
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
//        uc.rangeCheck(validList, 23, 2); // 24th 25th == y z
        for (String s : validList) {
            System.err.println("s = " + s);
        }
    }

    @Test
    public void checkUrl() {
        UnirestClient uc = new UnirestClient();
        String consts = "sai";
        for (char c2 = 'a'; c2 <= 'z'; c2++) {
            try {
                boolean b = uc.checkDomain(consts + c2);
                if (b) {
                    System.err.println("UnirestClient.checkUrl found!!! --- " + consts + c2);
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
    }

    private void rangeCheck(ArrayList<String> validList, int i, int n) throws UnirestException {
        System.err.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        for (char c = chararr[i]; c < chararr[i + n]; c++) {
            String varUrl = "";
            varUrl += c;
            for (char c2 = c; c2 <= 'z'; c2++) {
                varUrl = "" + c;
                varUrl += c2;
                if (checkDomain(varUrl)) {
                    validList.add(varUrl);
                }
            }
        }
    }

    private boolean checkDomain(String varUrl) throws UnirestException {
        System.err.println(Thread.currentThread().getName() + " :   checking  = " + varUrl + "  count=  " + count.addAndGet(1));
        HttpResponse<String> response = Unirest.get("https://wss.cloud.tencent.com/buy/api/domains/domain/check?g_tk=786224694&t=1543146071332&_format=json&mc_gtk=786224694&domain_name=" + varUrl + "&tlds=.cn&_xsrf=281527b972f4183797f9cbe824903248%7C1543133864")
                .header("Origin", "https://buy.cloud.tencent.com")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7")
                .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
                .header("Accept", "*/*")
                .header("Referer", "https://buy.cloud.tencent.com/domain?from=console")
                .header("Cookie", "_ga=GA1.2.1326717604.1543114469; pgv_pvi=9482649600; pgv_si=s7671099392; qcloud_uid=b7bdac80fe5463309792d176bc8b476f; qcloud_visitId=24ba8b6a410ab6c0cadaf4ebb8be95ac; qcact.sid=s%3ALGiseAhp-BJkHkKueUQ5WV3fzLugmiik.SC2QUIwny50z1qQiJPRBO21WysAFJmOI6PuHyIM%2FV64; _gcl_au=1.1.97349341.1543133838; language=zh; opc_xsrf=396667693565c32c09f6187986dd2576%7C1543133864; wss_xsrf=281527b972f4183797f9cbe824903248%7C1543133864; lastLoginType=wx; wx_tinyid=144115209770360661; wx_akey=wRt6f*bDOZTBc8*X3s9imedGRT6V8PVLkVT0JU0ADZY_; wx_avatar=https%3A%2F%2Fthirdwx.qlogo.cn%2Fmmopen%2Fvi_32%2FXiafbLibZJBgD3CRXVUoeWh5pibTq9h93a89eiaD98zV3ia0jcUmb69dicf55kn8KUacsNSnKooIAjcibUwHsBD7A46icw%2F132; wx_nickname=%E6%B2%96%E7%94%B0%E5%90%9B; wx_uin=100003057704; wx_key=r2KSZn%2FTstMMnqRlRpNaaHDXyTKouUlcs5oeu5q2TsToKpT11ZhswPmqlp29ovnwg5TxzXCAfOYK59tMk7lt2K8t%2BYvskTtQghsPjl0JStrWZWzCO5WeSNPP0oNyQfAqof%2BQHBGxThK1y792WW0vre%2BQHH1xQSIJUT2%2BeSCRPEA%3D; loginType=wx; moduleId=1255518299; systemTimeGap=-112; from=console; isQcloudUser=true; qcloud_from=gwzcw.1095857.1095857.1095857-1543135108871; intl=; C_AID=15ec174cfc19df2251e722b6a8f60d95; ci_session=ca5f4456b78dab5447b7dee131dd906ba2c9bef0")
                .header("Connection", "keep-alive")
                .header("cache-control", "no-cache")
                .header("Postman-Token", "72e29cb9-890f-4478-921f-a0bb53ddf02f")
                .asString();
        String body = response.getBody();
        if (body.contains(":false")) {
            System.err.println("UnirestClient.main found one! ---  " + varUrl);
            return true;
        } else {
            return false;
        }
    }

    @Test
    public void simpleHttpClient() throws UnirestException {
        String url = "https://api.opencagedata.com/geocode/v1/geojson?q=Best Western Holiday Hills Coalville  US&key=40c5004960f54155bf56be289a792da8";
        url = url.replaceAll("\\s+", "%");
        logger.info(url);
        String s = HttpUtil.doGet(url, 5000);
        logger.info(s);
    }
}
