package bank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import bank.Account;
import bank.Bank;

public class HtmlPrinter {

	PrintWriter out;
	Bank b;

	public HtmlPrinter(PrintWriter out, Bank b) {
		this.out = out;
		this.b = b;
	}

	public void printTopBody() {
		out.println("<html><body>");
		out.println("<h1>Account List</h1><table border=1>" + "<tr>" + "<th>Number</th>" + "<th>Owner</th>"
				+ "<th>Balance</th>" + "<th>Active</th>" + "<th>Deposit</th>" + "<th>Withdraw</th>"
				+ "<th>Transfer</th>" + "<th>Inactivate</th>" + "</tr>");
	}

	public void printAccountInfo(Account acc) throws IOException {
		out.println("<tr><td>" + acc.getNumber() + "</td>" + "<td>" + acc.getOwner() + "</td>" + "<td>" + acc.getBalance() + "</td>"
				+ "<td>" + acc.isActive() + "</td>");
	}

	public void printDepositOption(Account acc) throws IOException {
		out.println("<td><form action=\"\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"action\" value=\"deposit\">");
		out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
		out.println("Deposit Amount: <input type=\"text\" name=\"amount\">");
		out.println("<input type=\"submit\" value=\"Deposit\">");
		out.println("</form></td>");
	}

	public void printWithdrawOption(Account acc) throws IOException {
		out.println("<td><form action=\"\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"action\" value=\"withdraw\">");
		out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
		out.println("Withdraw Amount: <input type=\"text\" name=\"amount\">");
		out.println("<input type=\"submit\" value=\"Withdraw\">");
		out.println("</form></td>");
	}

	public void printTransferOption(Account acc, Set<String> accNumbers) throws IOException {
		String accNumber = acc.getNumber();
		out.println("<td><form action=\"\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"action\" value=\"transfer\">");
		out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
		out.println("<table><tr><td>Transfer To Acc:</td><td>");
		out.println("<select name=\"ToAccount\">");
		accNumbers.stream().filter(x -> !x.equals(accNumber)).forEach(accNum -> out.println("<option value=\"" 
				+ accNum + "\">" + accNum + "</option>"));
		out.println("</select></td></tr>");
		out.println("<tr><td>Amount</td><td><input type=\"text\" name=\"amount\"></td></tr>");
		out.println("<tr><td colspan=\"2\"><input type=\"submit\" value=\"Transfer\"></td></tr></table>");
		out.println("</form></td>");
	}

	public void printSetInactiveOption(Account acc) throws IOException{
		out.println("<td><form action=\"\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"action\" value=\"inactivate\">");
		out.println("<input type=\"hidden\" name=\"accountNo\" value=\"" + acc.getNumber() + "\">");
		out.println("<input type=\"submit\" value=\"Inactivate\">");
		out.println("</form></td>");
		out.println("</tr>");
	}
	
	public void printCreateAccountOption() {
		out.println("</table>");

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

}
