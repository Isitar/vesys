
package bank.soap.jaxws;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getAccountsResponse", namespace = "http://soap.bank/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAccountsResponse", namespace = "http://soap.bank/")
public class GetAccountsResponse {

    @XmlElement(name = "return", namespace = "")
    private Map<String, bank.Account> _return;

    /**
     * 
     * @return
     *     returns Map<String,Account>
     */
    public Map<String, bank.Account> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Map<String, bank.Account> _return) {
        this._return = _return;
    }

}
