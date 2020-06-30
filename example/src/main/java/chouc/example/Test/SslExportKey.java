package chouc.example.Test;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Base64;

public class SslExportKey {

    public static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
        FileInputStream is = new FileInputStream(keyStorePath);
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

    public static PrivateKey getPrivateKey(String path) {
        try {
            String password = "chouc....";
            KeyStore ks = getKeyStore(path, password);
            PrivateKey key = (PrivateKey) ks.getKey("serverKey", password.toCharArray());
            String encoded = Base64.getEncoder().encodeToString(key.getEncoded());//单行
            System.out.println("-----BEGIN RSA PRIVATE KEY-----");
            System.out.println(encoded);
            System.out.println("-----END RSA PRIVATE KEY-----");
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
