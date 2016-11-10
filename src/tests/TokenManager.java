package tests;

import java.util.ArrayList;
import java.util.HashMap;

import security.JsonWebToken;

public class TokenManager {

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			HashMap<String,String> headerMap=new HashMap<String,String>();
			HashMap<String,String> payloadMap=new HashMap<String,String>();
			String key="secret";
			
			long duration=1000L*60L*60L;
			headerMap.put("alg", "HS256");
			headerMap.put("typ", "JWT");
			
			payloadMap.put("iss", "/FileBox/shareFile");
			payloadMap.put("sub", "share_file");
			payloadMap.put("own", "Kartik");
			payloadMap.put("file", "schema.xlsx");
			payloadMap.put("exp", new Long(System.currentTimeMillis()+duration).toString());
			
			JsonWebToken testToken=new JsonWebToken();
			String header=testToken.makeJson(headerMap);
			String payload=testToken.makeJson(payloadMap);
			String unsignedJWT=testToken.getJWT(header, payload);
			String signedJWT=testToken.signJWT(unsignedJWT, key);
			
			
			boolean verified;
			verified=testToken.verifyJWT(signedJWT, key);
			boolean verifiedMalformed=testToken.verifyJWT("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGFyZV9maWxlIiwiZmlsZSI6InNjaGVtYS54bHN4Iiwib3duIjoiTGFrc2hheSIsImlzcyI6Ii9GaWxlQm94L3NoYXJlRmlsZSIsImV4cCI6IjE0Nzg3NTU1OTk5OTEifQ.MwoE0R-BoIbYYyfFS1OdgWSsilE1tIT2TfN4ZbpzZVE", "secret");
			System.out.println(verified+" "+verifiedMalformed);
			System.out.println("Decoded\n"+testToken.decodeJWT(signedJWT));
			System.out.println("Encoded\n"+signedJWT);
			
			ArrayList<String> token=testToken.decodeJWT(signedJWT);
			String json=token.get(1);
			System.out.println("++++\n");
			System.out.println(json);
			System.out.println("++++\n");
			
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

}
}
