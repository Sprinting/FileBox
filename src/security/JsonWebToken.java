package security;

import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import org.apache.tomcat.util.codec.binary.Base64;

public class JsonWebToken 
{
	final static String ALGORITHM="HmacSHA256";
	final static String ALG="HS256";
	
	public JsonWebToken()
	{
	}
	/*
	 * 
	 */
	public  String makeJson(HashMap<String,String> map)
	{
		JsonObjectBuilder jsonBuilder=Json.createObjectBuilder();
		map.forEach((key,value) ->{
			
			jsonBuilder.add(key.toString(), value.toString());
		});
		JsonObject json=jsonBuilder.build();
		
		return json.toString();
		
	}
	/*
	 * 
	 */
	boolean jsonPropEquals(String prop,String val,String json)
	{
		JsonReader jsonReader=Json.createReader(new StringReader(json));
		return jsonReader.readObject()
				.getJsonString(prop)
				.getString()
				.equals(val);
	}
	/*
	 * 
	 */
	boolean headerPropEquals(String prop,String val,String json)
	{
		return jsonPropEquals(prop,val,json);
	}
	/*
	 * 
	 */
	boolean payloadPropEquals(String prop,String val,String json)
	{
		return payloadPropEquals(prop,val,json);
	}
	/*
	 * 
	 */
	 boolean hasJsonProp(String prop,String json)
	{
		JsonReader jsonReader=Json.createReader(new StringReader(json));
		return jsonReader.readObject().containsKey(prop);
	}
	/*
	 * 
	 */
	public  boolean hasHeaderProp(String prop,String header)
	{
		return hasJsonProp(prop,header);
	}
	/*
	 * 
	 */
	public  boolean hasPayloadProp(String prop,String header)
	{
		return hasJsonProp(prop,header);
	}
	/*
	 * 
	 */
	String parseJson(String prop,String json)
	{
		JsonReader payloadReader=Json.createReader(new StringReader(json));
		JsonString res;
		if((res=payloadReader.readObject().getJsonString(prop))!=null)
		{
			System.out.println(prop+" : "+res.getString());
			return res.getString();
		}
		else return null;
	}
	/*
	 * 
	 */
	public  String parsePayload(String prop,String payload)
	{
		return parseJson(prop,payload);
	}
	/*
	 * 
	 */
	public  String parseHeader(String prop,String header)
	{
		return parseJson(prop,header);
	}
	 /*
	  * 
	  */
	 String encode_signature(String data, String key) throws Exception
	{
		Mac sha256_HMAC=Mac.getInstance(ALGORITHM);
		//message authentication code;
		SecretKeySpec secret=new SecretKeySpec(key.getBytes("UTF-8")
				,ALGORITHM);
		//secret key 
		sha256_HMAC.init(secret);
		String hash=Base64.encodeBase64URLSafeString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
		System.out.println("hash: "+hash);
		return hash;
	}
	/*
	 * 
	 */
	public   String getJWT(String header,String data) throws Exception
	{
		String JWT=Base64.encodeBase64URLSafeString(header.getBytes("UTF-8"))+"."
				+Base64.encodeBase64URLSafeString(data.getBytes("UTF-8"));
		return JWT;
	}
	/*
	 * 
	 */
	public  String signJWT(String token,String sign)throws Exception
	{
		return token+"."+encode_signature(token,sign);
		
	}
	public ArrayList<String> decodeJWT(String token) throws UnsupportedEncodingException
	{
		 ArrayList<String> decoded=new ArrayList<String>();
		 int j=0;
		 for(final String i:token.split("\\."))
		 {
			 if(j>1)
				 break;
			decoded.add(new String(Base64.decodeBase64(i),
					"UTF-8"));
			j++;
		 }
		return decoded;
	}
	public boolean verifyJWT(String token,String secret)
	{
		try
		{
			ArrayList<String> tokenList=decodeJWT(token);
			String header=tokenList.get(0);
			String payload=tokenList.get(1);
			if(headerPropEquals("alg",JsonWebToken.ALG,header))
			{
				String testJWT=getJWT(header,payload);
				String signature=encode_signature(testJWT,secret);
				System.out.println("Verifier: "+signature);
				System.out.println("Candidate: "+token.split("\\.")[2]);
				return (signature.equals(token.split("\\.")[2]));
				
			}
			return false;
		}
		catch(Exception e)
		{
			System.err.println("Error While verifying JWT");
			e.printStackTrace();
		}
		return true;
	}
	public String makeJson(String key, ArrayList<String> values) {
		key="\""+key+"\"";
		String dateKey="\"uploaded\"";
		ArrayList<String> dates=new ArrayList<String>();
		ArrayList<String> names=new ArrayList<String>();
		SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		for(String i:values)
		{
			
			File f=new File(i);
			if(f.exists())
			{
				dates.add("\""+dateFormat.format((f.lastModified()))+"\"");
			}
			i="\""+i+"\"";
			
			names.add(i);
			
		}
		
		
		return "{\"files\":"+names+","+dateKey+":"+dates+"}"; 
	}
	
}
