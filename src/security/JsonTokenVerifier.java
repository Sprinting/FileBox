package security;

public interface JsonTokenVerifier {
	
	public void requireIssuer(String iss) throws MissingClaimException,IncorrectClaimException;
	public void requireSubject(String sub) throws MissingClaimException,IncorrectClaimException;
	public void requireAudience(String aud) throws MissingClaimException,IncorrectClaimException;
	

}
