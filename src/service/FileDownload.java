package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import security.DashboardManager;

/**
 * Servlet implementation class FileDownload
 */
@WebServlet(
		urlPatterns={"/getFile"},
		initParams=
				@WebInitParam(name="downloadRoot",value="C:\\Kartik\\TomcatUploads\\")
				
		)
public class FileDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileDownload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user,file;
		user=request.getParameter("u");
		file=request.getParameter("f");
		System.out.println("getFile doGet Called");
		if(user==null || file==null)
			response.sendRedirect("dashboard.jsp");
		
		else
		{
			String filepath=this.getInitParameter("downloadRoot")+user+"\\"+file;
			File downloadFile=new File(filepath);
			System.out.println(filepath);
			boolean owner=DashboardManager.checkFileOwnership(user, filepath);
			if(owner)
			{
				if(downloadFile.exists())
				{
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
			else
			{
				request.setAttribute("owner", false);
				request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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
