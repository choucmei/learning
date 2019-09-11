package chouc.example.socks5;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * @author chouc
 * @version V1.0
 * @Title: HttpClientProxyUtils
 * @Package com.broadtech.common.util.proxy
 * @Description:
 * @date 8/8/19
 */
public class HttpClientProxyUtils {
    private static final String SOCK_PROXY_KEY = "proxy.socks";

    private static final Logger logger = Logger.getLogger(HttpClientProxyUtils.class);

    public static CloseableHttpClient getHttpClient() {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new CustomHttpConnectionSocketFactory())
                .register("https", new CustomHttpsConnectionSocketFactory(SSLContexts.createSystemDefault())).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
        return httpclient;
    }

    public static HttpClientContext getHttpClientContext() {
        return getHttpClientContext(false, null, 0, null, null);
    }


    public static HttpClientContext getHttpClientContext(Boolean isProxy, String proxyHost, int proxyPort, String proxyUser, String proxyPasswd) {
        HttpClientContext context = HttpClientContext.create();
        if (isProxy) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(proxyHost, proxyPort);
            java.net.Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPasswd));
            context.setAttribute(SOCK_PROXY_KEY, inetSocketAddress);
        }
        return context;
    }


    private static class CustomHttpsConnectionSocketFactory extends SSLConnectionSocketFactory {
        public CustomHttpsConnectionSocketFactory(SSLContext sslContext) {
            super(sslContext);
        }

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            if (context.getAttribute(SOCK_PROXY_KEY) != null) {
                InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute(SOCK_PROXY_KEY);
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
                return new Socket(proxy);
            }
            return new Socket();
        }
    }


    private static class CustomHttpConnectionSocketFactory extends PlainConnectionSocketFactory {
        public Socket createSocket(final HttpContext context) throws IOException {
            if (context.getAttribute(SOCK_PROXY_KEY) != null) {
                InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute(SOCK_PROXY_KEY);
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
                return new Socket(proxy);
            }
            return new Socket();
        }
    }


}
