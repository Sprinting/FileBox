package service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import security.JsonWebToken;

/**
 * Servlet implementation class FileShare
 */
@WebServlet(
		urlPatterns={"/shareFile"},
		initParams=
				@WebInitParam(name="filesLocation",value="C:\\Kartik\\TomcatUploads\\")
			)
public class FileShare extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileShare() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession loginSession=request.getSession(false);
		final String key="secret"; //TODO change this for better security
		long duration=1000L*60L*2L; // 2hrs 
		if(loginSession!=null)
		{
			/*user is logged in and trying to share files*/
			/*create a sharelink, with a verfying token*/
			try
			{
				String filename=request.getParameter("f");
				if(filename.equals("")||filename==null)
				{
					throw new NullPointerException();
				}
				String username=loginSession.getAttribute("username").toString();
				System.out.println("username:"+username);
				System.out.println(this.getInitParameter("filesLocation")+username+"\\"+filename);
				
				JsonWebToken shareToken=new JsonWebToken();
				HashMap<String,String> headerMap=new HashMap<String,String>();
				HashMap<String,String> payloadMap=new HashMap<String,String>();
				
				headerMap.put("alg", "HS256");
				headerMap.put("typ", "JWT");
				
				payloadMap.put("iss", "/FileBox/shareFile");
				payloadMap.put("sub", "share_file");
				payloadMap.put("own", username);
				payloadMap.put("file", filename);
				payloadMap.put("exp", new Long(System.currentTimeMillis()+duration).toString());
				
					String unsignedJWT=shareToken
							.getJWT(shareToken.makeJson(headerMap), shareToken.makeJson(payloadMap));
					String signedJWT=shareToken
							.signJWT(unsignedJWT, key); //TODO change this secret for security;
					System.out.println(shareToken.decodeJWT(unsignedJWT));
					System.out.println(shareToken.verifyJWT(signedJWT, key));
					response.getWriter().write("{\"status\":true,\"token\":"+"\""+signedJWT+"\"}");
				} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				response.getWriter().write("{\"status\":false}");
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write("{\"status\":false}");
			}
		}
		else
		{
			response.sendRedirect("dashboard.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
