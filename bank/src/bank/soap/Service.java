package bank.soap;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface Service {
	
	@WebMethod
	List<String> getAccountNumbers();

	@WebMethod
	String createAccount(@WebParam(name = "owner") String owner);
	
	@WebMethod
	boolean closeAccount(@WebParam(name = "number") String number);

	@WebMethod
	void deposit(@WebParam(name = "number") String number, @WebParam(name = "amount") double amount);

	@WebMethod
	void withdraw(@WebParam(name = "number") String number, @WebParam(name = "amount") double amount);
	
	@WebMethod
	void transfer(@WebParam(name = "fromAcc") String fromAcc, @WebParam(name = "toAcc") String toAcc, @WebParam(name = "amount") double amount);

}
