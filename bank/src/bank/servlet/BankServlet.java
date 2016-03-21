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

		Bank b = ServletSI.getInstance().getBank();
		Set<String> accNumbers = b.getAccountNumbers();
		HtmlPrinter printer = new HtmlPrinter(out, b);
		
		printer.printTopBody();

		accNumbers.forEach(a -> {
			try {
				Account acc = b.getAccount(a);
				printer.printAccountInfo(acc);
				printer.printDepositOption(acc);
				printer.printWithdrawOption(acc);
				printer.printTransferOption(acc, accNumbers);
				printer.printSetInactiveOption(acc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		printer.printCreateAccountOption();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = getAction(request);

		switch (action) {
		case "createaccount":
			try {
				createAccount(request);
				response.sendRedirect("/bank");
			} catch (IllegalArgumentException | InactiveException e) {
				createErrorPage(response, e);
			}
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

	private void createAccount(HttpServletRequest request) throws IllegalArgumentException, InactiveException {
		try {
			String accNumber = bank.createAccount(getOwner(request));
			double balance = getBalance(request);
			bank.getAccount(accNumber).deposit(balance);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException | InactiveException e) {
			throw e;
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
			out.println("<a href = \"/bank\">Back</a>");
			out.println("</body></html>");
		} catch (IOException e1) {
		}
	}
}
