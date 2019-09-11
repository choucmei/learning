package chouc.exmaple.socks5;

import chouc.example.socks5.HttpClientProxyUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author chouc
 * @version V1.0
 * @Title: ProxyTest
 * @Package chouc.exmaple.socks5
 * @Description:
 * @date 8/8/19
 */
public class ProxyTest {

    @Test
    public void testHttpsProxy() throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClientProxyUtils.getHttpClient();
        HttpContext httpContext = HttpClientProxyUtils.getHttpClientContext(true,"master01",10086,"guest","12345");
        HttpPost httpPost = new HttpPost("https://www.baidu.com");
        printResult(closeableHttpClient,httpPost,httpContext);
    }

    @Test
    public void testHttpsProxyError() throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClientProxyUtils.getHttpClient();
        HttpContext httpContext = HttpClientProxyUtils.getHttpClientContext(true,"master01",10086,"guest","1234511");
        HttpPost httpPost = new HttpPost("https://www.baidu.com");
        printResult(closeableHttpClient,httpPost,httpContext);
    }


    @Test
    public void testHttps() throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClientProxyUtils.getHttpClient();
        HttpContext httpContext = HttpClientProxyUtils.getHttpClientContext();
        HttpPost httpPost = new HttpPost("https://www.baidu.com");
        printResult(closeableHttpClient,httpPost,httpContext);
    }

    @Test
    public void testHttpProxy() throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClientProxyUtils.getHttpClient();
        HttpContext httpContext = HttpClientProxyUtils.getHttpClientContext(true,"master01",10086,"guest","12345");
        HttpPost httpPost = new HttpPost("http://master01:18080/login.htm");
        printResult(closeableHttpClient,httpPost,httpContext);
    }

    @Test
    public void testHttpProxyError() throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClientProxyUtils.getHttpClient();
        HttpContext httpContext = HttpClientProxyUtils.getHttpClientContext(true,"master01",10086,"guest1","12345");
        HttpPost httpPost = new HttpPost("http://master01:18080/login.htm");
        printResult(closeableHttpClient,httpPost,httpContext);
    }

    @Test
    public void testHttp() throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClientProxyUtils.getHttpClient();
        HttpContext httpContext = HttpClientProxyUtils.getHttpClientContext();
        HttpPost httpPost = new HttpPost("http://master01:18080/login.htm");
        HttpResponse response = closeableHttpClient.execute(httpPost,httpContext);
        printResult(closeableHttpClient,httpPost,httpContext);
    }

    public void printResult(CloseableHttpClient closeableHttpClient, HttpEntityEnclosingRequestBase http, HttpContext httpClientContext) throws IOException {
        HttpResponse response = closeableHttpClient.execute(http,httpClientContext);
        HttpEntity resEntity = response.getEntity();
        String message = EntityUtils.toString(resEntity, "utf-8");
        System.out.println(message);
    }
}
