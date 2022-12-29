package org.needcoke.coke.web.client;

import okhttp3.OkHttpClient;
import pers.warren.ioc.annotation.Bean;
import pers.warren.ioc.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class OkHttpClientHolder {

    private final static OkHttpClient okHttpClient;

    static {
        X509TrustManager trustManager = null;
        SSLContext sslContext = null;
        try {
            trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
        okHttpClient= builder.build();
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
