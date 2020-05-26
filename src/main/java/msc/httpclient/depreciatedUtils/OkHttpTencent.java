package msc.httpclient.depreciatedUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Okita.<br/>
 * User: yz<br/>
 * Date: 11/25/18<br/>
 * Time: 7:19 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class OkHttpTencent {

    private static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws IOException {
        Request request = new Request.Builder()
                .url("https://wss.cloud.tencent.com/ssl/api/common/dv_available?g_tk=786224694&t=1543143221283&_format=json&mc_gtk=786224694&domain=ctor.cn")
                .get()
                .addHeader("Origin", "https://buy.cloud.tencent.com")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7")
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
                .addHeader("Accept", "*/*")
                .addHeader("Referer", "https://buy.cloud.tencent.com/domain?from=console")
                .addHeader("Cookie", "_ga=GA1.2.1326717604.1543114469; pgv_pvi=9482649600; pgv_si=s7671099392; qcloud_uid=b7bdac80fe5463309792d176bc8b476f; qcloud_visitId=24ba8b6a410ab6c0cadaf4ebb8be95ac; qcact.sid=s%3ALGiseAhp-BJkHkKueUQ5WV3fzLugmiik.SC2QUIwny50z1qQiJPRBO21WysAFJmOI6PuHyIM%2FV64; _gcl_au=1.1.97349341.1543133838; language=zh; opc_xsrf=396667693565c32c09f6187986dd2576%7C1543133864; wss_xsrf=281527b972f4183797f9cbe824903248%7C1543133864; lastLoginType=wx; wx_tinyid=144115209770360661; wx_akey=wRt6f*bDOZTBc8*X3s9imedGRT6V8PVLkVT0JU0ADZY_; wx_avatar=https%3A%2F%2Fthirdwx.qlogo.cn%2Fmmopen%2Fvi_32%2FXiafbLibZJBgD3CRXVUoeWh5pibTq9h93a89eiaD98zV3ia0jcUmb69dicf55kn8KUacsNSnKooIAjcibUwHsBD7A46icw%2F132; wx_nickname=%E6%B2%96%E7%94%B0%E5%90%9B; wx_uin=100003057704; wx_key=r2KSZn%2FTstMMnqRlRpNaaHDXyTKouUlcs5oeu5q2TsToKpT11ZhswPmqlp29ovnwg5TxzXCAfOYK59tMk7lt2K8t%2BYvskTtQghsPjl0JStrWZWzCO5WeSNPP0oNyQfAqof%2BQHBGxThK1y792WW0vre%2BQHH1xQSIJUT2%2BeSCRPEA%3D; loginType=wx; moduleId=1255518299; systemTimeGap=-112; from=console; isQcloudUser=true; qcloud_from=gwzcw.1095857.1095857.1095857-1543135108871; ci_session=d56de4fb31720bd53906c184c5a4a0bce698a6f0; intl=")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "6b96377b-ccfe-4cd3-b743-320d61b02237")
                .build();

        Response response = client.newCall(request).execute();
        System.err.println("response = " + response); // print response summary
        System.err.println("response = " + response.body().string()); // print out the response body
        System.err.println("response.body() = " + response.body()); // nothing useful
        System.err.println("response = " + response.message()); // contained in the first response

    }
}
