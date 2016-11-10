package service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import security.DashboardManager;

/**
 * Servlet implementation class Dashboard
 */
@WebServlet("/dashboard")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession loginSession=request.getSession(false);
		if(loginSession==null)
			response.sendRedirect("index.jsp");
		else if(loginSession.getAttribute("username")==null)
		{
			loginSession.invalidate();
			response.sendRedirect("index.jsp");
		}
		else
		{
			DashboardManager userManager=new DashboardManager(loginSession.
					getAttribute("username").toString());
			try
			{
				response.setContentType("application/json");
				String json=userManager.prepareFileListJson("samples");
				loginSession.setAttribute("jsonResponse", json);
				response.getWriter().write(json);
			} catch (SQLException e) {
				
				e.printStackTrace();
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
