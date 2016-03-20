package bank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.Account;
import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;

@SuppressWarnings("serial")
public class BankServlet extends javax.servlet.http.HttpServlet {

	Bank bank = ServletSI.getInstance().getBank();

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
		out.flush();

		out.println("<html><body>");
		out.println("<h1>Account List</h1><table border=1>" + "<tr>" + "<th>Number</th>" + "<th>Owner</th>"
				+ "<th>Balance</th>" + "<th>Active</th>" + "<th>Deposit</th>" + "<th>Withdraw</th>"
				+ "<th>Transfer</th>" + "<th>Inactivate</th>" + "</tr>");
		Bank b = ServletSI.getInstance().getBank();
		Set<String> accNumbers = b.getAccountNumbers();
		accNumbers.forEach(a -> {
			try {
				Account acc = b.getAccount(a);
				out.println("<tr><td>" + a + "</td>" + "<td>" + acc.getOwner() + "</td>" + "<td>" + acc.getBalance()
						+ "</td>" + "<td>" + acc.isActive() + "</td>");

				// deposit
				out.println("<td><form action=\"\" method=\"post\">");
				out.println("<input type=\"hidden\" name=\"action\" value=\"deposit\">");
				out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
				out.println("Deposit Amount: <input type=\"text\" name=\"amount\">");
				out.println("<input type=\"submit\" value=\"Deposit\">");
				out.println("</form></td>");
				// withdraw
				out.println("<td><form action=\"\" method=\"post\">");
				out.println("<input type=\"hidden\" name=\"action\" value=\"withdraw\">");
				out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
				out.println("Withdraw Amount: <input type=\"text\" name=\"amount\">");
				out.println("<input type=\"submit\" value=\"Withdraw\">");
				out.println("</form></td>");
				// transfer
				// TODO select options
				out.println("<td><form action=\"\" method=\"post\">");
				out.println("<input type=\"hidden\" name=\"action\" value=\"transfer\">");
				out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
				out.println("<table><tr><td>Transfer To Acc:</td><td>");
				out.println("<select name=\"ToAccount\">");
				for (String accNum : accNumbers) {
					out.println("<option value=\"" + accNum + "\">" + accNum + "</option>");
				}
				out.println("</select></td></tr>");
				out.println("<tr><td>Amount</td><td><input type=\"text\" name=\"amount\"></td></tr>");
				out.println("<tr><td colspan=\"2\"><input type=\"submit\" value=\"Transfer\"></td></tr></table>");
				out.println("</form></td>");
				// set Inactive
				out.println("<td><form action=\"\" method=\"post\">");
				out.println("<input type=\"hidden\" name=\"action\" value=\"inactivate\">");
				out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
				out.println("<input type=\"submit\" value=\"Inactivate\">");
				out.println("</form></td>");

				out.println("</tr>");
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		out.println("</table>");

		// createaccount�
		out.println("<h3>Create new Account</h3>");
		out.println("<table>");
		out.println("<form action=\"\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"action\" value=\"createaccount\">");
		out.println("<tr><td>Owner:</td><td><input type=\"text\" name=\"owner\"></td></tr>");
		out.println("<tr><td>Balance:</td><td><input type=\"text\" name=\"balance\"></td></tr>");
		out.println("<tr><td colspan=\"2\"><input type=\"submit\" value=\"Submit\"></td></tr>");
		out.println("</form>");
		out.println("<table>");
		out.println("</body></html>");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = getAction(request);

		switch (action) {
		case "createaccount":
			createAccount(request);
			response.sendRedirect("/bank");
			break;
		case "deposit":
			try {
				deposit(request);
				response.sendRedirect("/bank");
			} catch (InactiveException e) {
				createErrorPage(response, e);
			}
			break;
		case "withdraw":
			try {
				withdraw(request);
				response.sendRedirect("/bank");
			} catch (IllegalArgumentException | OverdrawException | InactiveException e) {
				createErrorPage(response, e);
			}
			break;
		case "transfer":
			try {
				transfer(request);
				response.sendRedirect("/bank");
			} catch (IllegalArgumentException | OverdrawException | InactiveException e) {
				createErrorPage(response, e);
			}
			break;
		case "inactivate":
			inactivate(request);
			response.sendRedirect("/bank");
			break;
		default:
			response.sendRedirect("/bank");
			break;
		}
	}

	private void createAccount(HttpServletRequest request) {
		try {
			String accNumber = bank.createAccount(getOwner(request));
			double balance = getBalance(request);
			bank.getAccount(accNumber).deposit(balance);
		} catch (IOException | InactiveException e) {
			// nothing
		}
	}

	private void deposit(HttpServletRequest request) throws InactiveException {
		Bank bank = ServletSI.getInstance().getBank();
		try {
			Account acc = bank.getAccount(getAccountNo(request));
			if (acc != null) {
				acc.deposit(getAmount(request));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void withdraw(HttpServletRequest request)
			throws IllegalArgumentException, OverdrawException, InactiveException {
		Bank bank = ServletSI.getInstance().getBank();
		try {
			Account acc = bank.getAccount(getAccountNo(request));
			if (acc != null) {
				acc.withdraw(getAmount(request));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void transfer(HttpServletRequest request)
			throws IllegalArgumentException, OverdrawException, InactiveException {
		Bank bank = ServletSI.getInstance().getBank();
		try {
			Account acc = bank.getAccount(getAccountNo(request));
			Account toAcc = bank.getAccount(getToAccount(request));
			if ((acc != null) && (toAcc != null)) {
				bank.transfer(acc, toAcc, getAmount(request));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void inactivate(HttpServletRequest request) {
		Bank bank = ServletSI.getInstance().getBank();

		try {
			Account acc = bank.getAccount(getAccountNo(request));
			if (acc != null) {
				acc.setActive(false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getOwner(HttpServletRequest request) {
		return request.getParameterValues("owner")[0];
	}

	private String getAction(HttpServletRequest request) {
		return request.getParameterValues("action")[0];
	}

	private double getBalance(HttpServletRequest request) {
		try {
			return Double.parseDouble(request.getParameterValues("balance")[0]);
		} catch (Exception e) {
			return 0;
		}
	}

	private String getAccountNo(HttpServletRequest request) {
		return request.getParameter("accountNo");
	}

	private double getAmount(HttpServletRequest request) {
		try {
			return Double.parseDouble(request.getParameter("amount"));
		} catch (Exception e) {
			return 0;
		}
	}

	private String getToAccount(HttpServletRequest request) {
		return request.getParameter("ToAccount");
	}

	private void createErrorPage(HttpServletResponse response, Exception e) {
		try {
			PrintWriter out = response.getWriter();
			out.println("<html><body>");
			out.println("<p><b>Our bank could not process your command.</b></p>");
			out.println("<p>The following exception was thrown:</p>");
			out.println("<p>");
			e.printStackTrace(out);
			out.println("</p>");
			out.println("<form action=\"\" method=\"get\">");
			out.println("<input type=\"submit\" value=\"Back\">");
			out.println("</form>");
			out.println("</body></html>");
		} catch (IOException e1) {}
	}
}
