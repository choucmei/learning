package chouc.example.socks5;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;
import java.net.Authenticator;

/**
 * @author chouc
 * @version V1.0
 * @Title: DingdingProxy
 * @Package chouc.example.socks5
 * @Description: through sock5 proxy to send message of dingtalk
 * @date 7/24/19
 */
public class DingdingProxy {
    public static void main(String[] args) throws IOException {

        System.setProperty("socksProxyHost","master01");
        System.setProperty("socksProxyPort","10086");
        Authenticator.setDefault(new HttpProxy.ProxyAuthenticator("guest", "1234511"));

        PostMethod requestMethod = new PostMethod("https://oapi.dingtalk.com/robot/send?access_token=ded881d2fc7082dcdf4a8b3ee0e99f0a6dae349a02be80a09374477a52d91009");
        requestMethod.setRequestHeader("Content-Type","application/json");
        requestMethod.setRequestEntity(new StringRequestEntity("{    \"msgtype\": \"text\",    \"text\": {        \"content\": \"1111\"   },}"));
        HttpClient httpClient = new HttpClient(new SimpleHttpConnectionManager(true));
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
        int resultCode = httpClient.executeMethod(requestMethod);
        String body = requestMethod.getResponseBodyAsString();
        System.out.println(resultCode);
        System.out.println(body);
    }
}
