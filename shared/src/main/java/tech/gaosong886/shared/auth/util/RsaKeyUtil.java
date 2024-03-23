package tech.gaosong886.shared.auth.util;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import lombok.SneakyThrows;

/**
 * RSA 秘钥工具类
 */
public class RsaKeyUtil {

    /**
     * 读取公钥
     * @param rawStr 完整的从文件中读取的秘钥字符串
     * @return PublicKey 公钥
     */
    @SneakyThrows
    public static PublicKey getPublicKey(String rawStr) {
        String pem = rawStr
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decodeBase64(pem));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    /**
     * 读取私钥
     * @param rawStr 完整的从文件中读取的秘钥字符串
     * @return PrivateKey 私钥
     */
    @SneakyThrows
    public static PrivateKey getPrivateKey(String rawStr) {
        String base64PrivateKeyStr = rawStr
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END RSA PRIVATE KEY-----", "");

        /*
         * 
         * 我通过以下命令生成的私钥文件的格式是 PKCS1.
         * 
         * `openssl genrsa -out private.key 2048`
         * 
         * PKCS1 格式的 key 需要使用 BouncyCastle 来读取它，而不是使用原生的 Java 类 PKCS8EncodedKeySpec.
         * 也可以使用以下命令将 PKCS1 格式的 private.key 文件转换为 PKCS8 格式.
         * 
         * `openssl pkcs8 -topk8 -inform PEM -in private.key -outform pem -nocrypt -out pkcs8Private.key`
         * 
         */
        byte[] asn1PrivateKeyBytes = Base64.decodeBase64(base64PrivateKeyStr);
        ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(asn1PrivateKeyBytes));
        DLSequence dlSequence = (DLSequence) asn1InputStream.readObject();
        RSAPrivateKey rsaPrivateKey = RSAPrivateKey.getInstance(dlSequence);

        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(rsaPrivateKey.getModulus(),
                rsaPrivateKey.getPrivateExponent());

        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }
}