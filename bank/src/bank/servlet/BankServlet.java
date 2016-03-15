package bank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.Bank;
import bank.BankDriver;

public class BankServlet extends javax.servlet.http.HttpServlet {
	
	static class ServletSI
	{
		BankDriver localDriver;
			
		
		private static ServletSI instance;
		private ServletSI()
		{
			localDriver = new bank.local.Driver();
			try {
				localDriver.connect(new String[] { "" });
			} catch (IOException e) {
			}
			
		}
		public static ServletSI getInstance()
		{
			if (instance == null)
				instance = new ServletSI();
			return instance;
		}
		public Bank getBank()
		{
			return localDriver.getBank();
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		
		out.println("<table><tr><th>Number</th></tr>");
		Set<String> accNumbers = ServletSI.getInstance().getBank().getAccountNumbers();
		accNumbers.forEach(a -> out.println("<tr><td>" + a + "</td></tr>"));
		out.println("</table>");
		
		out.println("</body></html>");
		System.out.println("<< " + getClass().getName());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
