package bank.soap;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface Service {
	List<String> getAccountNumbers();

	String createAccount(@WebParam(name = "owner") String owner);
}
