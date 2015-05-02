package com.futrena.restws.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futrena.restws.request.StripeToken;
import com.stripe.net.*;
import com.stripe.model.*;

@RestController
public class stripeController {

	@RequestMapping("/successSubmission/{code}")
	@Secured("API")
	public ResponseEntity<Object> SuccessSubmission(@PathVariable String code) {

		String param = "client_secret=sk_test_e8sk4yI5mNw4UhaxiwOgZuhD"
				+ "&code=" + code + "&grant_type=authorization_code";
		return new ResponseEntity<Object>(getToken(param), HttpStatus.OK);
	}

	@RequestMapping("/charge")
	@Secured("API")
	public Charge Charge(@RequestBody StripeToken token) {
		System.out.println(token);

		RequestOptions options = (new RequestOptions.RequestOptionsBuilder())
				.setApiKey("sk_test_e8sk4yI5mNw4UhaxiwOgZuhD").setStripeAccount("acct_15wZZxHynNlW50G1").build();

		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", 100000);
		chargeParams.put("currency", "usd");
		chargeParams.put("source", token.getId());
		chargeParams.put("description", "Adding charge");

		try {
			Charge charge = Charge.create(chargeParams, options);
			return charge;
		} catch (com.stripe.exception.StripeException e) {
			e.printStackTrace();
			return null;
		}		
	}

	public ResponseEntity<Object> getToken(String param) {
		String TOKEN_URL = "https://connect.stripe.com/oauth/token";
		StringBuilder response = new StringBuilder();
		try {

			URL request = new URL(TOKEN_URL);
			HttpURLConnection connection = (HttpURLConnection) request
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(param.getBytes().length));
			connection.setUseCaches(false);

			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(param);
			wr.flush();
			wr.close();

			InputStream is = connection.getInputStream();

			if (is != null) {
				String line;
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is));
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					reader.close();
				} finally {
					is.close();
				}
			}
			connection.disconnect();
			return new ResponseEntity<Object>(response.toString(),
					HttpStatus.OK);

		} catch (IOException e) {
			return new ResponseEntity<Object>(e, HttpStatus.OK);
		}
	}


}