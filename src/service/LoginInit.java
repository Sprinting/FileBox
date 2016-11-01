package service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import security.LoginException;
import security.SQLManager;

/**
 * Servlet implementation class LoginInit
 */
@WebServlet("/login")
public class LoginInit extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/*
	 * read these from files in final version
	 */
    private static final String[] sqldbDetails={"root","parth@kartik123"};   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginInit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession loginSession=request.getSession(false);
		if(!(loginSession==null))
		{
			response.sendRedirect("dashboard.jsp");
		}
		else
		{
			response.sendRedirect("index.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession loginSession=request.getSession(false);
		
		/*
		 * check for an already existing session, if it exists
		 * just redirect to the /dashboard route
		 * and return
		 */
		//System.out.println("Session status:"+loginSession==null);
		if(!(loginSession==null))
		{
			response.sendRedirect("dashboard.jsp");
		}
		else
		{
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			SQLManager userManager=new SQLManager(username,password,sqldbDetails[0],sqldbDetails[1]);
			System.out.println(System.getProperty("catalina.home"));
			try {
				userManager.attemptConnection("samples");
				userManager.getUserDetails("sample_users");
				if(userManager.validateUser())
				{
					loginSession=request.getSession(true);
					loginSession.setAttribute("username", userManager.getUsername());
					loginSession.setAttribute("hash", userManager.getDbhash());
					response.sendRedirect("dashboard.jsp");
				}
				else
				{
					request.setAttribute("invalidEntry", true);
					request.getRequestDispatcher("index.jsp").forward(request, response);
				}
				
				} catch (LoginException e) {
				e.printStackTrace();
				
			} catch (SQLException e) {
				System.out.println(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"SQL connection credentials are invalid");;
			}
			finally
			{
				userManager.destruct();
			}
			
		}
	}

}
