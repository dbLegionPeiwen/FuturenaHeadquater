package com.futrena.restws.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.futrena.restws.models.*;

public class PaypalUtil {
	
	   public static String getPaymentDesc(String cartID, List<Product> products)
	    {
		   StringBuilder paymntDesc = new StringBuilder();
		   paymntDesc.append(cartID);
		   for (Product p :products){
			   paymntDesc.append( "_" + p.getProductID());
		   }
		   return paymntDesc.toString();
	    }
	   
	   
	   public static String float2Str(float f)
	    {
		   String ret="0";
		   if (f==0) return ret;
		   return Float.toString(f);
	    }
	   
	   public static String getPayPalAccount( String user)
	    {
		   
		   String ret="chenshoufang@gmail.com";
		   
		   if (user.equalsIgnoreCase("0")) 
		   		return "chenshoufang@gmail.com";
		   
		   if (user.equalsIgnoreCase("1")) 
		   		return "shoufang.chen@gmail.com";
		   
		   return "york.tips@gmail.com";
	    }
	   
	   
	   public static float getShippingAmt(String country, List<Product> products)
	    {
		   float fShippingAmt = 0;
		   for ( Product p :products ) {
			   fShippingAmt=fShippingAmt+ getShippingAmt(country,p);
		   }
		   
		   return fShippingAmt;
	    }
	   
	   public static float getShippingAmt(String country, Product p)
	    {
		   float amt=0;
		   if (country.equalsIgnoreCase("CA") || country.equalsIgnoreCase("CANADA"))
			   amt= p.getDeliveryFeeCA();
		   else if (country.equalsIgnoreCase("CN") || country.equalsIgnoreCase("CHINA") || country.equalsIgnoreCase("CH"))
			   amt= p.getDeliveryFeeCH();
		   else if (country.equalsIgnoreCase("US") || country.equalsIgnoreCase("AMERICA") || country.equalsIgnoreCase("USA"))
			   amt=p.getDeliveryFeeUS();
		   
		   return amt;
	    }
	   
	    //UUID unique_id = UUID.randomUUID();
	    public static HashMap httpsCall(String PaypalHost, String url, String paras, String unique_id)
	    {
	        String agent = "";
	        String respText = "";
	        HashMap nvp = null;  

	        String encodedData = paras;
	        try
	        {
	            URL postURL = new URL(url );
	            HttpURLConnection conn = (HttpURLConnection) postURL.openConnection();

	            // Set connection parameters. We need to perform input and output,
	            // so set both as true.
	            conn.setDoInput(true);
	            conn.setDoOutput(true);
	            
	            // Set the content type we are POSTing. We impersonate it as
	            // encoded form data
	            conn.setRequestProperty("Content-Type", "text/namevalue");
	            conn.setRequestProperty("User-Agent", agent);
	            conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
	            conn.setRequestMethod("POST");

	            conn.setRequestProperty("Host", PaypalHost);
	            conn.setRequestProperty("X-VPS-CLIENT-TIMEOUT", "45");
	            conn.setRequestProperty("X-VPS-REQUEST-ID", unique_id);
	            
	            // get the output stream to POST to.
	            DataOutputStream output = new DataOutputStream(conn.getOutputStream());
	            output.writeBytes(encodedData);
	            output.flush();
	            output.close();
	            
	            // Read input from the input stream.
	            DataInputStream in = new DataInputStream(conn.getInputStream());
	            int rc = conn.getResponseCode();
	            if (rc != -1)
	            {
	                BufferedReader is = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                String _line = null;
	                while (((_line = is.readLine()) != null))
	                {
	                    respText = respText + _line;
	                    //System.out.println("**DEBUG : response text : " + respText);
	                }
	                nvp = deformatNVP(respText);
	            }
	            return nvp;
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    
	/*********************************************************************************
	 * deformatNVP: Function to break the NVP string into a HashMap
	 * 	pPayLoad is the NVP string.
	 * returns a HashMap object containing all the name value pairs of the string.
	 *********************************************************************************/
	public static HashMap deformatNVP(String pPayload)
	{
		HashMap nvp = new HashMap();
		StringTokenizer stTok = new StringTokenizer(pPayload, "&");
		while (stTok.hasMoreTokens())
		{
			StringTokenizer stInternalTokenizer = new StringTokenizer(stTok.nextToken(), "=");
			if (stInternalTokenizer.countTokens() == 2)
			{
				String key = stInternalTokenizer.nextToken();
				String value = stInternalTokenizer.nextToken();
				nvp.put(key.toUpperCase(), value);
			}
		}
		return nvp;
	}   
	   
	    
	public static String httpRequestDump(HttpServletRequest request) {
		StringBuilder s=new StringBuilder();
		s.append("HEAD:");
		Enumeration headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) 
		{
			String headerName = (String)headerNames.nextElement();
			s.append(headerName + "=" + (request.getHeader(headerName) + ";"));
			//System.out.println(request.getHeader(headerName));
		}
		
		Enumeration params = request.getParameterNames(); 
		s.append("BODY:");
		while(params.hasMoreElements())
		{
			String paramName = (String)params.nextElement();
			s.append(paramName + "=" + request.getParameter(paramName) + ";");
			//System.out.println("Attribute Name - "+paramName+", Value - "+request.getParameter(paramName));
		}		    
		return s.toString();
	}
	    
}