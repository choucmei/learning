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
#
# SECTION       <VARIABLES AND FLAGS>
# \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
#
#  TAG: set
#
#       set option name:
#
#       SS5_DNSORDER            ->   order dns answer
#       SS5_VERBOSE               ->   enable verbose output to be written into logfile
#       SS5_DEBUG                 ->   enable debug output to be written into logfile
#       SS5_CONSOLE                ->   enable web console
#       SS5_ATIMEOUT               ->   for future uses
#       SS5_STIMEOUT               ->   set session idle timeout (default 1800 seconds,
#                                                         0 for infinite)
#       SS5_LDAP_TIMEOUT           ->   set ldap query timeout
#       SS5_LDAP_BASE              ->   set BASE method for profiling (see PROFILING section)
#                                             It is default option!
#       SS5_LDAP_FILTER           ->   set FILTER method for profiling (see PROFILING
#                                            section)
#       SS5_SRV                   ->   enable ss5srv admin tool
#       SS5_PAM_AUTH               ->   set PAM authentication
#       SS5_RADIUS_AUTH            ->   set RADIUS authentication
#       SS5_RADIUS_INTERIM_INT           ->   set interval beetwen interim update packet
#       SS5_RADIUS_INTERIM_TIMEOUT       ->   set interim response timeout
#       SS5_AUTHCACHEAGE           ->   set age in seconds for authentication cache
#       SS5_AUTHOCACHEAGE          ->   set age in seconds for authorization cache
#       SS5_STICKYAGE              ->   set age for affinity
#       SS5_STICKYSESSION          ->   enable affinity session
#       SS5_SUPAKEY                ->   set SUPA secret key (default SS5_SERVER_S_KEY)
#       SS5_ICACHESERVER           ->   set internet address of ICP server
#       SS5_GSS_PRINC              ->   set GSS service principal
#       SS5_PROCESSLIFE            ->   set number of requests process must servs before
#                                             closing
#       SS5_NETBIOS_DOMAIN         ->   enable netbios domain mapping with directory store,
#                                             during autorization process
#       SS5_SYSLOG_FACILITY        ->   set syslog facility
#       SS5_SYSLOG_LEVEL        ->   set syslog level
#
# ///////////////////////////////////////////////////////////////////////////////////
 
#
# SECTION     <AUTHENTICATION>
# \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
#
#  TAG: auth
#
#     auth source host, source port, authentication type
#
#     Some examples:
#
#     Authentication from 10.253.8.0 network
#           auth 10.253.8.0/22 - u
#
#     Fake authentication from 10.253.0.0 network. In this case, ss5 request
#    authentication but doesn't check for password. Use fake authentication
#    for logging or profiling purpose.
#           auth 10.253.0.0/16 - n
#
#     Fake authentication: ss5 doesn't check for correct password but fetchs
#    username for profiling.
#           auth 0.0.0.0/0 - n
#
#  TAG: external_auth_program
#
#     external_auth_program program name and path
#
#     Some examples:
#
#     Use shell file to autheticate user via ldap query
#           external_auth_program /usr/local/bin/ldap.sh
#
#  TAG: RADIUS authentication could be used setting SS5_RADIUS_AUTH option and
#       configuring the following attributes:
#
#       radius_ip               (radius address)
#       radius_bck_ip           (radius secondary address)
#       radius_auth_port        (radius authentication port, DFAULT = 1812)
#       radius_acct_port        (radius authorization  port, DFAULT = 1813)
#       radius_secret           (secret password betw
#
#
#
# ///////////////////////////////////////////////////////////////////////////////////
#       SHost           SPort           Authentication
#
#auth    0.0.0.0/0               -               -
 
 
#
# SECTION     <BANDWIDTH>
# \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
#
#  TAG: bandwidth
#
#     bandwidth group, max number of connections, bandwidth, session timeout
#
#     Some examples:
#
#     Limit connections to 2 for group Admin
#           bandwidth Admin 2 - -
#
#     Limit bandwidth to 100k for group Users
#           bandwidth Users - 102400 -
#
#       note: if you enable bandwith profiling per user, SS5 use this value instead of
#             value specified into permit directive.
#
# ///////////////////////////////////////////////////////////////////////////////////
#                   Group          MaxCons     Bandwidth   Session timeout
#       bandwidth   grp1           5           -           -
 
#
# SECTION    <PROXIES>
# \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
#
#  TAG: proxy/noproxy
#
#    proxy/noproxy dst host/network, dst port, socks proxy address, port address, ver
#
#    Some examples:
#
#    Proxy request for 172.0.0.0 network to socks server 10.253.9.240 on port 1081:
#
#       if authentication is request, downstream socks server have to  check it;
#       if resolution is request, downstream socks server does it before proxying
#    the request toward the upstream socks server.
#           proxy 172.0.0.0/16 - 10.253.9.240 1081
#
#       SS5 makes direct connection to 10.253.0.0 network (in this case, port value is not
#       verified) without using upstream proxy server
#           noproxy 0.0.0.0/0 - 10.253.0.0/16 1080 -
#
# ///////////////////////////////////////////////////////////////////////////////////
#           DHost/Net        DPort    DProxyip    DProxyPort SocksVer
#
#    proxy    0.0.0.0/0        -    1.1.1.1        -       -
 
#
# SECTION       <DUMP>
# \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
#
#  TAG: dump
#
#       dump host/network, port, s/d (s=source d=destination), dump mode (r=rx, t=tx, b=rx+tx)
#
#       Some examples:
#
#       Dump traffic for 172.30.1.0 network on port 1521:
#
#       if authentication is request, downstream socks server have to  check it;
#       if resolution is request, downstream socks server does it before proxying
#       the request toward the upstream socks server.
#               dump 172.30.1.0/24 1521 d b
#
# ///////////////////////////////////////////////////////////////////////////////////
#              DHost/Net               DPort   Dir     Dump mode (r=rx,t=tx,b=rx+tx)
#
#       dump   0.0.0.0/0               -       d    t
 
#
# SECTION    <ACCESS CONTROL>
# \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
#
#  TAG: permit/deny
#    permit/deny src auth flag, host/network, src port, dst host/network, dst port,
#    fixup, group, bandwidth (from 256 bytes per second to 2147483647), expdate
#
#    Some examples:
#
#     FTP Control + Passive Mode
#        permit - 0.0.0.0/0 - 172.0.0.0/8 21 - - - -
#
#    FTP DATA Active Mode
#        permit - 0.0.0.0/0     - 172.0.0.0/8     21     - - - -
#        permit - 172.0.0.0/8     - 0.0.0.0/0     -     - - - -
#
#    Query DNS
#        permit - 0.0.0.0/0 - 172.30.0.1/32 53 - - - -
#
#    Http + fixup
#        permit - 0.0.0.0/0 - www.example.com 80 http - - -
#
#    Http + fixup + profile + bandwidth (bytes x second)
#        permit - 0.0.0.0/0 - www.example.com 80 http admin 10240 -
#
#    Sftp + profile + bandwidth (bytes x second)
#        permit - 0.0.0.0/0 - sftp.example.com 22 - developer 102400 -
#
#    Http + fixup
#        permit - 0.0.0.0/0 - web.example.com 80 - - - -
#
#    Http + fixup + user autentication required with expiration date to 31/12/2006
#        permit u 0.0.0.0/0 - web.example.com 80 - - - 31-12-2006
#
#    Deny all connection to web.example.com
#        deny - 0.0.0.0/0 - web.example.com - - - - -
#
#
# /////////////////////////////////////////////////////////////////////////////////////////////////
#      Auth    SHost        SPort    DHost        DPort    Fixup    Group    Band    ExpDate
#
#permit -    0.0.0.0/0    -    0.0.0.0/0    -    -    -    -
 
 
 
#
# SECTION    <PROFILING>
# \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
#
#    1) File profiling:
#
#    ss5 look for a file name specified in permit line in the /etc/ss5 directory.
#    This file must contain user members. File profiling is the default option.
#
#    2) Ldap profiling:
#
#    ldap_profile_ip         (directory internet address)
#    ldap_profile_port       (directory port)
#    ldap_profile_base       (ss5 replaces % with "group specified in permit line"
#                if SS5LDAP_BASE if specified, otherwise if
#                SS5LDAP_FILTER is specified,  it uses base and search
#                for group as attribute in user entry; see examples)
#    ldap_profile_filter     (ss5 uses filter for search operation)
#    ldap_profile_dn         (directory manager or another user authorized to
#                query the directory)
#    ldap_profile_pass       ("dn" password)
#    ldap_netbios_domain    (If SS5_NETBIOS_DOMAIN option is set, ss5 map netbios
#                                domain user in authentication request with his configured
#                                directory sever. Otherwise no match is done and
#                                directory are contacted in order of configuration)
#
#    3) Mysql profiling:
#
#    mysql_profile_ip         (mysql server internet address)
#    mysql_profile_db       (mysql db )
#    mysql_profile_user     (mysql username )
#    mysql_profile_pass     (mysql password )
#    mysql_profile_sqlstring    (sql base string for query. DEFAULT 'SELECT uname FROM grp WHERE gname like' )
#
#    Some examples:
#
#    Directory configuration for ldap profiling with SS5_LDAP_BASE option:
#    in this case, ss5 look for attribute uid="username" with base ou="group",
#    dc=example,dc=com where group is specified in permit line as
#    "permit - - - - - group - -
#
#    Note: in this case, attribute value is not userd
#
#        ldap_profile_ip        10.10.10.1
#        ldap_profile_port      389
#        ldap_profile_base      ou=%,dc=example,dc=com
#        ldap_profile_filter    uid
#        ldap_profile_attribute gid
#        ldap_profile_dn        cn=root,dc=example,dc=com
#        ldap_profile_pass      secret
#        ldap_netbios_domain    dir
#
#    Directory configuration for ldap profiling with SS5_LDAP_FILTER option:
#    in this case, ss5 look for attributes uid="username" & "gid=group" with
#    base dc=example,dc=com where group is specified in permit line as
#    "permit - - - - - group - -
#
#    Note: you can also use a base like "ou=%,dc=example,dc=com", where %
#    will be replace with "group".
#
#        ldap_profile_ip        10.10.10.1
#        ldap_profile_port      389
#        ldap_profile_base      ou=Users,dc=example,dc=com
#        ldap_profile_filter    uid
#        ldap_profile_attribute gecos
#        ldap_profile_dn        cn=root,dc=example,dc=com
#        ldap_profile_pass      secret
#        ldap_domain_domain     dir
#
#    Sample OpenLdap log:
#    conn=304 op=0 BIND dn="cn=root,dc=example,dc=com" mech=simple ssf=0
#    conn=304 op=0 RESULT tag=97 err=0 text=
#    conn=304 op=1 SRCH base="ou=Users,dc=example,dc=com" scope=1 filter="(&(uid=usr1)(gecos=Users))"
#    conn=304 op=1 SRCH attr=gecos
#
#     where ldap entry is:
#    dn: uid=usr1,ou=Users,dc=example,dc=com
#    uid: usr1
#    cn: usr1
#    objectClass: account
#    objectClass: posixAccount
#    objectClass: top
#    userPassword:: dXNyMQ==
#    loginShell: /bin/bash
#    homeDirectory: /home/usr1
#    uidNumber: 1
#    gidNumber: 1
#    gecos: Users
 
#
# SECTION    <SERVER BALANCE>
# \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
#
#  TAG: virtual
#
#    virtual virtual identification (vid), real ip server
#
#    Some examples:
#
#    Two vip balancing on three real server each one
#        virtual 1 172.30.1.1
#        virtual 1 172.30.1.2
#        virtual 1 172.30.1.3
#
#        virtual 2 172.30.1.6
#        virtual 2 172.30.1.7
#        virtual 2 172.30.1.8
#
#     Note: Server balancing only works with -t option, (threaded mode) and ONLY
#    with "connect" operation.
#
# ///////////////////////////////////////////////////////////////////////////////////
#          Vid    Real ip
#
#vitual    -    -
 
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