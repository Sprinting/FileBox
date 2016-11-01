package tests;

import java.util.HashMap;

import security.JsonWebToken;

public class TokenManager {

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			HashMap<String,String> headerMap=new HashMap<String,String>();
			HashMap<String,String> payloadMap=new HashMap<String,String>();
			String key="secret";
			
			
			headerMap.put("alg", "HS256");
			headerMap.put("typ", "JWT");
			
			payloadMap.put("iss", "FileBox.src.tests.TokenManager");
			payloadMap.put("sub", "test");
			payloadMap.put("aud", "null");
			payloadMap.put("exp", new Long(System.currentTimeMillis()+600L).toString());
			
			JsonWebToken testToken=new JsonWebToken();
			String header=testToken.makeJson(headerMap);
			String payload=testToken.makeJson(payloadMap);
			String unsignedJWT=testToken.getJWT(header, payload);
			String signedJWT=testToken.signJWT(unsignedJWT, key);
			
			
			boolean verified;
			verified=testToken.verifyJWT(signedJWT, key);
			System.out.println(verified);
			System.out.println(testToken.decodeJWT(signedJWT));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

}
}
