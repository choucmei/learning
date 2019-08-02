package chouc.example.socks5;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * @author chouc
 * @version V1.0
 * @Title: HttpProxy
 * @Package chouc.example.socks5
 * @Description: through sock5 proxy to send request of http
 * @date 7/24/19
 */
public class HttpProxy {

    static class ProxyAuthenticator extends Authenticator {
        private String user = "";
        private String password = "";

        public ProxyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("socksProxyHost","master011");
        System.setProperty("socksProxyPort","10086");
        Authenticator.setDefault(new ProxyAuthenticator("guest", "12345"));
        GetMethod requestMethod = new GetMethod("http://www.baidu.com");
        HttpClient httpClient = new HttpClient(new SimpleHttpConnectionManager(true));
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000);
        int resultCode = httpClient.executeMethod(requestMethod);
        String body = requestMethod.getResponseBodyAsString();
        System.out.println(resultCode);
        System.out.println(body);
    }
}
