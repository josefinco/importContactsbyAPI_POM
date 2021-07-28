package com.project.massiveimportPOM;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.project.Util.RetornoWS;
import com.project.Util.Util;
import com.project.Util.WebServiceException;

public class Main {

	static String wsURL = "https://epma121.nuvem365ti.com.br/VP_POM_Service/v4/contactlists/1/contacts";
	static String user = "epadmin";
	static String pass = "Team@365";
	static JSONObject body = new JSONObject();
	static int timeout = 1500;

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {

		ExecutorService threadPool = Executors.newFixedThreadPool(50);
		System.out.println("Iniciando a chamada ao WS: ");
		threadPool.submit(t1);
		threadPool.shutdown();

	}

	private static Runnable t1 = new Runnable() {
		public void run() {
			long timeinitial = System.currentTimeMillis();
			for (int i = 0; i <= 30; i++) {

				try {
					Long time = System.currentTimeMillis();
					body = bodyformate(i);
					RetornoWS retorno = new RetornoWS();
					System.out.println(body);
					retorno = Util.postHttpsWS(wsURL, user, pass, timeout, body);
					--time;
					System.out.println("Tempo de resposta: " + (System.currentTimeMillis() - time) + "ms");
					System.out.println("StatusCode: " + retorno.getStatusCode());
					JSONObject json = new JSONObject(retorno.getMessage());
					System.out.println("Message: " + json);
				} catch (IOException | JSONException | WebServiceException | KeyManagementException
						| NoSuchAlgorithmException e) {
					System.out.println("Falha ao Executar o WS");
					System.out.println(e.getMessage());
				}

			}
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("Tempo total de execução: "
					+ TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - timeinitial)) + " seg");

		}
	};

	public static JSONObject bodyformate(int id) {
		JSONObject body = new JSONObject();
		JSONObject custonatb = new JSONObject();
		JSONArray jsonarr = new JSONArray();
		
		
		custonatb.put("attributeName", "Pedido");
		custonatb.put("attributeValue", id);
		jsonarr.put(custonatb);
		
		// Contruction body to send
		body.put("userContactID", id);
		body.put("title", "MR");
		body.put("firstName", "Contato Nome " + id);
		body.put("lastName", "Contato Sobrenome" + id);
		body.put("phoneNumber1", "011944432365");
		body.put("phoneNumber1CtryCode", "");
		body.put("phoneNumber1TimeZone", "");
		body.put("phoneNumber2", "011973532365");
		body.put("phoneNumber2CtryCode", "");
		body.put("phoneNumber2TimeZone", "");
		body.put("email", "test@test.com");
		body.put("addressLine1", "");
		body.put("addressLine2", "");
		body.put("addressLine3", "");
		body.put("addressLine4", "");
		body.put("addressLine5", "");
		body.put("country", "");
		body.put("zipcode", "");
		body.put("automaticUpdateTimezone", false);
		body.put("updateExistingContact", true);
		body.put("checkRejectPattern", false);
		body.put("checkPhoneFormatRule", false);
		body.put("checkDNC", false);
		body.put("sysAgentId", 65001);
		body.put("customAttributeList", jsonarr);
		return body;

	}

}
