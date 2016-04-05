package messageboard.client;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ClientHashHandler implements SOAPHandler<SOAPMessageContext> {
	private final String password;
	private final String name;

	public ClientHashHandler(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public void close(MessageContext mCtx) {
	}

	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleFault(SOAPMessageContext mCtx) {
		// just print the message to System.err.
		try {
			SOAPMessage msg = mCtx.getMessage();
			msg.writeTo(System.err);
			System.err.println();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public boolean handleMessage(SOAPMessageContext mCtx) {
		boolean outbound = (boolean) mCtx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			try {
				SOAPMessage soapMessage = mCtx.getMessage();
				SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();

				soapMessage.writeTo(System.err);
				System.err.println();

				// Ensure there is a header and add a 'wrapper' element.
				if (envelope.getHeader() == null) envelope.addHeader();
				SOAPHeader header = envelope.getHeader();
				QName qn = new QName("http://messageBoardSOAP", "credentials");
				SOAPHeaderElement h = header.addHeaderElement(qn);
				h.setMustUnderstand(true);

				// Now insert credentials into the header.
				String timeStamp = getTimestamp();
				String signature = getSignature(this.name, timeStamp, this.password);

				append(h, "name", this.name);
				append(h, "signature", signature);
				append(h, "timestamp", timeStamp);

				soapMessage.saveChanges();
				soapMessage.writeTo(System.err);
				System.err.println();
			} catch (Exception e) {
				throw new RuntimeException("SOAPException thrown.", e);
			}
		}
		return true;
	}

	private String getSignature(String name, String timestamp, String password) {
		try {
			System.err.println("Name == " + name);
			System.err.println("Timestamp == " + timestamp);

			String toSign = name + timestamp;

			Mac signer = Mac.getInstance("HmacSHA256");
			SecretKeySpec keySpec = new SecretKeySpec(getBytes(password), "HmacSHA256");

			signer.init(keySpec);
			signer.update(getBytes(toSign));
			byte[] signBytes = signer.doFinal();

			Base64.Encoder encoder = Base64.getEncoder();
			String signature = new String(encoder.encode(signBytes));
			return signature;
		} catch (Exception e) {
			throw new RuntimeException("NoSuchAlgorithmException thrown.", e);
		}
	}

	private String getTimestamp() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(calendar.getTime());
	}

	private void append(Node node, String elementName, String elementText) {
		Element element = node.getOwnerDocument().createElement(elementName);
		element.setTextContent(elementText);
		node.appendChild(element);
	}

	private byte[] getBytes(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
