package bank.jms;

import java.io.IOException;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import bank.InactiveException;
import bank.OverdrawException;

@WebService
public interface Service {

	@WebMethod
	Set<String> getAccountNumbers() throws IOException;

	@WebMethod
	boolean AccountExists(@WebParam(name = "number") String number) throws IOException;

	@WebMethod
	String createAccount(@WebParam(name = "owner") String owner) throws IOException;

	@WebMethod
	void deposit(@WebParam(name = "number") String number, @WebParam(name = "amount") double amount)
			throws IllegalArgumentException, IOException, InactiveException;

	@WebMethod
	void withdraw(@WebParam(name = "number") String number, @WebParam(name = "amount") double amount)
			throws IllegalArgumentException, IOException, OverdrawException, InactiveException;

	@WebMethod
	boolean setActive(@WebParam(name = "number") String number, @WebParam(name = "active") boolean active)
			throws IOException;

	@WebMethod
	String getOwner(@WebParam(name = "number") String number) throws IOException;

	@WebMethod
	boolean isActive(@WebParam(name = "number") String number) throws IOException;

	@WebMethod
	double getBalance(@WebParam(name = "number") String number) throws IOException;

}
