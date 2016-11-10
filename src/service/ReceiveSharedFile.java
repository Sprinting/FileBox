package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import security.JsonWebToken;

/**
 * Servlet implementation class ReceiveSharedFile
 */
@WebServlet(
		urlPatterns={"/getSharedFile"},
		initParams=
				@WebInitParam(name="fileRoot",value="C:\\Kartik\\TomcatUploads\\")
				
		)
public class ReceiveSharedFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReceiveSharedFile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession loginSession=request.getSession(false);
		String token=request.getParameter("token");
		final String key="secret"; //todo change this for better security;
		if(token==null||token.equals(""))
		{
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token. Please request a new one!");
		}
		else
		{
			
			JsonWebToken shareToken=new JsonWebToken();
			boolean verified=shareToken.verifyJWT(token, key);
			String file="",filepath="",owner="";
			if(verified)
			{
				ArrayList<String> decodedJWT=shareToken.decodeJWT(token);
				
					String expiry=shareToken.parsePayload("exp", decodedJWT.get(1)).replace("\"", "");
					System.out.println(expiry);
					Long expired=Long.parseLong(expiry);
					System.out.println("Expiry time: "+expired);
					
					if(expired<System.currentTimeMillis())
					{
						System.out.println(expired);
						System.out.println(System.currentTimeMillis());
						response.sendError(HttpServletResponse.SC_FORBIDDEN, 
								"Invalid token(Exipred). Please request a new one!");
					}
					else
					{
						System.out.println("ElSE");
						file=shareToken.parsePayload("file", decodedJWT.get(1)).replace("\"", "");
						owner=shareToken.parsePayload("own", decodedJWT.get(1)).replace("\"", "");
						filepath=this.getInitParameter("fileRoot").toString()+
								owner+"\\"+file;
						System.out.println(filepath);
						//TODO implement a sqlmanager class for logged in users,lol
						if(new File(filepath).exists() && (loginSession==null||loginSession!=null))
						{
							response.setContentType("application/octet-stream");
							response.setContentType("application/octet-stream");
							response.setHeader("Content-Disposition","attachment; filename=\""+file+"\"");
							FileInputStream uploadStream=new FileInputStream(filepath);
							OutputStream downloadStream=response.getOutputStream();
						
						try
						{
							
							int currByte=0;
							while((currByte=uploadStream.read())!=-1)
							{
								downloadStream.write(currByte);
							}
							
						}
						catch(Exception e)
						{
							response.getWriter().println(e.getMessage());
						}
						finally
						{
							uploadStream.close();
						}
					}
					
					
				}
			}
			else
			{
				response.sendError(HttpServletResponse.SC_FORBIDDEN, 
						"Invalid token. Please request a new one!");
			}
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
