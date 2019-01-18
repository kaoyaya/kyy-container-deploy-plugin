package com.kaoyaya.jenkins.cd.cs;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;

public class Core {

    private char[] KEY_STORE_PASSWORD = "".toCharArray();
    public CertificateFactory cf;
    public PKCS8EncodedKeySpec spec;
    private KeyStore trustStore;
    private KeyStore keyStore;
    public CloseableHttpClient httpclient;


    public Core(String caCertS, String clientCertS, String ClientKeyS) {
        try {
            setCredentials(caCertS, clientCertS, ClientKeyS);
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, null)
                    .loadKeyMaterial(keyStore, KEY_STORE_PASSWORD)
                    .build();
            SSLConnectionSocketFactory conn = new SSLConnectionSocketFactory(
                    sslContext,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            httpclient = HttpClients.custom()
                    .setSSLSocketFactory(conn)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    CloseableHttpClient getHttpClient() {
        return httpclient;
    }

    private void setCredentials(String caCertS, String clientCertS, String ClientKeyS) {
        InputStream caCertIn = new ByteArrayInputStream(caCertS.getBytes(Charset.forName("UTF-8")));
        InputStream clientCertIn = new ByteArrayInputStream(clientCertS.getBytes(Charset.forName("UTF-8")));
        InputStream clientKeyIn = new ByteArrayInputStream(ClientKeyS.getBytes(Charset.forName("UTF-8")));
        BufferedReader clientKeyReader = new BufferedReader(new InputStreamReader(clientKeyIn, Charset.defaultCharset()));
        try {
            cf = CertificateFactory.getInstance("X.509");
            Certificate caCert = cf.generateCertificate(caCertIn);
            Certificate clientCert = cf.generateCertificate(clientCertIn);
            PEMKeyPair clientKeyPair = (PEMKeyPair) new PEMParser(clientKeyReader).readObject();
            spec = new PKCS8EncodedKeySpec(
                    clientKeyPair.getPrivateKeyInfo().getEncoded());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey clientKey = kf.generatePrivate(spec);
            //设置信任的证书
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            trustStore.setEntry("ca", new KeyStore.TrustedCertificateEntry(caCert), null);
            //设置私钥
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("client", clientCert);
            keyStore.setKeyEntry("key", clientKey, KEY_STORE_PASSWORD, new Certificate[]{clientCert});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
