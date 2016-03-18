package bank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Set;

import javax.print.attribute.standard.PrinterLocation;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.Account;
import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;

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
				+ "<th>transfer</th>" + "<th>inactivate</th>" + "</tr>");
		Bank b = ServletSI.getInstance().getBank();
		Set<String> accNumbers = b.getAccountNumbers();
		accNumbers.forEach(a -> {
			try {
				Account acc = b.getAccount(a);
				out.println("<tr><td>" + a + "</td>" + "<td>" + acc.getOwner() + "</td>" + "<td>" + acc.getBalance()
						+ "</td>" + "<td>" + acc.isActive() + "</td>");

				out.println("<br>");
				// deposit
				out.println("<td><form action=\"\" method=\"post\">");
				out.println("<input type=\"hidden\" name=\"action\" value=\"deposit\">");
				out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
				out.println("Deposit Amount: <input type=\"text\" name=\"amount\">");
				out.println("<input type=\"submit\" value=\"Deposit\">");
				out.println("</form></td>");
				out.println("<br>");
				// withdraw
				out.println("<td><form action=\"\" method=\"post\">");
				out.println("<input type=\"hidden\" name=\"action\" value=\"withdraw\">");
				out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
				out.println("Withdraw Amount: <input type=\"text\" name=\"amount\">");
				out.println("<input type=\"submit\" value=\"Withdraw\">");
				out.println("</form></td>");
				out.println("<br>");
				// transfer
				out.println("<td><form action=\"\" method=\"post\">");
				out.println("<input type=\"hidden\" name=\"action\" value=\"transfer\">");
				out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
				out.println(
						"<table><tr><td>Transfer To Acc:</td><td><input type=\"text\" name=\"ToAccount\"></td></tr>");
				out.println("<tr><td>Amount</td><td><input type=\"text\" name=\"amount\"></td></tr>");
				out.println("<tr><td colspan=\"2\"><input type=\"submit\" value=\"Transfer\"></td></tr></table>");
				out.println("</form></td>");
				out.println("<br>");

				// set Inactive
				out.println("<td><form action=\"\" method=\"post\">");
				out.println("<input type=\"hidden\" name=\"action\" value=\"inactivate\">");
				out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
				out.println("<input type=\"submit\" value=\"SetInactive\">");
				out.println("</form></td>");

				out.println("</tr>");
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		out.println("</table>");

		// createaccount¨
		out.println("<h1>Create new Account</h1><br>");
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
			response.sendRedirect("");
			break;
		case "deposit":
			try {
				deposit(request);
				response.sendRedirect("");
			} catch (InactiveException e) {
				response.sendRedirect("");
				// TODO redirect to error Page?
			}
			break;
		case "withdraw":
			try {
				withdraw(request);
				response.sendRedirect("");
			} catch (IllegalArgumentException | OverdrawException | InactiveException e) {
				response.sendRedirect("");
				// TODO redirect to error Page?
			}
			break;
		case "transfer":
			try {
				transfer(request);
				response.sendRedirect("");
			} catch (IllegalArgumentException | OverdrawException | InactiveException e) {
				response.sendRedirect("");
				// TODO redirect to error Page?
			}
			break;
		case "inactivate":
			inactivate(request);
			response.sendRedirect("");
			break;
		default:
			response.sendRedirect("");
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
}
