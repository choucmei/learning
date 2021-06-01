# 下载代理服务
# 安装
## 解压

 安装前，先安装依赖。 socks5 依赖有：gcc  pam-devel openldap-devel cyrus-sasl-devel openssl-devel 
 

## 安装：

tar xvf ss5-3.8.9-8.tar.gz
cd ss5-3.8.9
./configure && make && make install
# 配置
有两个配置： /etc/opt/ss5/ss5.conf 和 /etc/opt/ss5/ss5.passwd

## /etc/opt/ss5/ss5.conf 
```

#                         例如         具体阅读以上说明
auth    0.0.0.0/0               -               u
permit u    192.168.90.44/0    -    0.0.0.0/0    -    -    -    -    -
```

## /etc/opt/ss5/ss5.passwd
```
guest 12345
```

# 启动
当前socks5所在的机器Ip：指定一个端口
/usr/sbin/ss5 -u root -b 192.168.95.235:10086
 

# 测试
在另一台没有网的机器

如果配置没有限制用户和密码
` curl --sock5 192.168.95.235:10086 www.baidu.com
` 
如果配置有限制用户和密码
`curl --proxy-user guest:12345 --socks5 192.168.95.235:10086 www.baidu.com
`
或
`curl -U guest:12345 --socks5 192.168.95.235:10086 www.baidu.com
` 
如果返回正常则成功、反之查看端口监听和日志找原因。
 

# JAVA实际场景测试

```

// 大部分在代码中添加一下代码即可 
    {
        System.setProperty("proxySet", "true");
        System.setProperty("socksProxyHost","master01");
        System.setProperty("socksProxyPort","10086");
        Authenticator.setDefault(new ProxyAuthenticator("guest", "12345"));
    }
 
 
 
// 例如：HTTP
 
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
        /**代理设置**/
        System.setProperty("proxySet", "true");
        System.setProperty("socksProxyHost","master01");
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
 
 
 
// 例如：DINGDING发送告警
 
 
 
public class DingdingProxy {
    public static void main(String[] args) throws IOException {
          
        /**代理设置**/
        System.setProperty("socksProxyHost","master01");
        System.setProperty("socksProxyPort","10086");
        Authenticator.setDefault(new HttpProxy.ProxyAuthenticator("guest", "1234511"));
 
        PostMethod requestMethod = new PostMethod("https://oapi.dingtalk.com/robot/send?access_token=....");
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
 
 
 
 
 
 
 
 
 
// socks 用户和密码类
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
```