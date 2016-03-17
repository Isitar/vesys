package bank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;

public class BankServlet extends javax.servlet.http.HttpServlet {

	static class ServletSI {
		BankDriver localDriver;

		private static ServletSI instance;

		private ServletSI() {
			localDriver = new bank.local.Driver();
			try {
				localDriver.connect(new String[] { "" });
			} catch (IOException e) {
			}

		}

		public static ServletSI getInstance() {
			if (instance == null)
				instance = new ServletSI();
			return instance;
		}

		public Bank getBank() {
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

		// createaccount
		out.println("<form action=\"\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"action\" value=\"createaccount\">");
		out.println("Owner: <input type=\"text\" name=\"owner\"><br>");
		out.println("Balance: <input type=\"text\" name=\"balance\"><br>");
		out.println("<input type=\"submit\" value=\"Submit\">");

		out.println("</body></html>");
	}

	private void createAccount(HttpServletRequest request) {
		Bank bank = ServletSI.getInstance().getBank();
		String owner = getOwner(request);
		try {
			String accountNumber = bank.createAccount(owner);
			double balance = Double.parseDouble(getBalance(request));
			bank.getAccount(accountNumber).deposit(balance);
		} catch (IOException | InactiveException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = getAction(request);

		switch (action) {
			case "createaccount":
				createAccount(request);
				// TODO response.setStatus(HttpServletResponse.SC_CREATED);
				break;
			default:
				break;
		}
		

	}
	
	private String getOwner(HttpServletRequest request) {
		return request.getParameterValues("owner")[0];
	}

	private String getAction(HttpServletRequest request) {
		return request.getParameterValues("action")[0];
	}

	private String getBalance(HttpServletRequest request) {
		return request.getParameterValues("balance")[0];
	}

}
