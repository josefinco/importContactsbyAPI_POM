package com.project.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;

public class Util {

	public static RetornoWS postHttpsWS(String url, String username, String password, int timeout,  JSONObject body) throws IOException, WebServiceException, KeyManagementException, NoSuchAlgorithmException {
		RetornoWS retorno = new RetornoWS();
		String authHeaderValue = null;

		URL obj = new URL(url);
		int responseCode = 404;
		StringBuffer response = new StringBuffer();
		
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }	
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		if (username != null && password != null) {
			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			authHeaderValue = "Basic " + encodedAuth;
		}
		try {
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setReadTimeout(timeout);
			con.setRequestProperty("Content-Type", "application/json");
			if (authHeaderValue != null) {
				con.setRequestProperty("Authorization", authHeaderValue);
			}
			con.setRequestProperty("X-Requested-With", "rest");
			con.setRequestProperty("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
			con.setDoOutput(true);
			try(OutputStream os = con.getOutputStream()) {
			    byte[] input = body.toString().getBytes("utf-8");
			    os.write(input, 0, input.length);			
			}

			responseCode = con.getResponseCode();
			BufferedReader in;

			if (responseCode >= 200 && responseCode < 400) {
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			retorno.setMessage(response.toString());
			retorno.setStatusCode(responseCode);

			if (!(responseCode >= 200 && responseCode < 400)) {
				if (responseCode != 404 && responseCode != 417) {
					throw new WebServiceException();
				}
			}

		} catch (IOException e) {
			throw new IOException(e.getMessage(), e.getCause());
		} catch (WebServiceException e) {
			throw new WebServiceException(e.getMessage(), e.getCause(), retorno);
		}
		return retorno;
	}
}
