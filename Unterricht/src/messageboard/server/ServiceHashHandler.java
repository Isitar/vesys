package messageboard.server;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServiceHashHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public void close(MessageContext mCtx) {
	}

	@Override
	public Set<QName> getHeaders() {
		// returns the headers handled by this handler
		// http://dwuysan.wordpress.com/2012/04/02/jax-ws-wsimport-and-the-error-mustunderstand-headers-not-understood/
		final QName securityHeader = new QName("http://messageBoardSOAP", "credentials");
		return Collections.singleton(securityHeader);
	}

	@Override
	public boolean handleFault(SOAPMessageContext mCtx) {
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext mCtx) {
		boolean outbound = (boolean) mCtx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!outbound) {
			try {
				SOAPMessage msg = mCtx.getMessage();
				SOAPHeader soapHeader = msg.getSOAPHeader();
				if (soapHeader == null) {
					generateFault(msg, "No header!");
				}
				
//				QName qn = new QName("http://messageBoardSOAP", "credentials");
//				NodeList nodes = soapHeader.getChildNodes();
//				if(nodes.getLength() == 0) {
//					generateFault(msg, "credentials header is missing!");
//				}
//				Node node = nodes.item(0);
				
				Node node = soapHeader.getFirstChild(); // credentials
				NodeList nodeList = node.getChildNodes(); // Name, Timestamp, Signature
				if (nodeList.getLength() < 3) {
					generateFault(msg, "Too few header blocks!");
				}
				
//				for(int i = 0; i < nodeList.getLength(); i++) {
//					System.out.println(nodeList.item(i));
//					nodeList.item(i).getNodeName();
//				}

				// Extract the required attributes.
				String name = nodeList.item(0).getFirstChild().getNodeValue();
				String signature = nodeList.item(1).getFirstChild().getNodeValue();
				String timestamp = nodeList.item(2).getFirstChild().getNodeValue();

				if (name == null || timestamp == null || signature == null) {
					generateFault(msg, "Missing header key/value pairs!");
				}
				
				// Check the timestamp field
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date requestDate = dateFormat.parse(timestamp);
				Calendar calendar = Calendar.getInstance();
				Date currentDate = calendar.getTime();
				long diff = currentDate.getTime() - requestDate.getTime();
				if(diff > 10000) { generateFault(msg, "request expired!"); }

				// Generate comparison signature and compare against what's sent.
				String secret = DataStore.get(name);
				if (secret == null) {
					generateFault(msg, name + " not registered!");
				}
				
				String localSignature = getSignature(name, timestamp, getBytes(secret));

				if (!signature.equals(localSignature)) {
					generateFault(msg, "HMAC signatures do not match.");
				}
			} catch (Exception e) {
				throw new RuntimeException("SOAPException thrown.", e);
			}
		}
		return true;
	}

	private String getSignature(String name, String timestamp, byte[] secretBytes) {
		try {
			System.err.println("Name ==      " + name);
			System.err.println("Timestamp == " + timestamp);

			String toSign = name + timestamp;
			byte[] toSignBytes = getBytes(toSign);

			Mac signer = Mac.getInstance("HmacSHA256");
			SecretKeySpec keySpec = new SecretKeySpec(secretBytes, "HmacSHA256");

			signer.init(keySpec);
			signer.update(toSignBytes);
			byte[] signBytes = signer.doFinal();

			Base64.Encoder encoder = Base64.getEncoder();
			String signature = new String(encoder.encode(signBytes));
			return signature;
		} catch (Exception e) {
			throw new RuntimeException("NoSuchAlgorithmException thrown.", e);
		}
	}

	private byte[] getBytes(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void generateFault(SOAPMessage msg, String reason) {
		try {
			SOAPBody body = msg.getSOAPBody();
			SOAPFault fault = body.addFault();
			fault.setFaultString(reason);
			throw new SOAPFaultException(fault);
		} catch (SOAPException e) {
		}
	}
}
