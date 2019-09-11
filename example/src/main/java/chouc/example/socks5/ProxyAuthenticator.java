package chouc.example.socks5;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * @author chouc
 * @version V1.0
 * @Title: ProxyAuthenticator
 * @Package com.broadtech.common.util
 * @Description:
 * @date 7/24/19
 */
public class ProxyAuthenticator extends Authenticator {
    private String user = "";
    private String password = "";

    public ProxyAuthenticator(String user, String password) {
        if (user != null){
            this.user = user;
        }
        if (password != null){
            this.password = password;
        }
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password.toCharArray());
    }
}
